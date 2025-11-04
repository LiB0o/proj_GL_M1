
package gl.morpion.vue.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Custom JavaFX view that wraps a game board with a styled menu interface.
 * Provides a back button and decorative styling around the game board.
 * Extends StackPane to layer background, game board, and UI elements.
 */
public class GameBoardWithMenuView extends StackPane {

    /**
     * Constructor: Creates a styled game board view with menu controls.
     * 
     * @param gameBoardView The game board visual component to display
     * @param onBack Callback function executed when the back button is clicked
     */
    public GameBoardWithMenuView(Node gameBoardView, Runnable onBack) {
        // Set preferred size for the entire view (1200x800 pixels)
        setPrefSize(1200, 800);

        // Charger le CSS
        // Load the external CSS stylesheet for menu styling
        var css = getClass().getResource("/css/menu.css");
        if (css != null) getStylesheets().add(css.toExternalForm());

        // Fond avec le même style que le menu
        // Create background region with the same style as main menu
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        // Bind background size to parent container dimensions
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // Bouton "Retour" stylisé
        // Create styled "Back" button with arrow icon
        Button backButton = new Button("← Retour");
        backButton.getStyleClass().add("pill-button");
        // Attach click handler that executes the onBack callback
        backButton.setOnAction(e -> { if (onBack != null) onBack.run(); });

        // Create spacer region to push button to the left
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Create top bar with back button aligned to the left
        HBox topBar = new HBox(10, backButton, spacer);
        topBar.setPadding(new Insets(16, 16, 0, 16));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // 🎨 CONTAINER STYLISÉ pour le plateau de jeu
        // Create styled container for the game board
        StackPane gameContainer = new StackPane(gameBoardView);
        gameContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Respecte la taille du plateau
        // Center the game board within the container
        gameContainer.setAlignment(Pos.CENTER);

        // Effet d'ombre portée (optionnel)
        // Apply drop shadow effect to the game container for depth
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5)); // Semi-transparent black shadow
        shadow.setRadius(20); // Shadow blur radius
        shadow.setOffsetY(5); // Vertical shadow offset
        gameContainer.setEffect(shadow);

        // Style du container (bordure arrondie, fond semi-transparent)
        // Apply inline CSS styling: gradient background, rounded borders, semi-transparent
        gameContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(30, 40, 50, 0.95), rgba(20, 30, 40, 0.95));" +
                        "-fx-border-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 25;"
        );

        // 🎯 Centre parfaitement le container dans la scène
        // Wrap the game container in another StackPane to perfectly center it
        StackPane centerWrapper = new StackPane(gameContainer);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(80, 40, 40, 40)); // Espace autour (top, right, bottom, left spacing)

        // Create main layout using BorderPane (top bar + centered game board)
        BorderPane layout = new BorderPane();
        layout.setTop(topBar); // Place back button at the top
        layout.setCenter(centerWrapper); // Place game board in the center

        // Add all layers to this StackPane: background first, then layout on top
        getChildren().addAll(bg, layout);
    }
}