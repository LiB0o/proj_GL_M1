package gl.morpion.audio;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class SoundFX {

    public static void attachReturn(Button b) {
        EventHandler<ActionEvent> old = b.getOnAction();
        b.setOnAction(e -> {
            AudioManager.playReturn();
            if (old != null) old.handle(e);
        });
    }

    public static void attachClick(Button b) {
        EventHandler<ActionEvent> old = b.getOnAction();
        b.setOnAction(e -> {
            AudioManager.playClick();
            if (old != null) old.handle(e);
        });
    }

    public static void attachQuit(Button b) {
        EventHandler<ActionEvent> old = b.getOnAction();
        b.setOnAction(e -> {
            AudioManager.playQuit();
            if (old != null) old.handle(e);
        });
    }
}
