package gl.morpion.view.player;

import gl.morpion.audio.SoundFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.IntConsumer;

/**
 * View that allows the user to choose the number of symbols to align in order to win.
 * <p>
 * The user types an integer value using the keyboard. The value is validated to be within
 * the allowed range [{@link #MIN}, {@link #MAX}]. If valid, a callback is invoked.
 * </p>
 */
public class WinConditionView extends BorderPane {

    /** Minimum allowed win condition. */
    private static final int MIN = 3;

    /** Maximum allowed win condition. */
    private static final int MAX = 8;

    private final TextField numberField = new TextField();
    private final Label errorLabel = new Label();

    /**
     * Creates the win condition selection view.
     *
     * @param currentValue the current value to pre-fill in the input field (e.g., 5)
     * @param onValidate   callback invoked with the validated value
     * @param onBack       callback executed when the user clicks "Back"
     */
    public WinConditionView(int currentValue,
                            IntConsumer onValidate,
                            Runnable onBack) {

        // Same background as the main menu
        getStyleClass().add("main-menu-bg");

        // --------- Title ----------
        Label title = new Label("Win condition");
        title.getStyleClass().add("title-glow");

        // --------- Explanation text ----------
        Label info = new Label(
                "How many symbols in a row to win ? (" + MIN + " - " + MAX + ")"
        );
        info.getStyleClass().add("form-label");

        // --------- Input field ----------
        numberField.setText(String.valueOf(currentValue));
        numberField.setPromptText(MIN + " - " + MAX);
        numberField.getStyleClass().add("text-input");
        numberField.setMaxWidth(120);

        // Pressing Enter validates the value
        numberField.setOnAction(e -> validateAndSend(onValidate));

        // --------- Error label ----------
        errorLabel.setText(""); // empty by default
        // If you want to style it, you can add a CSS class:
        // errorLabel.getStyleClass().add("error-label");

        // --------- Buttons ----------
        Button validateBtn = new Button("Validate");
        validateBtn.getStyleClass().add("big-button");
        validateBtn.setOnAction(e -> validateAndSend(onValidate));

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });
        SoundFX.attachReturn(backBtn);

        // --------- Main layout ----------
        VBox content = new VBox(12,
                title,
                info,
                numberField,
                errorLabel,
                validateBtn,
                backBtn
        );
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(32));
        content.getStyleClass().add("form-card");

        setCenter(content);
        setPadding(new Insets(24));
    }

    /**
     * Reads the typed value, validates that it is between {@link #MIN} and {@link #MAX},
     * then calls the callback if everything is valid.
     * <p>
     * Side effects:
     * </p>
     * <ul>
     *     <li>Updates {@link #errorLabel} to display validation feedback</li>
     *     <li>Invokes {@code onValidate.accept(value)} when valid</li>
     * </ul>
     *
     * @param onValidate callback invoked with the validated integer (may be {@code null})
     */
    private void validateAndSend(IntConsumer onValidate) {
        if (onValidate == null) return;

        String txt = numberField.getText();
        if (txt == null || txt.trim().isEmpty()) {
            errorLabel.setText("Please type a number between " + MIN + " and " + MAX + ".");
            return;
        }

        try {
            int value = Integer.parseInt(txt.trim());
            if (value < MIN || value > MAX) {
                errorLabel.setText("Value must be between " + MIN + " and " + MAX + ".");
                return;
            }

            // OK → clear error and send value
            errorLabel.setText("");
            onValidate.accept(value);

        } catch (NumberFormatException ex) {
            errorLabel.setText("Please type a valid integer.");
        }
    }
}
