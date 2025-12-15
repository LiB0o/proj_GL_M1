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
 * Main menu controller for the application.
 * <p>
 * This controller manages navigation within a single JavaFX {@link Stage} and a single
 * root {@link StackPane}. It swaps views in the same scene to display:
 * </p>
 * <ul>
 *     <li>Main menu</li>
 *     <li>Game modes (PvP, PvBot, Custom)</li>
 *     <li>Custom mode entry (New / Load)</li>
 *     <li>Save list and saved game loading</li>
 *     <li>Rules and Settings screens</li>
 * </ul>
 *
 * <p>
 * It is responsible for wiring UI views (menu screens) to game creation logic
 * (creating players, boards, controllers, restoring saved data, etc.).
 * </p>
 */
public class MainMenuController {

    /** The main JavaFX stage used by the application. */
    private final Stage stage;

    /** Root container of the single-scene setup where views are swapped. */
    private final StackPane root;   // root container for the single Scene

    /** Application settings loaded from persistence (or defaults). */
    private final SettingsModel settings = SettingsModel.load();

    /** Default application width (may be overridden by settings). */
    private static final int WIDTH = 1200;

    /** Default application height (may be overridden by settings). */
    private static final int HEIGHT = 800;

    /** Current number of aligned symbols required to win (used by win-condition selection screens). */
    private int currentWinCondition = Game.getDefaultMaxNumberSymbolAlign();

    /**
     * Creates a menu controller.
     *
     * @param stage the main JavaFX stage
     * @param root  the root container of the single scene
     */
    public MainMenuController(Stage stage, StackPane root) {
        this.stage = stage;
        this.root = root;
    }

    /**
     * Replaces the content of the root container with the given view.
     *
     * @param view the new view node to display
     */
    private void setView(Node view) {
        root.getChildren().setAll(view);
    }

    // ========== MAIN MENU ==========

    /**
     * Displays the main menu screen.
     * <p>
     * This method creates a {@link MainMenuView} and swaps it into the current scene.
     * </p>
     */
    public void showMainMenu() {
        System.out.println("Je suis dans : MENU PRINCIPAL");

        MainMenuView menu = new MainMenuView(this);
        setView(menu);
    }

