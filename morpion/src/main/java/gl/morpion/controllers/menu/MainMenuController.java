package gl.morpion.controllers.menu;

import gl.morpion.controllers.GameController;
import gl.morpion.model.*;
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
import gl.morpion.persistence.*;
import gl.morpion.audio.AudioManager;
import gl.morpion.settings.SettingsModel;
import gl.morpion.view.menu.SettingsView;



import java.util.List;

/**
 * <h1>controller MainMenuController</h1>
 * <h2>Elements of MainMenuController</h2>
 */
public class MainMenuController {

    private final Stage stage;
    private final StackPane root;   // 🔹 conteneur racine de la Scene unique
    private final SettingsModel settings = SettingsModel.load();

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

    /**
     * <h3>showMainMenu</h3>
     * Show the window of the game
     */
    public void showMainMenu() {
        System.out.println("Je suis dans : MENU PRINCIPAL");

        MainMenuView menu = new MainMenuView(this);
        setView(menu);
    }

    /**
     * <h3>showMode</h3>
     * Permit to go to the selected mode or to quit
     * @param modeName name of the name (PvsP, PvsBot)
     */
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
        System.out.println("Loading save : " + metadata.getSaveName());

        // 1) Charger GameData complet à partir du fichier
        GameData data = SaveManager.loadGameData(metadata.getFileName());
        if (data == null) {
            System.err.println("Impossible de charger GameData pour " + metadata.getFileName());
            ModePlaceholderView errorView = new ModePlaceholderView(
                    "Error while loading save.",
                    this::showCustomLoadMenu
            );
            setView(errorView);
            return;
        }

