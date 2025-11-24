package gl.morpion.controllers.menu;
import gl.morpion.model.*;
import gl.morpion.view.player.BotDifficultyView;
import gl.morpion.view.player.PlayerNamesView;
import gl.morpion.controllers.GameController;
import gl.morpion.view.menu.*;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * <h1>controller MainMenuController</h1>
 * <h2>Elements of MainMenuController</h2>
 */
public class MainMenuController {
    /**
     * <h3>private stage</h3>
     * The visual of the game
     */
    private final Stage stage;
    /**
     * <h3>private WIDTH and HEIGHT</h3>
     * size of the window of the game
     */
    private final int WIDTH = 800, HEIGHT = 900;

    /**
     * <h2>Functions of MainMenuController</h2>
     */

    /**
     * <h3>MainMenuController</h3>
     * constructor of MainMenuController
     * @param stage the visual of game
     */

    public MainMenuController(Stage stage) {
        this.stage = stage;
    }

    /**
     * <h3>showMainMenu</h3>
     * Show the window of the game
     */
    public void showMainMenu() {
        MainMenuView menu = new MainMenuView(this);
        stage.setScene(new Scene(menu, WIDTH, HEIGHT));
    }

    /**
     * <h3>showMode</h3>
     * Permit to go to the selected mode or to quit
     * @param modeName name of the name (PvsP, PvsBot)
     */
    public void showMode(String modeName) {
        if(modeName == "QUIT"){
            Stage s = (Stage)stage.getScene().getWindow();
            s.close();
        }
        ModePlaceholderView view = new ModePlaceholderView("I'm in " + modeName,
                this::showMainMenu
        );
        stage.setScene(new Scene(view, WIDTH, HEIGHT));
    }

    /**
     * <h3>startModePvp</h3>
     * Initialize the Player vs Player mod
     */
    public void startModePvp() {
        PlayerNamesView namesView = new PlayerNamesView(
                // onStart: recieve (name1, name2)
                (name1, name2) -> {
                    Player p1 = new Player(name1, 0, new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(), TypeOfSymbol.CROSS));
                    Player p2 = new Player(name2, 0, new Symbol(getClass().getResource("/gl/morpion/cercle.png").toExternalForm(), TypeOfSymbol.CIRCLE));
                    // Create le GameController with two names (see point 3)
                    GameController gameController = new GameController(p1, p2); // get players name
                    //print board
                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu // bouton Retour
                    );
                    Scene scene = new Scene(gameView, WIDTH, HEIGHT);

                    // keep the same CSS !
                    var css = getClass().getResource("/css/menu.css");
                    if (css != null) scene.getStylesheets().add(css.toExternalForm());

                    // Escape to return at the menu
                    scene.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ESCAPE) showMainMenu();
                    });

                    // end of game partie → return to menu
                    gameController.handleGame(this::showMainMenu);
                    //gameController.initializeGame();

                    stage.setScene(scene);
                },
                // onBack → return to main menu
                this::showMainMenu
        );

        System.out.println("PlayerNameView all good");
        //System.out.println("P1 = "+gameController.getListPlayers().get(0)+ "P2 = "+gameController.getListPlayers().get(2));


        Scene scene = new Scene(namesView, WIDTH, HEIGHT);
        // keep the same CSS !
        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
    }

    /**
     * <h3>startModePvsBot</h3>
     * Initialize the Player vs Bot mod
     */
    public void startModePvsBot() {
        PlayerNamesView namesView = new PlayerNamesView(
                true, // vsBot = one player, Player 2 = "BOT"
                (name1, ignored) -> {
                    // 1. Create the player (X)
                    Player human = new Player(
                            name1,
                            0,
                            new Symbol(
                                    getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                                    TypeOfSymbol.CROSS
                            )
                    );

                    // 2. Create the bot (O)
                    Symbol botSymbol = new Symbol(
                            getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                            TypeOfSymbol.CIRCLE
                    );

                    BotPlayer bot = new BotPlayer(
                            "BOT",
                            0,
                            BotPlayer.getCurrentDefaultLevel(),                        // level du bot
                            botSymbol,
                            5,                           // nb symbols align to win
                            new RectangleBoard(
                                    RectangleBoard.DEFAULT_ROW,
                                    RectangleBoard.DEFAULT_COLUMN
                            ).useCase                   // ⚠️ see notes underneath
                    );
                    // ⚠ better : Create Board dans GameController, put useCase from there.
                    // Pour faire simple au début, garde comme dans ton Bot actuel (ou adapte).

                    // 3. Create GameController mode BOT
                    GameController gameController = new GameController(human, bot, true, this::showMainMenu);


                    // 4. Construct view with menu like in PvP
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

                    // If you want to manage end with GameController
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

    /**
     * <h3>startChooseBotDifficulty</h3>
     * manage the choice of the Bot level for the Player vs Bot mod
     */
    public void startChooseBotDifficulty() {
        BotDifficultyView view = new BotDifficultyView(
                difficultyKey -> {
                    // 1) Update global level of bot
                    BotPlayer.changeLevel(difficultyKey);


                    // 2) Start the mod between Player and Bot
                    startModePvsBot();
                },
                this::showMainMenu
        );

        Scene scene = new Scene(view, WIDTH, HEIGHT);
        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
    }




    public void openSettings() { /* todo later */ }

    /**
     * <h3>showRules</h3>
     * Show the rules in the main menu
     */
    public void showRules() {
        RulesView view = new RulesView(this::showMainMenu);
        Scene scene = new Scene(view, WIDTH, HEIGHT);

        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
    }

    public void toggleLanguage(String code) { /*todo later*/ }
}