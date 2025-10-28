package gl.morpion.vue.menu;

import gl.morpion.controllers.menu.MainMenuController;
import gl.morpion.util.JavaFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainMenuView extends StackPane {

    private Button vsBot, vsPlayer, custom;

    public MainMenuView(MainMenuController controller) {
        setPrefSize(1200, 800);

        var css = getClass().getResource("/css/menu.css");
        if (css != null) getStylesheets().add(css.toExternalForm());

        // Fond
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());
        getChildren().add(bg);

        // top-right: settings + FR
        Button btnSettings = new Button("⚙");
        btnSettings.getStyleClass().add("icon-button");
        btnSettings.setOnAction(e -> controller.openSettings());

        ToggleButton lang = new ToggleButton("FR");
        lang.getStyleClass().add("icon-button");
        lang.selectedProperty().addListener((o,w,is)->{
            lang.setText(is ? "EN" : "FR");
            controller.toggleLanguage(is ? "en" : "fr");
        });

        Region spacerTop = new Region();
        HBox.setHgrow(spacerTop, Priority.ALWAYS);


        HBox topBar = new HBox(10, btnSettings, spacerTop, lang);
        topBar.setPadding(new Insets(16, 16, 0, 16));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // centre: titre + 3 boutons
        Label title = new Label("Hi players");
        title.getStyleClass().add("title-glow");
        title.setFont(Font.font("Montserrat", FontWeight.EXTRA_BOLD, 56));
        title.setTextFill(Color.web("#e8f6ff"));

        vsBot    = big("Player vs Bots",   () -> controller.showMode("Player vs Bots"));
        vsPlayer = big("Player vs Player", () -> controller.showMode("Player vs Player"));
        custom   = big("Custom",           () -> controller.showMode("Custom"));

        VBox btnCol = new VBox(14, vsBot, vsPlayer, custom);
        btnCol.setAlignment(Pos.CENTER);

        VBox centerCol = new VBox(24, title, btnCol);
        centerCol.setAlignment(Pos.CENTER);

        // bottom: version left + rules right
        Label version = new Label("v0.1 (GL M1)");
        version.getStyleClass().add("version-label");

        Button rules = new Button("Rules…");
        rules.getStyleClass().add("pill-button");
        rules.setOnAction(e -> controller.showRules());

        Region spacerBottom = new Region();
        HBox.setHgrow(spacerBottom, Priority.ALWAYS);
        HBox bottomBar = new HBox(12, version, spacerBottom, rules);
        bottomBar.setPadding(new Insets(0,24,16,16));
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);

        layout.setCenter(centerCol);
        layout.setBottom(bottomBar);

        getChildren().add(layout);

        // nav clavier
        JavaFXUtils.setupKeyboardNav(this, vsBot, vsPlayer, custom);
    }

    private Button big(String text, Runnable action) {
        Button b = new Button(text);
        b.getStyleClass().add("big-button");
        b.setOnAction(e -> { if (action != null) action.run(); });
        return b;
    }
}