    /**
     * Handles a mode selection from the main menu.
     * <p>
     * Supported mode names include:
     * </p>
     * <ul>
     *     <li>{@code "QUIT"}: closes the application</li>
     *     <li>{@code "Custom"}: opens the custom entry screen (New / Load)</li>
     *     <li>Other values: displays a placeholder view</li>
     * </ul>
     *
     * @param modeName the selected mode name (e.g., "PvsP", "PvsBot", "Custom", "QUIT")
     */
    public void showMode(String modeName) {
        if ("QUIT".equals(modeName)) {
            Stage s = (Stage) stage.getScene().getWindow();
            s.close();
            return;
        }

        // When clicking the "Custom" button
        if ("Custom".equalsIgnoreCase(modeName)) {
            // First show the entry screen with "Play a new game / Load a saved game"
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

    /**
     * Toggles the application language.
     * <p>
     * This feature is currently a placeholder.
     * </p>
     *
     * @param code the language code
     */
    public void toggleLanguage(String code) {
        // to be implemented later
    }

    // ========== CUSTOM MODE ENTRY SCREEN (New / Load) ==========

    /**
     * Displays the Custom mode entry screen (New / Load / Back).
     * <p>
     * This screen lets the player choose to start a new custom game or load an existing one.
     * </p>
     */
    public void showCustomEntry() {
        System.out.println("Je suis dans : CUSTOM ENTRY (New / Load)");
        CustomView view = new CustomView(
                // New custom game: start the configuration flow
                this::startModeCustom,
                // Load saved game: open the save list menu
                this::showCustomLoadMenu,
                // Back: return to main menu
                this::showMainMenu
        );
        setView(view);
    }

    /**
     * Displays the save list menu for the Custom mode.
     * <p>
     * Uses {@link SaveManager#listSaves()} and displays a {@link SaveListView}.
     * If there are no saves, a fallback placeholder view is shown.
     * </p>
     */
    private void showCustomLoadMenu() {
        System.out.println("Je suis dans : MENU DES SAUVEGARDES (Custom)");

        List<SaveMetadata> saves = SaveManager.listSaves();
        if (saves == null || saves.isEmpty()) {
            // Fallback message if no saves exist
            ModePlaceholderView empty = new ModePlaceholderView(
                    "No saved games found.",
                    this::showCustomEntry
            );
            setView(empty);
            return;
        }

        SaveListView view = new SaveListView(
                saves,
                this::loadCustomSave,   // when clicking Play
                this::showCustomEntry   // Back button
        );
        setView(view);
    }

    /**
     * Loads a Custom saved game selected from {@link SaveListView}.
     * <p>
     * This method restores the game state from a persisted {@link GameData} instance:
     * </p>
     * <ol>
     *     <li>Loads game data from file</li>
     *     <li>Restores mode, board size, win condition, and bot difficulty</li>
     *     <li>Recreates the board and re-applies placed symbols</li>
     *     <li>Recreates players (human vs human or human vs bot)</li>
     *     <li>Restores current player and refreshes the view</li>
     * </ol>
     *
     * <p>
     * Side effects: updates {@link Game#setDefaultMaxNumberSymbolAlign(int)} and swaps the current view.
     * </p>
     *
     * @param metadata metadata describing the save to load
     */
    private void loadCustomSave(SaveMetadata metadata) {
        System.out.println("Loading save : " + metadata.getSaveName());

        // 1) Load the full GameData from disk
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

        // 2) Restore mode, board size, win condition, and bot difficulty
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

        // Update the global win condition
        Game.setDefaultMaxNumberSymbolAlign(winCondition);

        // 3) Recreate the board
        RectangleBoard board = new RectangleBoard(rows, cols);

        // 4) Create symbols (X / O)
        Symbol cross = new Symbol(
                getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                TypeOfSymbol.CROSS
        );
        Symbol circle = new Symbol(
                getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                TypeOfSymbol.CIRCLE
        );

        // 5) Restore placed symbols on the board from board[] data
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

        // 6) Recreate players
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
                    diff,                 // bot difficulty restored
                    circle,
                    winCondition,
                    board.useCase        // playable cells for AI
            );
        } else {
            p2 = new Player(p2Name, 0, circle);
        }

        // 7) Create the correct GameController depending on the mode
        GameController controller;
        if (vsBot && p2 instanceof BotPlayer) {
            controller = new GameController(p1, (BotPlayer) p2, true, this::showMainMenu, board);
        } else {
            controller = new GameController(p1, p2, board);
        }

        // 8) Restore the correct current player using currentPlayerName
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

        // 8bis) Resynchronize bot internal state with the loaded board
        if (vsBot && p2 instanceof BotPlayer) {
            BotPlayer bot = (BotPlayer) p2;
            Game g = controller.getGame();
            GameBoard gb = g.getGameBoard();

            bot.resyncBoardViewFromBoard(gb, p1.getSymbol());
        }

        // Synchronize the view with the loaded model
        controller.refreshViewFromModel();

        // 9) Create the game view with menu and restored save info
        GameBoardWithMenuView view = new GameBoardWithMenuView(
                controller.getGameBoardView(),
                this::showMainMenu,
                controller,
                mode,
                winCondition,
                data.getBotDifficulty()
        );

        // In BOT mode, handleGame does nothing (early return),
        // but if you want to be extra clean, you can condition it:
        if (!vsBot || !(p2 instanceof BotPlayer)) {
            controller.handleGame(this::showMainMenu);
        }

        setView(view);
    }

    // ========== PLAYER VS PLAYER MODE ==========

