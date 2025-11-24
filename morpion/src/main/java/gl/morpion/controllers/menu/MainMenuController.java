package gl.morpion.controllers.menu;
import gl.morpion.model.*;
import gl.morpion.view.player.BotDifficultyView;
import gl.morpion.view.player.PlayerNamesView;
import gl.morpion.controllers.GameController;
import gl.morpion.view.menu.*;
import javafx.scene.Scene;
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