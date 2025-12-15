package gl.morpion.view.menu;

import gl.morpion.audio.SoundFX;
import gl.morpion.controllers.GameController;
import gl.morpion.model.Game;
import gl.morpion.model.GameMode;
import gl.morpion.persistence.SaveManager;
import gl.morpion.view.GameBoardView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Custom JavaFX view that wraps a game board with a styled menu interface.
 * <p>
 * Provides a back button and decorative styling around the game board.
 * Extends {@link StackPane} to layer background, game board, and UI elements.
 * </p>
 */
public class GameBoardWithMenuView extends StackPane {

    // ====== NEW CHAMPS ======
    private final GameBoardView boardView;
    private final GameController gameController;
    private final GameMode gameMode;
    private final int winCondition;
    private final String botDifficulty;

    /**
     * Default constructor for compatibility.
     * Assumes a Player vs Player (PVP) mode with a win condition of 3.
     *
     * @param gameBoardView the view of the game board
     * @param onBack        the callback for the "Back" button
     */
    public GameBoardWithMenuView(Node gameBoardView, Runnable onBack) {
        this(gameBoardView, onBack, null, GameMode.PVP, 3, null);
    }

    /**
     * Constructor for compatibility with GameController.
     * Assumes Player vs Player (PVP) mode with a win condition of 3.
     *
     * @param gameBoardView the view of the game board
     * @param onBack        the callback for the "Back" button
     * @param gameController the game controller instance
     */
    public GameBoardWithMenuView(Node gameBoardView,
                                 Runnable onBack,
                                 GameController gameController) {
        this(gameBoardView, onBack, gameController, GameMode.PVP, 3, null);
    }

    /**
     * Full constructor:
     * - gameBoardView: the game board view
     * - onBack        : callback for the back button
     * - gameController: to access the game (for saving)
     * - gameMode      : game mode (PVP / PVBOT / CUSTOM_PVP / CUSTOM_PVBOT)
     * - winCondition  : the number of symbols required to align
     * - botDifficulty : the bot difficulty (or null if no bot)
     *
     * @param gameBoardView  the view of the game board
     * @param onBack         the callback for the "Back" button
     * @param gameController the game controller instance
     * @param gameMode       the game mode
     * @param winCondition   the win condition (number of symbols required to win)
     * @param botDifficulty  the bot difficulty (or null if not applicable)
     */
    public GameBoardWithMenuView(Node gameBoardView,
                                 Runnable onBack,
                                 GameController gameController,
                                 GameMode gameMode,
                                 int winCondition,
                                 String botDifficulty) {
        this.boardView = (GameBoardView) gameBoardView;
        this.gameController = gameController;
        this.gameMode = gameMode;
        this.winCondition = winCondition;
        this.botDifficulty = botDifficulty;

        // Load the external CSS stylesheet for menu styling
        var css = getClass().getResource("/css/menu.css");
        if (css != null) getStylesheets().add(css.toExternalForm());

        // Background
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // "Back" button
        Button backButton = new Button("← Back");
        backButton.getStyleClass().add("pill-button");
        backButton.setOnAction(e -> {
            if (onBack != null) {
                // Always prompt for a save before going back
                this.showAskSavePopup(onBack);
            }
        });
        SoundFX.attachReturn(backButton);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, backButton, spacer);
        topBar.setPadding(new Insets(16, 16, 0, 16));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Game board container
        StackPane gameContainer = new StackPane(gameBoardView);
        gameContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        gameContainer.setAlignment(Pos.CENTER);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setRadius(20);
        shadow.setOffsetY(5);
        gameContainer.setEffect(shadow);

        gameContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(30, 40, 50, 0.95), rgba(20, 30, 40, 0.95));" +
                        "-fx-border-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 25;"
        );

        StackPane centerWrapper = new StackPane(gameContainer);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(80, 40, 40, 40));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(centerWrapper);

        getChildren().addAll(bg, layout);
    }

    /**
     * Prompts the user with a confirmation dialog to save the game before exiting.
     * Uses {@link SaveManager} to write the full save data.
     *
     * @param onBack the callback to return to the previous screen if no save is made
     */
    public void showAskSavePopup(Runnable onBack) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Saving an unfinished game");
            alert.setHeaderText("CONFIRMATION");
            alert.setContentText("Would you like to save it?");
            alert.getButtonTypes().clear();

            ButtonType buttonTypeYes = new ButtonType("YES", ButtonType.YES.getButtonData());
            ButtonType buttonTypeNo = new ButtonType("NO", ButtonType.NO.getButtonData());
            alert.getButtonTypes().addAll(buttonTypeYes, buttonTypeNo);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/alert-style.css").toExternalForm()
            );

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    // Save the game
                    if (gameController != null && gameController.getGame() != null) {
                        Game game = gameController.getGame();

                        // Ask for the save name
                        String defaultName;
                        try {
                            String p1 = game.getPlayers().get(0).getName();
                            String p2 = game.getPlayers().get(1).getName();
                            defaultName = p1 + "_vs_" + p2;
                        } catch (Exception ex) {
                            defaultName = "My_game";
                        }

                        TextInputDialog nameDialog = new TextInputDialog(defaultName);
                        nameDialog.setTitle("Save game");
                        nameDialog.setHeaderText("Choose a name for this save");
                        nameDialog.setContentText("Save name:");

                        nameDialog.showAndWait().ifPresent(saveName -> {
                            if (saveName != null && !saveName.isBlank()) {
                                // Save using SaveManager
                                SaveManager.saveGame(
                                        game,
                                        boardView,
                                        saveName.trim(),
                                        gameMode,
                                        botDifficulty,
                                        winCondition
                                );
                                System.out.println("Game saved as: " + saveName);
                            }
                            // Return to the menu regardless of save name
                            onBack.run();
                        });

                    } else {
                        // Fallback behavior if GameController is not available
                        this.boardView.save();
                        onBack.run();
                    }

                } else if (response == buttonTypeNo) {
                    // No save, return directly
                    onBack.run();
                    System.out.println("Save cancelled");
                }
            });
        });
    }
}