    /**
     * Starts the Player vs Player flow:
     * <ol>
     *     <li>Choose win condition</li>
     *     <li>Enter player names</li>
     *     <li>Create players and start the game</li>
     * </ol>
     *
     * <p>
     * Side effects: updates {@link #currentWinCondition} and swaps views in the root container.
     * </p>
     */
    public void startModePvp() {
        System.out.println("Je suis dans : ÉCRAN CHOIX NB SYMBOLES (PVP)");

        WinConditionView winView = new WinConditionView(
                currentWinCondition,
                value -> {
                    // Store the new win condition
                    currentWinCondition = value;

                    System.out.println("Je suis dans : MODE PLAYER VS PLAYER (NOMS)");

                    // Now show the player names screen
                    PlayerNamesView namesView = new PlayerNamesView(
                            (name1, name2) -> {
                                System.out.println("Je suis dans : MODE PLAYER VS PLAYER (JEU)");

                                // Players with symbols
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

                                // Standard PvP game controller (default board)
                                GameController gameController = new GameController(p1, p2);

                                GameBoardView boardView = gameController.getGameBoardView();

                                GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                                        boardView,
                                        this::showMainMenu,
                                        gameController,
                                        GameMode.PVP,          // mode
                                        currentWinCondition,   // chosen win condition
                                        null                   // no bot
                                );

                                // End callback: return to menu
                                gameController.handleGame(this::showMainMenu);

                                // Display the game view in the same scene
                                setView(gameView);
                            },
                            this::showMainMenu   // Back button from PlayerNamesView
                    );

                    // Display PlayerNamesView in the same scene
                    setView(namesView);
                },
                this::showMainMenu   // Back button from WinConditionView
        );