        // 2) Récupérer mode, taille, winCondition, difficulté du bot
        GameMode mode = null;
        try {
            if (data.getMode() != null) {
                mode = GameMode.valueOf(data.getMode());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Unknown mode in save: " + data.getMode());
        }
        if (mode == null) {
            mode = GameMode.PVP; // fallback
        }

        int rows = (data.getRows() != null) ? data.getRows() : RectangleBoard.DEFAULT_ROW;
        int cols = (data.getCols() != null) ? data.getCols() : RectangleBoard.DEFAULT_COLUMN;
        int winCondition = (data.getWinCondition() != null) ? data.getWinCondition()
                : Game.getDefaultMaxNumberSymbolAlign();

        // On met à jour la win condition globale
        Game.setDefaultMaxNumberSymbolAlign(winCondition);

        // 3) Recréer le plateau
        RectangleBoard board = new RectangleBoard(rows, cols);

        // 4) Créer les Symbol (X / O)
        Symbol cross = new Symbol(
                getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                TypeOfSymbol.CROSS
        );
        Symbol circle = new Symbol(
                getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                TypeOfSymbol.CIRCLE
        );

        // 5) Replacer les symboles sur le plateau à partir de board[]
        if (data.getBoard() != null) {
            for (CellData cell : data.getBoard()) {
                String sym = cell.getSymbol();
                Symbol s = null;

                if ("croix.jpg".equals(sym)) s = cross;
                if ("cercle.png".equals(sym)) s = circle;
                if (s == null) continue;

                int r = cell.getRow();
                int c = cell.getCol();
                if (r < 0 || r >= rows || c < 0 || c >= cols) continue;

                board.setSymbolAt(r, c, s);
            }
        }

        // 6) Recréer les joueurs
        String p1Name = (data.getPlayer1Name() != null && !data.getPlayer1Name().isBlank())
                ? data.getPlayer1Name()
                : "Player 1";

        String p2Name = (data.getPlayer2Name() != null && !data.getPlayer2Name().isBlank())
                ? data.getPlayer2Name()
                : "Player 2";

        Player p1 = new Player(p1Name, 0, cross);
        Player p2;

        boolean vsBot = (mode == GameMode.PVBOT || mode == GameMode.CUSTOM_PVBOT);
        if (vsBot) {
            float diff = 1.0f;
            if (data.getBotDifficulty() != null) {
                try {
                    diff = Float.parseFloat(data.getBotDifficulty());
                    System.out.println("Bot difficulty---------------------: " + diff);
                } catch (Exception ignored) {
                    System.err.println("Invalid botDifficulty: " + data.getBotDifficulty());
                }
            }

            p2 = new BotPlayer(
                    p2Name,
                    0,
                    diff,                 // 🔥 difficulté du bot restaurée
                    circle,
                    winCondition,
                    board.useCase        // cases jouables pour l'IA
            );
        } else {
            p2 = new Player(p2Name, 0, circle);
        }

        // 7) Créer le GameController correct selon le mode (vsBot ou non)
        GameController controller;
        if (vsBot && p2 instanceof BotPlayer) {
            controller = new GameController(p1, (BotPlayer) p2, true, this::showMainMenu, board);
        } else {
            controller = new GameController(p1, p2, board);
        }

        // 8) Remettre le bon joueur courant grâce à currentPlayerName
        try {
            Game g = controller.getGame();
            if (g != null && data.getCurrentPlayerName() != null) {
                String cur = data.getCurrentPlayerName();

                if (cur.equals(p1.getName())) {
                    g.setCurrentPlayer(p1);
                } else if (cur.equals(p2.getName())) {
                    g.setCurrentPlayer(p2);
                }
            }
        } catch (Exception e) {
            System.err.println("Could not restore current player: " + e.getMessage());
        }

        // 8bis) 🔁 Resynchroniser le cerveau du bot avec le plateau chargé
        if (vsBot && p2 instanceof BotPlayer) {
            BotPlayer bot = (BotPlayer) p2;
            Game g = controller.getGame();
            GameBoard gb = g.getGameBoard();

            bot.resyncBoardViewFromBoard(gb, p1.getSymbol());
        }

        // 🔄 Synchroniser la vue avec le plateau chargé
        controller.refreshViewFromModel();

        // 9) Créer la vue de jeu avec menu + infos de sauvegarde
        GameBoardWithMenuView view = new GameBoardWithMenuView(
                controller.getGameBoardView(),
                this::showMainMenu,
                controller,
                mode,
                winCondition,
                data.getBotDifficulty()
        );

        // ⚠️ En mode BOT, handleGame ne fait rien (early return),
        // mais si tu veux être ultra propre, tu peux conditionner :
        if (!vsBot || !(p2 instanceof BotPlayer)) {
            controller.handleGame(this::showMainMenu);
        }

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

                    // 4. Construct view with menu like in PvP
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
                    // If you want to manage end with GameController
                    // gameController.handleGame(this::showMainMenu);

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
    // 🔹 Helper : transforme un code ("X", "O", "CROSS", "CIRCLE") en Symbol avec la bonne image
    private Symbol createSymbolFromCode(String code) {
        if (code == null) return null;

        String upper = code.trim().toUpperCase();

        if ("X".equals(upper) || "CROSS".equals(upper)) {
            return new Symbol(
                    getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                    TypeOfSymbol.CROSS
            );
        }

        if ("O".equals(upper) || "CIRCLE".equals(upper)) {
            return new Symbol(
                    getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                    TypeOfSymbol.CIRCLE
            );
        }

        return null;
    }


    // ========== RÈGLES ==========


    public void openSettings() {
        // (optionnel) petit son de clic si tu veux
        // AudioManager.playClick();

        SettingsView view = new SettingsView(this, settings);
        setView(view);
    }

    /**
     * <h3>showRules</h3>
     * Show the rules in the main menu
     */
    public void showRules() {
        System.out.println("Je suis dans : ÉCRAN DES RÈGLES");

        RulesView view = new RulesView(this::showMainMenu);
        setView(view);
    }
    public void applyResolution(String resolution) {
        try {
            String[] d = resolution.split("x");
            double w = Double.parseDouble(d[0]);
            double h = Double.parseDouble(d[1]);

            // IMPORTANT : il faut que "stage" existe dans ton controller
            stage.setWidth(w);
            stage.setHeight(h);

        } catch (Exception e) {
            System.err.println("Bad resolution: " + resolution);
        }
    }
    public void applyFullscreen(boolean fullscreen) {
        stage.setFullScreen(fullscreen);
    }


}
