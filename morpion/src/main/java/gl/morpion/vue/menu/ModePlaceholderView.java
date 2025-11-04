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

/**
 * Placeholder view for game modes that are not yet implemented.
 * Displays a message and a back button to return to the previous screen.
 * Extends StackPane to layer background and centered content.
 */
public class ModePlaceholderView extends StackPane {

    /**
     * Constructor: Creates a placeholder view with a custom message and back button.
     * 
     * @param message The message to display in the center of the view
     * @param onBack Callback function executed when the back button is clicked
     */
    public ModePlaceholderView(String message, Runnable onBack) {
        // Set preferred size for the placeholder view (1200x800 pixels)
        setPrefSize(1200, 800);

        // Fond
        // Create background region with gradient styling
        Region bg = new Region();
        bg.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0f14, #14202a);");
        // Bind background dimensions to parent container size (responsive)
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // Contenu centré
        // Create message label with white text and bold font
        Label msg = new Label(message);
        msg.setTextFill(Color.WHITE);
        msg.setFont(Font.font("Montserrat", FontWeight.BOLD, 36));

        // Create back button with arrow icon and custom inline styling
        Button back = new Button("← Retour");
        back.setStyle(
                "-fx-background-radius:12; -fx-background-color: rgba(255,255,255,0.10);" +
                        "-fx-text-fill: white; -fx-padding: 8 18 8 18; -fx-border-color: rgba(255,255,255,0.35); -fx-border-width:1.2; -fx-border-radius:12;"
        );
        // Attach click handler that executes the onBack callback
        back.setOnAction(e -> { if (onBack != null) onBack.run(); });

        // Create vertical box to center message and button with 16px spacing
        VBox box = new VBox(16, msg, back);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(16));

        // Add background and content box to the scene graph (layered)
        getChildren().addAll(bg, box);
    }
}
