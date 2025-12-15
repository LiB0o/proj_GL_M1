package gl.morpion.view.menu;

import gl.morpion.audio.SoundFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Entry view for the Custom mode.
 * <p>
 * This screen provides three actions:
 * </p>
 * <ul>
 *     <li>Start a new custom game (opens the custom configuration flow)</li>
 *     <li>Load a saved custom game</li>
 *     <li>Go back to the main menu</li>
 * </ul>
 */
public class CustomView extends StackPane {

    /**
     * Creates the Custom mode entry screen.
     *
     * @param onNewGame callback executed when the user clicks "New custom game"
     * @param onLoad    callback executed when the user clicks "Load saved game"
     * @param onBack    callback executed when the user clicks "Back"
     */
    public CustomView(Runnable onNewGame, Runnable onLoad, Runnable onBack) {
        setPrefSize(1200, 800);

        // Background with the same style as the main menu
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // Title
        Label title = new Label("Custom mode");
        title.getStyleClass().add("title-glow");

        Label subMsg = new Label("Choose an option");
        subMsg.getStyleClass().add("form-label");

        // "New custom game" button
        Button newGameBtn = new Button("New custom game");
        newGameBtn.getStyleClass().add("big-button");
        newGameBtn.setOnAction(e -> {
            if (onNewGame != null) onNewGame.run();
        });

        // "Load saved game" button
        Button loadBtn = new Button("Load saved game");
        loadBtn.getStyleClass().add("big-button");
        loadBtn.setOnAction(e -> {
            if (onLoad != null) onLoad.run();
        });

        // "Back" button
        Button back = new Button("← Back");
        back.getStyleClass().add("pill-button");
        back.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });
        SoundFX.attachReturn(back);

        VBox box = new VBox(16, title, subMsg, newGameBtn, loadBtn, back);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(16));
        box.getStyleClass().add("form-card");

        getChildren().addAll(bg, box);
    }
}
