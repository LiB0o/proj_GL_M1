package gl.morpion.vue.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ModePlaceholderView extends StackPane {

    public ModePlaceholderView(String message, Runnable onBack) {
        setPrefSize(1200, 800);

        // Fond
        Region bg = new Region();
        bg.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0f14, #14202a);");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // Contenu centré
        Label msg = new Label(message);
        msg.setTextFill(Color.WHITE);
        msg.setFont(Font.font("Montserrat", FontWeight.BOLD, 36));

        Button back = new Button("← Retour");
        back.setStyle(
                "-fx-background-radius:12; -fx-background-color: rgba(255,255,255,0.10);" +
                        "-fx-text-fill: white; -fx-padding: 8 18 8 18; -fx-border-color: rgba(255,255,255,0.35); -fx-border-width:1.2; -fx-border-radius:12;"
        );
        back.setOnAction(e -> { if (onBack != null) onBack.run(); });

        VBox box = new VBox(16, msg, back);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(16));

        getChildren().addAll(bg, box);
    }
}
