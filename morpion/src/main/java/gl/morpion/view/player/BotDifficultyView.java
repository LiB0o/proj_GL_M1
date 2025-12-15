package gl.morpion.view.player;

import gl.morpion.audio.SoundFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * View allowing the user to choose the bot difficulty level.
 * <p>
 * This screen is used before starting a Player vs Bot game (or Custom vs Bot),
 * and returns a difficulty key through a callback.
 * </p>
 * <p>
 * The expected difficulty keys are: {@code "EASY"}, {@code "NORMAL"}, {@code "HARD"}.
 * </p>
 */
public class BotDifficultyView extends BorderPane {

    /**
     * Creates the bot difficulty selection screen.
     *
     * @param onSelect callback invoked with {@code "EASY"}, {@code "NORMAL"} or {@code "HARD"}
     * @param onBack   callback invoked to go back to the previous menu
     */
    public BotDifficultyView(Consumer<String> onSelect, Runnable onBack) {
        // Same background as the main menu
        getStyleClass().add("main-menu-bg");

        // Title
        Label title = new Label("Choose Bot Difficulty");
        title.getStyleClass().add("title-glow");

        // Buttons (same style as main menu big buttons)
        Button easyBtn = new Button("Easy");
        easyBtn.getStyleClass().add("big-button");
        easyBtn.setOnAction(e -> onSelect.accept("EASY"));

        Button normalBtn = new Button("Normal");
        normalBtn.getStyleClass().add("big-button");
        normalBtn.setOnAction(e -> onSelect.accept("NORMAL"));

        Button hardBtn = new Button("Hard");
        hardBtn.getStyleClass().add("big-button");
        hardBtn.setOnAction(e -> onSelect.accept("HARD"));

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> onBack.run());
        SoundFX.attachReturn(backBtn);

        VBox box = new VBox(14, title, easyBtn, normalBtn, hardBtn, backBtn);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.getStyleClass().add("form-card");

        setCenter(box);
        setPadding(new Insets(24));
    }
}
