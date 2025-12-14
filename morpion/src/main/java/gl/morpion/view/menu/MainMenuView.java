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
import gl.morpion.audio.AudioManager;

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

        // ======================
        // Background
        // ======================
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());
        getChildren().add(bg);

        // ======================
        // Top bar (settings + language)
        // ======================
        Button btnSettings = new Button("⚙");
        btnSettings.getStyleClass().add("icon-button");
        btnSettings.setOnAction(e -> {
            AudioManager.playClick();          // 🔊 son
            controller.openSettings();
        });

        ToggleButton lang = new ToggleButton("FR");
        lang.getStyleClass().add("icon-button");
        lang.selectedProperty().addListener((o, w, is) -> {
            lang.setText(is ? "EN" : "FR");
            controller.toggleLanguage(is ? "en" : "fr");
        });

        Region spacerTop = new Region();
        HBox.setHgrow(spacerTop, Priority.ALWAYS);

        HBox topBar = new HBox(10, btnSettings, spacerTop, lang);
        topBar.setPadding(new Insets(16, 16, 0, 16));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // ======================
        // Center (title + buttons)
        // ======================
        Label title = new Label("Hi players");
        title.getStyleClass().add("title-glow");
        title.setFont(Font.font("Montserrat", FontWeight.EXTRA_BOLD, 56));
        title.setTextFill(Color.web("#e8f6ff"));

        vsBot    = big("Player vs Bots",   controller::startChooseBotDifficulty);
        vsPlayer = big("Player vs Player", controller::startModePvp);
        custom   = big("Custom",           controller::showCustomEntry);
        QUIT     = big("QUIT",              () -> controller.showMode("QUIT"));

        VBox btnCol = new VBox(14, vsBot, vsPlayer, custom, QUIT);
        btnCol.setAlignment(Pos.CENTER);

        VBox centerCol = new VBox(24, title, btnCol);
        centerCol.setAlignment(Pos.CENTER);

        // ======================
        // Bottom bar (version + rules)
        // ======================
        Label version = new Label("v2.0.1 (GL M1)");
        version.getStyleClass().add("version-label");

        Button rules = new Button("Rules…");
        rules.getStyleClass().add("pill-button");
        rules.setOnAction(e -> {
            AudioManager.playClick();          // 🔊 son
            controller.showRules();
        });

        Region spacerBottom = new Region();
        HBox.setHgrow(spacerBottom, Priority.ALWAYS);

        HBox bottomBar = new HBox(12, version, spacerBottom, rules);
        bottomBar.setPadding(new Insets(0, 24, 16, 16));
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        // ======================
        // Layout
        // ======================
        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(centerCol);
        layout.setBottom(bottomBar);

        getChildren().add(layout);

        // ======================
        // Keyboard navigation
        // ======================
        JavaFXUtils.setupKeyboardNav(this, vsBot, vsPlayer, custom);
    }

    /**
     * Helper method to create a styled button with custom action.
     * ALL big buttons will play the SAME sound here.
     */
    private Button big(String text, Runnable action) {
        Button b = new Button(text);
        b.getStyleClass().add("big-button");

        b.setOnAction(e -> {
            AudioManager.playClick();   // 🔊 SON ICI (UNE SEULE FOIS)
            if (action != null) action.run();
        });

        return b;
    }
}
