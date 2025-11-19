package gl.morpion.controllers.menu;
import gl.morpion.controllers.PvsBotController;
import gl.morpion.model.*;
import gl.morpion.view.GameBoardView;
import gl.morpion.view.player.PlayerNamesView;
import gl.morpion.controllers.GameController;
import gl.morpion.view.menu.*;
import javafx.scene.Scene;
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
        }
        ModePlaceholderView view = new ModePlaceholderView("Je suis dans " + modeName,
                this::showMainMenu
        );
        stage.setScene(new Scene(view, WIDTH, HEIGHT));
    }
    public void startModePvp() {
        PlayerNamesView namesView = new PlayerNamesView(
                // onStart: reçoit (name1, name2)
                (name1, name2) -> {
                    Player p1 = new Player(name1, 0, new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(), TypeOfSymbol.CROSS));
                    Player p2 = new Player(name2, 0, new Symbol(getClass().getResource("/gl/morpion/cercle.png").toExternalForm(), TypeOfSymbol.CIRCLE));
                    // Créer le GameController avec les deux noms (voir point 3)
                    GameController gameController = new GameController(p1, p2); // le gzmr recupere le nom des joueurs;
                    //afficher la grille
                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu // bouton Retour intégré
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

        System.out.println("PlayerNameView passe bien");
        //System.out.println("P1 = "+gameController.getListPlayers().get(0)+ "P2 = "+gameController.getListPlayers().get(2));


        Scene scene = new Scene(namesView, WIDTH, HEIGHT);
        // garder le même CSS !
        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
    }

    public void startModePvsBot() {
        PlayerNamesView namesView = new PlayerNamesView(
                true, // 👉 à condition d'avoir ajouté le constructeur vsBot dans PlayerNamesView
                (name1, ignored) -> {
                    // 1. Création du plateau
                    RectangleBoard board = new RectangleBoard(
                            RectangleBoard.DEFAULT_ROW,
                            RectangleBoard.DEFAULT_COLUMN
                    );

                    // 2. Joueur humain (X)
                    Player human = new Player(
                            name1,
                            0,
                            new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                                    TypeOfSymbol.CROSS)
                    );

                    // 3. Bot (O)
                    Symbol botSymbol = new Symbol(
                            getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                            TypeOfSymbol.CIRCLE
                    );

                    BotPlayer bot = new BotPlayer(
                            "BOT",
                            0,
                            3.698f,           // niveau du bot (coef)
                            botSymbol,
                            5,              // condition de victoire (5 alignés comme ton Game)
                            board.useCase   // cases jouables (déjà remplies dans RectangleBoard)
                    );

                    // 4. Game
                    Game game = new Game(board, human, bot, human);
                    game.addPlayer(human);
                    game.addPlayer(bot);

                    // 5. Vue de la grille
                    GameBoardView boardView = new GameBoardView(board, human, bot);

                    // 6. Contrôleur PvS Bot
                    new PvsBotController(game, boardView, human, bot);

                    // 7. Emballage dans GameBoardWithMenuView + scène
                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            boardView,
                            this::showMainMenu
                    );

                    Scene scene = new Scene(gameView, WIDTH, HEIGHT);
                    var css = getClass().getResource("/css/menu.css");
                    if (css != null) scene.getStylesheets().add(css.toExternalForm());

                    scene.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ESCAPE) showMainMenu();
                    });

                    stage.setScene(scene);
                },
                this::showMainMenu
        );

        Scene scene = new Scene(namesView, WIDTH, HEIGHT);
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