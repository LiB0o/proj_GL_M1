package gl.morpion.controllers.menu;
import gl.morpion.model.*;
import gl.morpion.view.player.BotDifficultyView;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.persistence.GameData;
import javafx.util.Pair;
import java.util.HashMap;
import java.util.Map;
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

import gl.morpion.view.player.WinConditionView;

import gl.morpion.model.BotPlayer;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.model.RectangleBoard;
import gl.morpion.view.menu.GameBoardWithMenuView;




public class MainMenuController {
    private final Stage stage;
    private final int WIDTH = 800, HEIGHT = 900;
    private int currentWinCondition = Game.getDefaultMaxNumberSymbolAlign();
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
            // Display Custom view with two load buttons (PvP and PvB)
            CustomView customView = new CustomView(
                    this::loadGamePvP,  // Action for "Load PvP" button
                    this::loadGamePvB,  // Action for "Load PvB" button
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
     * Loads a saved Player vs Player game.
     * Restores player names, current player, and board state from save_pvp.json.
     */
    public void loadGamePvP() {
        loadGame("save_pvp.json", false);
    }
    
    /**
     * Loads a saved Player vs Bot game.
     * Restores player names, current player, bot difficulty, and board state from save_pvb.json.
     */
    public void loadGamePvB() {
        loadGame("save_pvb.json", true);
    }
    
    /**
     * Loads a saved board and starts the game.
     * Restores player names, current player, and board state from the save file.
     * 
     * @param fileName The name of the save file to load
     * @param expectBotMode Whether to expect bot mode (true) or player vs player (false)
     */
    private void loadGame(String fileName, boolean expectBotMode) {
        try {
            // First read metadata from file to get player names
            GameData gameData = readGameDataFromFile(fileName);
            
            // Check if this is a bot game (use expectBotMode or check data)
            Boolean p1IsBot = gameData != null ? gameData.getPlayer1IsBot() : null;
            Boolean p2IsBot = gameData != null ? gameData.getPlayer2IsBot() : null;
            boolean isBotMode = expectBotMode || (p1IsBot != null && p1IsBot) || (p2IsBot != null && p2IsBot);
            
            // Create board first (will be loaded with saved data)
            RectangleBoard board = new RectangleBoard(
                RectangleBoard.DEFAULT_ROW,
                RectangleBoard.DEFAULT_COLUMN
            );
            
            // Create temporary players to load the board
            Player tempP1 = new Player("temp1", 0, 
                    new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toString(), TypeOfSymbol.CROSS));
            Player tempP2 = new Player("temp2", 0, 
                    new Symbol(getClass().getResource("/gl/morpion/cercle.png").toString(), TypeOfSymbol.CIRCLE));
            Game tempGame = new Game(board, tempP1, tempP2, tempP1);
            
            // Load the board state first
            LoadBoard loadBoard = new LoadBoard(tempGame);
            loadBoard.readJsonFromFile(fileName);
            
            // Now create the real players with loaded data
            Player p1, p2;
            
            if (gameData != null && gameData.getPlayer1Name() != null && gameData.getPlayer2Name() != null) {
                // Use loaded players
                String symbol1Url = convertSymbolToUrl(gameData.getPlayer1Symbol() != null ? 
                        gameData.getPlayer1Symbol() : "croix.jpg");
                String symbol2Url = convertSymbolToUrl(gameData.getPlayer2Symbol() != null ? 
                        gameData.getPlayer2Symbol() : "cercle.png");
                
                if (isBotMode) {
                    // Create human player
                    Player human = (p1IsBot != null && p1IsBot) ? 
                        new Player(gameData.getPlayer2Name(), 0, new Symbol(symbol2Url, TypeOfSymbol.CIRCLE)) :
                        new Player(gameData.getPlayer1Name(), 0, new Symbol(symbol1Url, TypeOfSymbol.CROSS));
                    
                    // Create bot player with saved difficulty and win condition
                    Float botDifficulty = gameData.getBotDifficulty() != null ? 
                        gameData.getBotDifficulty() : BotPlayer.NORMAL_LEVEL;
                    Integer winCondition = gameData.getWinCondition() != null ? 
                        gameData.getWinCondition() : Game.getDefaultMaxNumberSymbolAlign();
                    
                    // Determine bot symbol
                    TypeOfSymbol botSymbolType = (p1IsBot != null && p1IsBot) ? 
                        TypeOfSymbol.CROSS : TypeOfSymbol.CIRCLE;
                    String botSymbolUrl = (p1IsBot != null && p1IsBot) ? symbol1Url : symbol2Url;
                    
                    // Create bot player with the loaded board (important: use the board that was just loaded)
                    BotPlayer bot = new BotPlayer(
                        (p1IsBot != null && p1IsBot) ? gameData.getPlayer1Name() : gameData.getPlayer2Name(),
                        0,
                        botDifficulty,
                        new Symbol(botSymbolUrl, botSymbolType),
                        winCondition,
                        board.useCase  // Use the board that was just loaded
                    );
                    
                    // Update bot's boardView with loaded symbols
                    updateBotBoardView(bot, tempGame);
                    
                    p1 = (p1IsBot != null && p1IsBot) ? bot : human;
                    p2 = (p1IsBot != null && p1IsBot) ? human : bot;
                } else {
                    // Regular Player vs Player mode
                    p1 = new Player(gameData.getPlayer1Name(), 0, 
                            new Symbol(symbol1Url, TypeOfSymbol.CROSS));
                    p2 = new Player(gameData.getPlayer2Name(), 0, 
                            new Symbol(symbol2Url, TypeOfSymbol.CIRCLE));
                }
            } else {
                // Legacy format or missing data: use default players
                p1 = new Player("Player 1", 0, 
                        new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toString(), TypeOfSymbol.CROSS));
                p2 = new Player("Player 2", 0, 
                        new Symbol(getClass().getResource("/gl/morpion/cercle.png").toString(), TypeOfSymbol.CIRCLE));
            }
            
            // Create GameController with players (different constructor for bot mode)
            GameController gameController;
            if (isBotMode && (p1 instanceof BotPlayer || p2 instanceof BotPlayer)) {
                BotPlayer bot = (p1 instanceof BotPlayer) ? (BotPlayer) p1 : (BotPlayer) p2;
                Player human = (p1 instanceof BotPlayer) ? p2 : p1;
                // Create GameController (it will create a new board, we'll copy the loaded data to it)
                gameController = new GameController(human, bot, true, this::showMainMenu);
                // Copy loaded board state to GameController's board
                copyBoardState(board, gameController.getGame().getGameBoard());
                // Update bot's boardView with loaded symbols
                updateBotBoardView(bot, gameController.getGame());
            } else {
                gameController = new GameController(p1, p2);
                // Copy loaded board state to GameController's board
                copyBoardState(board, gameController.getGame().getGameBoard());
            }
            
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
            if (!isBotMode) {
                gameController.handleGame(this::showMainMenu);
            }
            // For bot mode, PvsBotController handles the game logic (already created in GameController constructor)

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
     * @param fileName The name of the save file to read
     * @return The GameData object containing player information, or null if file doesn't exist or is in legacy format
     */
    private GameData readGameDataFromFile(String fileName) {
        try {
            java.io.File projectRoot = getProjectRoot();
            java.io.File file = new java.io.File(projectRoot, "save/" + fileName);
            
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
    
    /**
     * Copies board state from source board to destination board.
     * This is used to transfer loaded board data to the GameController's board.
     * 
     * @param source The source board with loaded data
     * @param destination The destination board to copy to
     */
    private void copyBoardState(GameBoard source, GameBoard destination) {
        // Copy all symbols from source to destination
        for (int i = 0; i < source.getRow(); i++) {
            for (int j = 0; j < source.getColumn(); j++) {
                if (source.isValidCase(i, j)) {
                    Symbol symbol = source.getSymbolInCase(i, j);
                    if (symbol != null && !source.isEmptyCase(i, j)) {
                        destination.placeSymbol(symbol, i, j);
                    }
                }
            }
        }
    }
    
    /**
     * Updates the bot's boardView to reflect the loaded game state.
     * This synchronizes the bot's internal board representation with the actual loaded board.
     * 
     * @param bot The bot player to update
     * @param game The game containing the loaded board state
     */
    private void updateBotBoardView(BotPlayer bot, Game game) {
        // Get all used cases from the game
        HashMap<Pair<Integer, Integer>, Symbol> usedCase = game.getUsedCase();
        
        // Update bot's boardView based on loaded symbols
        for (Map.Entry<Pair<Integer, Integer>, Symbol> entry : usedCase.entrySet()) {
            Pair<Integer, Integer> position = entry.getKey();
            Symbol symbol = entry.getValue();
            
            if (symbol != null) {
                // Check if this symbol belongs to the bot
                if (symbol.equals(bot.getSymbol())) {
                    // Bot's symbol: set to 0.0f
                    bot.symbolPutByBot(position);
                } else {
                    // Human's symbol: set to -1.0f
                    bot.symbolPutByPlayer(position);
                }
            }
        }
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

    public void startModePvsBot(int WIN_CONDITION) {
        PlayerNamesView namesView = new PlayerNamesView(
                true, // vsBot = un seul joueur, Player 2 = "BOT"
                (name1, ignored) -> {
                    // 1. Créer le joueur humain (X)
                    Player human = new Player(
                            name1,
                            0,
                            new Symbol(
                                    getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                                    TypeOfSymbol.CROSS
                            )
                    );

                    // 2. Créer le bot (O)
                    Symbol botSymbol = new Symbol(
                            getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                            TypeOfSymbol.CIRCLE
                    );

                    BotPlayer bot = new BotPlayer(
                            "BOT",
                            0,
                            BotPlayer.getCurrentDefaultLevel(),                        // niveau du bot
                            botSymbol, WIN_CONDITION,                           // nb symboles alignés pour gagner
                            new RectangleBoard(
                                    RectangleBoard.DEFAULT_ROW,
                                    RectangleBoard.DEFAULT_COLUMN
                            ).useCase                   // ⚠️ voir remarque ci-dessous
                    );
                    // ⚠ mieux : créer Board dans GameController, et passer useCase depuis là.
                    // Pour faire simple au début, garde comme dans ton Bot actuel (ou adapte).

                    // 3. Créer le GameController mode BOT
                    GameController gameController = new GameController(human, bot, true, this::showMainMenu);


                    // 4. Construire la vue avec menu comme en PvP
                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu
                    );

                    Scene scene = new Scene(gameView, WIDTH, HEIGHT);

                    var css = getClass().getResource("/css/menu.css");
                    if (css != null) scene.getStylesheets().add(css.toExternalForm());

                    scene.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ESCAPE) showMainMenu();
                    });

                    // Si tu veux gérer la fin via GameController :
                    // gameController.handleGame(this::showMainMenu);

                    stage.setScene(scene);
                },
                this::showMainMenu
        );

        Scene scene = new Scene(namesView, WIDTH, HEIGHT);
        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
        stage.setScene(scene);
    }
    public void startChooseBotDifficulty() {
        // 1) On commence par afficher l'écran pour choisir le nombre de symboles
        WinConditionView winView = new WinConditionView(
                currentWinCondition,          // valeur actuelle (ex: 5)
                value -> {


                    // on mémorise dans le contrôleur
                    currentWinCondition = value;

                    // 🔹 très important : on pousse la valeur dans le modèle Game
                    // (assume que tu as ajouté Game.setDefaultMaxNumberSymbolAlign(int))
                    Game.setDefaultMaxNumberSymbolAlign(value);

                    // 2) Une fois validé, on passe à l'écran de difficulté du bot
                    BotDifficultyView diffView = new BotDifficultyView(
                            difficultyKey -> {
                                // a) changer le niveau global de l'IA
                                BotPlayer.changeLevel(difficultyKey);

                                // b) lancer le mode Player vs Bot avec cette winCondition
                                startModePvsBot(currentWinCondition);
                            },
                            this::showMainMenu
                    );

                    Scene scene2 = new Scene(diffView, WIDTH, HEIGHT);
                    var css2 = getClass().getResource("/css/menu.css");
                    if (css2 != null) scene2.getStylesheets().add(css2.toExternalForm());

                    stage.setScene(scene2);
                },
                this::showMainMenu
        );

        // 1ère scène : choix du nombre de symboles
        Scene scene1 = new Scene(winView, WIDTH, HEIGHT);
        var css1 = getClass().getResource("/css/menu.css");
        if (css1 != null) scene1.getStylesheets().add(css1.toExternalForm());

        stage.setScene(scene1);
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