        // Display WinConditionView
        setView(winView);
    }

    // ========== PLAYER VS BOT MODE: choose win condition & difficulty ==========

    /**
     * Starts the "Player vs Bot" flow by letting the user choose:
     * <ol>
     *     <li>Win condition</li>
     *     <li>Bot difficulty</li>
     * </ol>
     *
     * <p>
     * Once selected, it launches {@link #startModePvsBot(int)} with the chosen win condition.
     * </p>
     */
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

    // ========== PLAYER VS BOT MODE: name + launch ==========

    /**
     * Starts a Player vs Bot game with the given win condition.
     * <p>
     * The user is prompted for the human player name, then a {@link BotPlayer} is created
     * with the currently selected default difficulty level.
     * </p>
     *
     * @param winCondition the number of aligned symbols required to win
     */
    public void startModePvsBot(int winCondition) {
        System.out.println("Je suis dans : MODE PLAYER VS BOT (winCondition = " + winCondition + ")");

        PlayerNamesView namesView = new PlayerNamesView(
                true,   // vsBot: only one input field for the human player
                (name1, ignored) -> {
                    // Human player (X)
                    Player human = new Player(
                            name1,
                            0,
                            new Symbol(
                                    getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                                    TypeOfSymbol.CROSS
                            )
                    );

                    // Bot symbol (O)
                    Symbol botSymbol = new Symbol(
                            getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                            TypeOfSymbol.CIRCLE
                    );

                    // Bot player
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

                    // VS BOT game controller
                    GameController gameController =
                            new GameController(human, bot, true, this::showMainMenu);

                    // Construct the game view with menu (as in PvP)
                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu,
                            gameController,
                            GameMode.PVBOT,                          // mode
                            winCondition,                            // win condition
                            String.valueOf(BotPlayer.getCurrentDefaultLevel()) // difficulty (float -> String)
                    );

                    setView(gameView);
                },
                this::showMainMenu
        );

        setView(namesView);
    }

    // ========== CUSTOM MODE ==========

    /**
     * Starts the Custom mode configuration flow.
     * <p>
     * This method displays {@link CustomModeView} to collect user configuration, then:
     * </p>
     * <ul>
     *     <li>Sets the global win condition</li>
     *     <li>Creates a board (square or rectangle)</li>
     *     <li>Creates players (PvP or PvBot)</li>
     *     <li>Creates the corresponding {@link GameController} and view</li>
     * </ul>
     *
     * <p>
     * Side effects: updates {@link Game#setDefaultMaxNumberSymbolAlign(int)} and swaps views.
     * </p>
     */
    public void startModeCustom() {
        System.out.println("Je suis dans : MODE CUSTOM (CONFIG NOUVELLE PARTIE)");

        CustomModeView view = new CustomModeView(
                config -> {
                    // 1) Global win condition
                    Game.setDefaultMaxNumberSymbolAlign(config.getWinCondition());

                    // 2) Board depending on the shape (rectangle / square)
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

                    // 3) Symbols
                    Symbol cross = new Symbol(
                            getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                            TypeOfSymbol.CROSS
                    );
                    Symbol circle = new Symbol(
                            getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                            TypeOfSymbol.CIRCLE
                    );

                    if (!config.isVsBot()) {
                        // ===== CUSTOM PvP MODE =====
                        Player p1 = new Player(config.getPlayer1Name(), 0, cross);
                        Player p2 = new Player(config.getPlayer2Name(), 0, circle);

                        GameController gameController = new GameController(p1, p2, board);
                        GameBoardView boardView = gameController.getGameBoardView();

                        GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                                boardView,
                                this::showMainMenu,
                                gameController,
                                GameMode.CUSTOM_PVP,           // custom PvP mode
                                config.getWinCondition(),      // custom win condition
                                null                           // no bot
                        );

                        gameController.handleGame(this::showMainMenu);
                        setView(gameView);

                    } else {
                        // ===== CUSTOM VS BOT MODE =====
                        Player human = new Player(config.getPlayer1Name(), 0, cross);

                        String botName = config.getPlayer2Name();
                        if (botName == null || botName.isBlank()) botName = "BOT";

                        RectangleBoard finalBoard = board;
                        String finalBotName = botName;
                        int winCond = config.getWinCondition();

                        // Display bot difficulty selection screen
                        BotDifficultyView diffView = new BotDifficultyView(
                                difficultyKey -> {
                                    // 1) Update global AI level
                                    BotPlayer.changeLevel(difficultyKey);

                                    // 2) Create the bot with this level
                                    BotPlayer bot = new BotPlayer(
                                            finalBotName,
                                            0,
                                            BotPlayer.getCurrentDefaultLevel(),
                                            circle,
                                            winCond,
                                            finalBoard.useCase   // playable cells for AI
                                    );

                                    // 3) Start the custom VS BOT game
                                    GameController gameController =
                                            new GameController(human, bot, true, this::showMainMenu, finalBoard);

                                    GameBoardView boardView = gameController.getGameBoardView();

                                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                                            boardView,
                                            this::showMainMenu,
                                            gameController,
                                            GameMode.CUSTOM_PVBOT,                    // custom VS BOT mode
                                            winCond,                                  // win condition
                                            String.valueOf(BotPlayer.getCurrentDefaultLevel()) // difficulty float -> String
                                    );

                                    setView(gameView);
                                },
                                this::showCustomEntry   // Back: return to New / Load entry screen
                        );

                        setView(diffView);
                    }
                },
                this::showCustomEntry   // Back from CustomModeView -> return to New / Load
        );

        setView(view);
    }

    // Helper: converts a code ("X", "O", "CROSS", "CIRCLE") into a Symbol with the matching image
    /**
     * Creates a {@link Symbol} instance based on a textual code.
     * <p>
     * Supported values (case-insensitive) include:
     * </p>
     * <ul>
     *     <li>{@code "X"} or {@code "CROSS"} for the cross symbol</li>
     *     <li>{@code "O"} or {@code "CIRCLE"} for the circle symbol</li>
     * </ul>
     *
     * @param code the symbol code to convert
     * @return the created {@link Symbol}, or {@code null} if the code is null or unsupported
     */
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

    // ========== RULES ==========

    /**
     * Opens the Settings view.
     */
    public void openSettings() {
        // (Optional) small click sound if desired
        // AudioManager.playClick();

        SettingsView view = new SettingsView(this, settings);
        setView(view);
    }

    /**
     * Displays the rules screen.
     */
    public void showRules() {
        System.out.println("Je suis dans : ÉCRAN DES RÈGLES");

        RulesView view = new RulesView(this::showMainMenu);
        setView(view);
    }

    /**
     * Applies a resolution to the main stage using the format {@code "<width>x<height>"}.
     * <p>
     * Example: {@code "1200x800"}.
     * </p>
     *
     * @param resolution resolution string formatted as "WIDTHxHEIGHT"
     */
    public void applyResolution(String resolution) {
        try {
            String[] d = resolution.split("x");
            double w = Double.parseDouble(d[0]);
            double h = Double.parseDouble(d[1]);

            // IMPORTANT: the "stage" must exist in this controller
            stage.setWidth(w);
            stage.setHeight(h);

        } catch (Exception e) {
            System.err.println("Bad resolution: " + resolution);
        }
    }

    /**
     * Enables or disables fullscreen mode on the main stage.
     *
     * @param fullscreen {@code true} to enable fullscreen, {@code false} to disable it
     */
    public void applyFullscreen(boolean fullscreen) {
        stage.setFullScreen(fullscreen);
    }
}
