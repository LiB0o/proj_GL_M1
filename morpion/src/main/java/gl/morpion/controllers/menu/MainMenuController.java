package gl.morpion.controllers.menu;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.persistence.GameData;
import gl.morpion.view.player.PlayerNamesView;
import gl.morpion.controllers.GameController;
import gl.morpion.view.menu.*;
import gl.morpion.persistence.LoadBoard;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class MainMenuController {
    private final Stage stage;
    private final int WIDTH = 800, HEIGHT = 900;
    public MainMenuController(Stage stage) {
        this.stage = stage;
    }
    public void showMainMenu() {
        MainMenuView menu = new MainMenuView(this);
        stage.setScene(new Scene(menu, WIDTH, HEIGHT));
    }

    public void showMode(String modeName) {
        if(modeName == "QUIT"){
            Stage s = (Stage)stage.getScene().getWindow();
            s.close();
        } else if(modeName == "Custom") {
            // Display Custom view with load button
            CustomView customView = new CustomView(
                    this::loadGame,  // Action for "Load" button
                    this::showMainMenu  // Action for "Back" button
            );
            Scene scene = new Scene(customView, WIDTH, HEIGHT);
            
            // Keep the same CSS
            var css = getClass().getResource("/css/menu.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            
            stage.setScene(scene);
        } else {
            ModePlaceholderView view = new ModePlaceholderView("I am in " + modeName,
                    this::showMainMenu
            );
            stage.setScene(new Scene(view, WIDTH, HEIGHT));
        }
    }
    
    /**
     * Loads a saved board and starts the game.
     * Restores player names, current player, and board state from the save file.
     */
    public void loadGame() {
        try {
            // First read metadata from file to get player names
            GameData gameData = readGameDataFromFile();
            
            // Create players with loaded names or defaults
            Player p1, p2;
            if (gameData != null && gameData.getPlayer1Name() != null && gameData.getPlayer2Name() != null) {
                // Use loaded players
                String symbol1Url = convertSymbolToUrl(gameData.getPlayer1Symbol() != null ? 
                        gameData.getPlayer1Symbol() : "croix.jpg");
                String symbol2Url = convertSymbolToUrl(gameData.getPlayer2Symbol() != null ? 
                        gameData.getPlayer2Symbol() : "cercle.png");
                
                p1 = new Player(gameData.getPlayer1Name(), 0, 
                        new Symbol(symbol1Url, TypeOfSymbol.CROSS));
                p2 = new Player(gameData.getPlayer2Name(), 0, 
                        new Symbol(symbol2Url, TypeOfSymbol.CIRCLE));
            } else {
                // Legacy format or missing data: use default players
                p1 = new Player("Player 1", 0, 
                        new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toString(), TypeOfSymbol.CROSS));
                p2 = new Player("Player 2", 0, 
                        new Symbol(getClass().getResource("/gl/morpion/cercle.png").toString(), TypeOfSymbol.CIRCLE));
            }
            
            // Create GameController with players
            GameController gameController = new GameController(p1, p2);
            
            // Load the board into the game
            LoadBoard loadBoard = new LoadBoard(gameController.getGame());
            loadBoard.readJsonFromFile();
            
            // Restore current player if available
            if (gameData != null && gameData.getCurrentPlayerName() != null) {
                Player currentPlayer = gameData.getCurrentPlayerName().equals(p1.getName()) ? p1 : p2;
                gameController.getGame().setCurrentPlayer(currentPlayer);
            }
            
            // Update the board view with loaded data
            gameController.getGameBoardView().update(
                    gameController.getGame().getGameBoard(), 
                    gameController.getGame().getCurrentPlayer().getSymbol()
            );
            
            // Update the active player in the view
            gameController.getGameBoardView().setActivePlayer(gameController.getGame().getCurrentPlayer());
            
            // Display the game board with GameController for saving
            GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                    gameController.getGameBoardView(),
                    this::showMainMenu, // Integrated back button
                    gameController // Pass GameController for saving with players
            );
            Scene scene = new Scene(gameView, WIDTH, HEIGHT);

            // Keep the same CSS
            var css = getClass().getResource("/css/menu.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());

            // Escape to return to menu
            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) showMainMenu();
            });

            // End game logic → return to menu
            gameController.handleGame(this::showMainMenu);

            stage.setScene(scene);
            
        } catch (RuntimeException e) {
            // Display an alert if file doesn't exist or there's an error
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Load Error");
                alert.setHeaderText("Unable to load board");
                alert.setContentText(e.getMessage());
                
                // Apply CSS style
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("/css/alert-style.css").toExternalForm()
                );
                
                alert.showAndWait();
            });
        }
    }
    
    /**
     * Reads metadata from the save file without loading the board.
     * 
     * @return The GameData object containing player information, or null if file doesn't exist or is in legacy format
     */
    private GameData readGameDataFromFile() {
        try {
            java.io.File projectRoot = getProjectRoot();
            java.io.File file = new java.io.File(projectRoot, "save/save.json");
            
            if (!file.exists()) {
                return null;
            }
            
            com.google.gson.Gson gson = new com.google.gson.Gson();
            try (java.io.FileReader reader = new java.io.FileReader(file)) {
                return gson.fromJson(reader, gl.morpion.persistence.GameData.class);
            }
        } catch (Exception e) {
            // If error, return null (legacy format)
            return null;
        }
    }
    
    /**
     * Gets the project root directory.
     * 
     * @return The project root directory as a File object
     */
    private java.io.File getProjectRoot() {
        java.io.File currentDir = new java.io.File(System.getProperty("user.dir"));
        if (currentDir.getName().equals("morpion")) {
            return currentDir.getParentFile();
        }
        return currentDir;
    }
    
    /**
     * Converts a symbol name to a valid URL.
     * 
     * @param symbolName The symbol filename (e.g., "croix.jpg" or "cercle.png")
     * @return The complete resource URL
     */
    private String convertSymbolToUrl(String symbolName) {
        if (symbolName == null || symbolName.isEmpty()) {
            return getClass().getResource("/gl/morpion/croix.jpg").toString();
        }
        if (symbolName.contains("croix")) {
            return getClass().getResource("/gl/morpion/croix.jpg").toString();
        } else if (symbolName.contains("cercle")) {
            return getClass().getResource("/gl/morpion/cercle.png").toString();
        }
        return getClass().getResource("/gl/morpion/croix.jpg").toString();
    }
    
    public void startModePvp() {
        PlayerNamesView namesView = new PlayerNamesView(
                // onStart: reçoit (name1, name2)
                (name1, name2) -> {
                    Player p1 = new Player(name1, 0, new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toString(), TypeOfSymbol.CROSS));
                    Player p2 = new Player(name2, 0, new Symbol(getClass().getResource("/gl/morpion/cercle.png").toString(), TypeOfSymbol.CIRCLE));
                    // Créer le GameController avec les deux noms (voir point 3)
                    GameController gameController = new GameController(p1, p2); // le gzmr recupere le nom des joueurs;
                    //afficher la grille
                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu, // bouton Retour intégré
                            gameController // Passer le GameController pour la sauvegarde avec joueurs
                    );
                    Scene scene = new Scene(gameView, WIDTH, HEIGHT);

                    // garder le même CSS !
                    var css = getClass().getResource("/css/menu.css");
                    if (css != null) scene.getStylesheets().add(css.toExternalForm());

                    // Escape pour revenir au menu
                    scene.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ESCAPE) showMainMenu();
                    });

                    // logiques de fin de partie → retour au menu
                    gameController.handleGame(this::showMainMenu);
                    //gameController.initializeGame();

                    stage.setScene(scene);
                },
                // onBack → retour au menu principal
                this::showMainMenu
        );

        //System.out.println("PlayerNameView passe bien");
        //System.out.println("P1 = "+gameController.getListPlayers().get(0)+ "P2 = "+gameController.getListPlayers().get(2));


        Scene scene = new Scene(namesView, WIDTH, HEIGHT);
        // garder le même CSS !
        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
    }


    public void openSettings() { /* à faire plus tard */ }
    public void showRules() {
        RulesView view = new RulesView(this::showMainMenu);
        Scene scene = new Scene(view, WIDTH, HEIGHT);

        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
    }

    public void toggleLanguage(String code) { /* à faire plus tard */ }
}