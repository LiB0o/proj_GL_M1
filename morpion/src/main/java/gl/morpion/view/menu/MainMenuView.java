package gl.morpion.view.menu;

import gl.morpion.controllers.menu.MainMenuController;
import gl.morpion.util.JavaFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Main menu view for the Tic-Tac-Toe game.
 * Displays a styled menu with game mode options, settings, and navigation controls.
 * Extends StackPane to layer background and UI components.
 */
public class MainMenuView extends StackPane {

    // Main menu buttons for different game modes
    private Button vsBot, vsPlayer, custom, QUIT;

    /**
     * Constructor: Creates the main menu UI with all components and styling.
     * 
     * @param controller The MainMenuController that handles user interactions and navigation
     */
    public MainMenuView(MainMenuController controller) {
        // Set preferred size for the menu view (1200x800 pixels)
        setPrefSize(1200, 800);

        // Load external CSS stylesheet for menu styling
        var css = getClass().getResource("/css/menu.css");
        if (css != null) getStylesheets().add(css.toExternalForm());

        // Fond
        // Create background region with styled class
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        // Bind background dimensions to parent container size (responsive)
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());
        // Add background to the scene graph
        getChildren().add(bg);

        // top-right: settings + FR
        // Create settings button with gear icon
        Button btnSettings = new Button("⚙");
        btnSettings.getStyleClass().add("icon-button");
        // Attach click handler to open settings
        btnSettings.setOnAction(e -> controller.openSettings());

        // Create language toggle button (FR/EN)
        ToggleButton lang = new ToggleButton("FR");
        lang.getStyleClass().add("icon-button");
        // Listen for toggle state changes and update language accordingly
        lang.selectedProperty().addListener((o,w,is)->{
            // Update button text based on toggle state (EN when selected, FR when not)
            lang.setText(is ? "EN" : "FR");
            // Notify controller to switch language (en when selected, fr when not)
            controller.toggleLanguage(is ? "en" : "fr");
        });

        // Create spacer region to push buttons to opposite ends of the bar
        Region spacerTop = new Region();
        HBox.setHgrow(spacerTop, Priority.ALWAYS);

        // Create top bar with settings button (left) and language toggle (right)
        HBox topBar = new HBox(10, btnSettings, spacerTop, lang);
        topBar.setPadding(new Insets(16, 16, 0, 16));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // centre: titre + 3 boutons
        // Create main title label with custom styling
        Label title = new Label("Hi players");
        title.getStyleClass().add("title-glow");
        title.setFont(Font.font("Montserrat", FontWeight.EXTRA_BOLD, 56));
        title.setTextFill(Color.web("#e8f6ff"));

        // Create three main game mode buttons with their respective actions
        vsBot    = big("Player vs Bots",   () -> controller.showMode("Player vs Bots"));
        vsPlayer = big("Player vs Player", controller::startModePvp);
        custom   = big("Custom",           () -> controller.showMode("Custom"));
        QUIT   = big("QUIT",           () -> controller.showMode("QUIT"));
        // Arrange buttons vertically with 14px spacing between them
        VBox btnCol = new VBox(14, vsBot, vsPlayer, custom, QUIT);
        btnCol.setAlignment(Pos.CENTER);

        // Combine title and buttons in a centered vertical layout with 24px spacing
        VBox centerCol = new VBox(24, title, btnCol);
        centerCol.setAlignment(Pos.CENTER);

        // bottom: version left + rules right
        // Create version label for bottom-left corner
        Label version = new Label("v1.0.2 (GL M1)");
        version.getStyleClass().add("version-label");

        // Create rules button for bottom-right corner
        Button rules = new Button("Rules…");
        rules.getStyleClass().add("pill-button");
        rules.setOnAction(e -> controller.showRules());

        // Create spacer to separate version and rules button
        Region spacerBottom = new Region();
        HBox.setHgrow(spacerBottom, Priority.ALWAYS);
        // Create bottom bar with version (left) and rules button (right)
        HBox bottomBar = new HBox(12, version, spacerBottom, rules);
        bottomBar.setPadding(new Insets(0,24,16,16));
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        // Assemble the complete layout using BorderPane (top, center, bottom regions)
        BorderPane layout = new BorderPane();
        layout.setTop(topBar);

        layout.setCenter(centerCol);
        layout.setBottom(bottomBar);

        // Add the layout on top of the background
        getChildren().add(layout);

        // nav clavier
        // Setup keyboard navigation (arrow keys) for the three main buttons
        JavaFXUtils.setupKeyboardNav(this, vsBot, vsPlayer, custom);
    }

    /**
     * Helper method to create a styled button with custom action.
     * 
     * @param text The text to display on the button
     * @param action The action to execute when the button is clicked
     * @return A styled Button instance with the "big-button" CSS class
     */
    private Button big(String text, Runnable action) {
        // Create a new button with the specified text
        Button b = new Button(text);
        b.getStyleClass().add("big-button");
        // Attach click handler that executes the provided action
        b.setOnAction(e -> { if (action != null) action.run(); });
        return b;
    }
}
