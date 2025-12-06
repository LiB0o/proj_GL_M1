package gl.morpion.controllers.menu;

import gl.morpion.controllers.GameController;
import gl.morpion.model.BotPlayer;
import gl.morpion.model.Game;
import gl.morpion.model.GameMode;
import gl.morpion.model.Player;
import gl.morpion.model.RectangleBoard;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.persistence.SaveManager;
import gl.morpion.persistence.SaveMetadata;
import gl.morpion.view.GameBoardView;
import gl.morpion.view.menu.CustomModeView;
import gl.morpion.view.menu.CustomView;
import gl.morpion.view.menu.GameBoardWithMenuView;
import gl.morpion.view.menu.MainMenuView;
import gl.morpion.view.menu.ModePlaceholderView;
import gl.morpion.view.menu.RulesView;
import gl.morpion.view.menu.SaveListView;
import gl.morpion.view.player.BotDifficultyView;
import gl.morpion.view.player.PlayerNamesView;
import gl.morpion.view.player.WinConditionView;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

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

    public void showMode(String modeName) {
        if ("QUIT".equals(modeName)) {
            Stage s = (Stage) stage.getScene().getWindow();
            s.close();
            return;
        }

        // 🔹 ICI : quand on clique sur le bouton "Custom"
        if ("Custom".equalsIgnoreCase(modeName)) {
            // 👉 d’abord l’écran avec "Play a new game / Load a saved game"
            showCustomEntry();
            return;
        }

        System.out.println("Je suis dans : " + modeName);
        ModePlaceholderView view = new ModePlaceholderView(
                "Je suis dans " + modeName,
                this::showMainMenu
        );
        setView(view);
    }



    public void openSettings() {
        // à implémenter plus tard
    }

    public void toggleLanguage(String code) {
        // à implémenter plus tard
    }

    // ========== ÉCRAN D'ENTRÉE MODE CUSTOM (New / Load) ==========

    public void showCustomEntry() {
        System.out.println("Je suis dans : CUSTOM ENTRY (New / Load)");
        CustomView view = new CustomView(
                // New custom game → on lance le flow habituel de config
                this::startModeCustom,
                // Load saved game → ouvre le menu des sauvegardes
                this::showCustomLoadMenu,
                // Back → retour au menu principal
                this::showMainMenu
        );
        setView(view);
    }

    /**
     * Menu des sauvegardes pour le mode Custom.
     * Utilise SaveManager.listSaves() et affiche SaveListView.
     */
    private void showCustomLoadMenu() {
        System.out.println("Je suis dans : MENU DES SAUVEGARDES (Custom)");

        List<SaveMetadata> saves = SaveManager.listSaves();
        if (saves == null || saves.isEmpty()) {
            // petit fallback : message simple si aucune sauvegarde
            ModePlaceholderView empty = new ModePlaceholderView(
                    "No saved games found.",
                    this::showCustomEntry
            );
            setView(empty);
            return;
        }

        SaveListView view = new SaveListView(
                saves,
                this::loadCustomSave,   // quand on clique sur Play
                this::showCustomEntry   // bouton Back
        );
        setView(view);
    }

    /**
     * Chargement d'une sauvegarde Custom sélectionnée dans SaveListView.
     * Ici on délègue à SaveManager la recréation du GameController & co.
     */
    private void loadCustomSave(SaveMetadata metadata) {
        System.out.println("Loading save: " + metadata.getSaveName());

        // 👉 ICI tu dois utiliser les méthodes de ton SaveManager
        // pour reconstruire Game, GameBoard, GameController, etc.
        //
        // Exemple de design possible (à adapter à ton vrai SaveManager) :
        //
        // LoadedGame loaded = SaveManager.loadGame(metadata);
        // GameController gameController = loaded.getGameController();
        // GameMode mode = loaded.getGameMode();
        // int winCond = loaded.getWinCondition();
        // String botDiff = loaded.getBotDifficulty();
        //
        // GameBoardWithMenuView gameView = new GameBoardWithMenuView(
        //         gameController.getGameBoardView(),
        //         this::showMainMenu,
        //         gameController,
        //         mode,
        //         winCond,
        //         botDiff
        // );
        // setView(gameView);
        //
        // Pour l'instant, si ton SaveManager n'a pas encore ça,
        // tu peux juste faire un placeholder :
        ModePlaceholderView view = new ModePlaceholderView(
                "LOAD NOT IMPLEMENTED YET\nSelected save: " + metadata.getSaveName(),
                this::showCustomLoadMenu
        );
        setView(view);
    }

    // ========== MODE PLAYER vs PLAYER ==========

    public void startModePvp() {
        System.out.println("Je suis dans : ÉCRAN CHOIX NB SYMBOLES (PVP)");

        WinConditionView winView = new WinConditionView(
                currentWinCondition,
                value -> {
                    // on mémorise la nouvelle condition de victoire
                    currentWinCondition = value;

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

                                // Contrôleur de jeu PVP standard (board par défaut)
                                GameController gameController = new GameController(p1, p2);

                                GameBoardView boardView = gameController.getGameBoardView();

                                GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                                        boardView,
                                        this::showMainMenu,
                                        gameController,
                                        GameMode.PVP,          // 🔹 mode
                                        currentWinCondition,   // 🔹 nb de symboles choisis
                                        null                   // 🔹 pas de bot
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

                    // BotPlayer
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
                    GameController gameController =
                            new GameController(human, bot, true, this::showMainMenu);

                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu,
                            gameController,
                            GameMode.PVBOT,                          // 🔹 mode
                            winCondition,                            // 🔹 win condition
                            String.valueOf(BotPlayer.getCurrentDefaultLevel()) // 🔹 difficulté (float → String)
                    );

                    setView(gameView);
                },
                this::showMainMenu
        );

        setView(namesView);
    }

    // ========== MODE CUSTOM ==========

    public void startModeCustom() {
        System.out.println("Je suis dans : MODE CUSTOM (CONFIG NOUVELLE PARTIE)");

        CustomModeView view = new CustomModeView(
                config -> {
                    // 1) win condition globale
                    Game.setDefaultMaxNumberSymbolAlign(config.getWinCondition());

                    // 2) plateau selon la forme (rectangle / carré)
                    int rows = config.getRows();
                    int cols = config.getCols();

                    RectangleBoard board;
                    if ("SQUARE".equals(config.getShape())) {
                        int size = Math.min(rows, cols);
                        board = new RectangleBoard(size, size);
                    } else {
                        board = new RectangleBoard(rows, cols);
                    }

                    // 3) symboles
                    Symbol cross = new Symbol(
                            getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                            TypeOfSymbol.CROSS
                    );
                    Symbol circle = new Symbol(
                            getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                            TypeOfSymbol.CIRCLE
                    );

                    if (!config.isVsBot()) {
                        // ===== MODE CUSTOM PVP =====
                        Player p1 = new Player(config.getPlayer1Name(), 0, cross);
                        Player p2 = new Player(config.getPlayer2Name(), 0, circle);

                        GameController gameController = new GameController(p1, p2, board);
                        GameBoardView boardView = gameController.getGameBoardView();

                        GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                                boardView,
                                this::showMainMenu,
                                gameController,
                                GameMode.CUSTOM_PVP,           // 🔹 mode custom PVP
                                config.getWinCondition(),      // 🔹 win condition custom
                                null                           // 🔹 pas de bot
                        );

                        gameController.handleGame(this::showMainMenu);
                        setView(gameView);

                    } else {
                        // ===== MODE CUSTOM VS BOT =====
                        Player human = new Player(config.getPlayer1Name(), 0, cross);

                        String botName = config.getPlayer2Name();
                        if (botName == null || botName.isBlank()) botName = "BOT";

                        RectangleBoard finalBoard = board;
                        String finalBotName = botName;
                        int winCond = config.getWinCondition();

                        // 👉 On affiche l'écran de difficulté du bot
                        BotDifficultyView diffView = new BotDifficultyView(
                                difficultyKey -> {
                                    // 1) on change le niveau global de l'IA
                                    BotPlayer.changeLevel(difficultyKey);

                                    // 2) on crée le Bot avec CE niveau
                                    BotPlayer bot = new BotPlayer(
                                            finalBotName,
                                            0,
                                            BotPlayer.getCurrentDefaultLevel(),
                                            circle,
                                            winCond,
                                            finalBoard.useCase   // cases jouables pour l'IA
                                    );

                                    // 3) on lance la partie custom VS BOT
                                    GameController gameController =
                                            new GameController(human, bot, true, this::showMainMenu, finalBoard);

                                    GameBoardView boardView = gameController.getGameBoardView();

                                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                                            boardView,
                                            this::showMainMenu,
                                            gameController,
                                            GameMode.CUSTOM_PVBOT,                    // 🔹 mode custom VS BOT
                                            winCond,                                  // 🔹 win condition
                                            String.valueOf(BotPlayer.getCurrentDefaultLevel()) // 🔹 difficulté float → String
                                    );

                                    setView(gameView);
                                },
                                this::showCustomEntry   // Back → retour à l'écran New / Load
                        );

                        setView(diffView);
                    }
                },
                this::showCustomEntry   // Back depuis CustomModeView → retour New / Load
        );

        setView(view);
    }

    // ========== RÈGLES ==========

    public void showRules() {
        System.out.println("Je suis dans : ÉCRAN DES RÈGLES");

        RulesView view = new RulesView(this::showMainMenu);
        setView(view);
    }
}
