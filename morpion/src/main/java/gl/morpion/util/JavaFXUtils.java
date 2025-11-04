package gl.morpion.util;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public final class JavaFXUtils {
    private JavaFXUtils() {}

    public static void setupKeyboardNav(Pane parent, Button... order) {
        if (order == null || order.length == 0) return;
        order[0].requestFocus();

        parent.setOnKeyPressed(e -> {
            int idx = -1;
            for (int i = 0; i < order.length; i++) if (order[i].isFocused()) { idx = i; break; }
            if (idx == -1) { order[0].requestFocus(); return; }

            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.TAB) {
                order[(idx + 1) % order.length].requestFocus(); e.consume();
            } else if (e.getCode() == KeyCode.UP) {
                order[(idx - 1 + order.length) % order.length].requestFocus(); e.consume();
            } else if (e.getCode() == KeyCode.ENTER) {
                order[idx].fire(); e.consume();
            }
        });
    }
}
