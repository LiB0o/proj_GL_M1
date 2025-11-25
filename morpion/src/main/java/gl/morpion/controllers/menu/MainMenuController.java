package gl.morpion.controllers.menu;

import gl.morpion.controllers.GameController;
import gl.morpion.model.BotPlayer;
import gl.morpion.model.Game;
import gl.morpion.model.Player;
import gl.morpion.model.RectangleBoard;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.view.GameBoardView;
import gl.morpion.view.menu.GameBoardWithMenuView;
import gl.morpion.view.menu.MainMenuView;
import gl.morpion.view.menu.ModePlaceholderView;
import gl.morpion.view.menu.RulesView;
import gl.morpion.view.player.BotDifficultyView;
import gl.morpion.view.player.PlayerNamesView;
import gl.morpion.view.player.WinConditionView;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenuController {

    private final Stage stage;
    private final StackPane root;   // 🔹 conteneur racine de la Scene unique

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    // nb de symboles alignés pour gagner
    private int currentWinCondition = Game.getDefaultMaxNumberSymbolAlign();

    public MainMenuController(Stage stage, StackPane root) {
        this.stage = stage;
        this.root = root;
    }

    // 🔁 Change le contenu de la même Scene
    private void setView(Node view) {
        root.getChildren().setAll(view);
    }

    // ========== MENU PRINCIPAL ==========

    public void showMainMenu() {
        System.out.println("Je suis dans : MENU PRINCIPAL");

        MainMenuView menu = new MainMenuView(this);
        setView(menu);
    }

    // appelé par MainMenuView pour les modes génériques (simulation)
    public void showMode(String modeName) {
        // ⚠️ en Java on compare les String avec equals
        if ("QUIT".equals(modeName)) {
            Stage s = (Stage) stage.getScene().getWindow();
            s.close();
            return;
        }

        System.out.println("Je suis dans : " + modeName);

        ModePlaceholderView view = new ModePlaceholderView(
                "Je suis dans " + modeName,
                this::showMainMenu // bouton Back
        );

        // 🔹 AVANT tu faisais : stage.setScene(new Scene(view, WIDTH, HEIGHT));
        // 🔹 MAINTENANT : on reste sur la même Scene, on change juste le contenu
        setView(view);
    }

    public void openSettings() {
        // à implémenter plus tard
    }

    public void toggleLanguage(String code) {
        // à implémenter plus tard
    }

    // ========== MODE PLAYER vs PLAYER ==========

    public void startModePvp() {
        System.out.println("Je suis dans : ÉCRAN CHOIX NB SYMBOLES (PVP)");

        WinConditionView winView = new WinConditionView(
                currentWinCondition,
                value -> {
                    // on mémorise la nouvelle condition de victoire
                    currentWinCondition = value;

                    // (optionnel) si tu as une méthode globale dans Game :
                    // Game.setDefaultMaxNumberSymbolAlign(currentWinCondition);

                    System.out.println("Je suis dans : MODE PLAYER VS PLAYER (NOMS)");

                    // ⇨ maintenant on passe à l'écran pour taper les noms
                    PlayerNamesView namesView = new PlayerNamesView(
                            (name1, name2) -> {
                                System.out.println("Je suis dans : MODE PLAYER VS PLAYER (JEU)");

                                // Joueurs avec tes symboles
                                Player p1 = new Player(
                                        name1,
                                        0,
                                        new Symbol(
                                                getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                                                TypeOfSymbol.CROSS
                                        )
                                );
                                Player p2 = new Player(
                                        name2,
                                        0,
                                        new Symbol(
                                                getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                                                TypeOfSymbol.CIRCLE
                                        )
                                );

                                // Si tu as un constructeur qui prend la win condition :
                                // GameController gameController = new GameController(p1, p2, currentWinCondition);
                                // Sinon tu gardes celui-ci :
                                GameController gameController = new GameController(p1, p2);

                                GameBoardView boardView = gameController.getGameBoardView();

                                GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                                        boardView,
                                        this::showMainMenu
                                );

                                // callback de fin → retour au menu
                                gameController.handleGame(this::showMainMenu);

                                // on affiche la vue de jeu dans la MÊME scène
                                setView(gameView);
                            },
                            this::showMainMenu   // bouton Back depuis PlayerNamesView
                    );

                    // on affiche PlayerNamesView dans la même scène
                    setView(namesView);
                },
                this::showMainMenu   // bouton Back depuis WinConditionView
        );

        // on affiche WinConditionView
        setView(winView);
    }


    // ========== MODE PLAYER vs BOT : choix nb symboles & difficulté ==========

    public void startChooseBotDifficulty() {
        System.out.println("Je suis dans : ÉCRAN CHOIX NB SYMBOLES");

        WinConditionView winView = new WinConditionView(
                currentWinCondition,
                value -> {
                    currentWinCondition = value;

                    System.out.println("Je suis dans : CHOIX DIFFICULTÉ BOT");

                    BotDifficultyView diffView = new BotDifficultyView(
                            difficultyKey -> {
                                BotPlayer.changeLevel(difficultyKey);
                                startModePvsBot(currentWinCondition);
                            },
                            this::showMainMenu
                    );

                    setView(diffView);
                },
                this::showMainMenu
        );

        setView(winView);
    }

    // ========== MODE PLAYER vs BOT : nom + lancement ==========

    public void startModePvsBot(int winCondition) {
        System.out.println("Je suis dans : MODE PLAYER VS BOT (winCondition = " + winCondition + ")");

        PlayerNamesView namesView = new PlayerNamesView(
                true,   // vsBot : un seul champ pour le joueur humain
                (name1, ignored) -> {
                    // Joueur humain (X)
                    Player human = new Player(
                            name1,
                            0,
                            new Symbol(
                                    getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                                    TypeOfSymbol.CROSS
                            )
                    );

                    // Symbole du bot (O)
                    Symbol botSymbol = new Symbol(
                            getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                            TypeOfSymbol.CIRCLE
                    );

                    // BotPlayer (adapte si ton constructeur est différent)
                    BotPlayer bot = new BotPlayer(
                            "BOT",
                            0,
                            BotPlayer.getCurrentDefaultLevel(),
                            botSymbol,
                            winCondition,
                            new RectangleBoard(
                                    RectangleBoard.DEFAULT_ROW,
                                    RectangleBoard.DEFAULT_COLUMN
                            ).useCase
                    );

                    // Contrôleur de jeu VS BOT
                    // ⚠️ Si ton GameController n'a pas ce constructeur,
                    //     adapte-le (par ex. new GameController(human, bot))
                    GameController gameController =
                            new GameController(human, bot, true, this::showMainMenu);

                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu
                    );

                    setView(gameView);
                },
                this::showMainMenu
        );

        setView(namesView);
    }

    // ========== RÈGLES ==========

    public void showRules() {
        System.out.println("Je suis dans : ÉCRAN DES RÈGLES");

        RulesView view = new RulesView(this::showMainMenu);
        setView(view);
    }
}
