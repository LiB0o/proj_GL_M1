package gl.morpion.audio;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Utility class used to attach sound effects to JavaFX {@link Button} actions.
 * <p>
 * Each method wraps the button's current {@code onAction} handler and ensures a sound effect
 * is played before executing the original handler.
 * </p>
 *
 * <p>
 * Side effects:
 * </p>
 * <ul>
 *     <li>Replaces the button's {@code onAction} handler with a new wrapped handler</li>
 *     <li>Plays an SFX via {@link AudioManager} when the button is clicked</li>
 * </ul>
 */
public class SoundFX {

    /**
     * Attaches the "return/back" sound effect to a button.
     * <p>
     * The sound is played first, then the existing handler (if any) is executed.
     * </p>
     *
     * @param b the button to decorate (must not be {@code null})
     */
    public static void attachReturn(Button b) {
        EventHandler<ActionEvent> old = b.getOnAction();
        b.setOnAction(e -> {
            AudioManager.playReturn();
            if (old != null) old.handle(e);
        });
    }

    /**
     * Attaches the generic click sound effect to a button.
     * <p>
     * The sound is played first, then the existing handler (if any) is executed.
     * </p>
     *
     * @param b the button to decorate (must not be {@code null})
     */
    public static void attachClick(Button b) {
        EventHandler<ActionEvent> old = b.getOnAction();
        b.setOnAction(e -> {
            AudioManager.playClick();
            if (old != null) old.handle(e);
        });
    }

    /**
     * Attaches the quit sound effect to a button.
     * <p>
     * The sound is played first, then the existing handler (if any) is executed.
     * </p>
     *
     * @param b the button to decorate (must not be {@code null})
     */
    public static void attachQuit(Button b) {
        EventHandler<ActionEvent> old = b.getOnAction();
        b.setOnAction(e -> {
            AudioManager.playQuit();
            if (old != null) old.handle(e);
        });
    }
}
