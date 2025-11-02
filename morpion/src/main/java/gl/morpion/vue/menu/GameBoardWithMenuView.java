
package gl.morpion.vue.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GameBoardWithMenuView extends StackPane {

    public GameBoardWithMenuView(Node gameBoardView, Runnable onBack) {
        setPrefSize(1200, 800);

        // Charger le CSS
        var css = getClass().getResource("/css/menu.css");
        if (css != null) getStylesheets().add(css.toExternalForm());

        // Fond avec le même style que le menu
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // Bouton "Retour" stylisé
        Button backButton = new Button("← Retour");
        backButton.getStyleClass().add("pill-button");
        backButton.setOnAction(e -> { if (onBack != null) onBack.run(); });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(10, backButton, spacer);
        topBar.setPadding(new Insets(16, 16, 0, 16));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // 🎨 CONTAINER STYLISÉ pour le plateau de jeu
        StackPane gameContainer = new StackPane(gameBoardView);
        gameContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Respecte la taille du plateau
        gameContainer.setAlignment(Pos.CENTER);

        // Effet d'ombre portée (optionnel)
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setRadius(20);
        shadow.setOffsetY(5);
        gameContainer.setEffect(shadow);

        // Style du container (bordure arrondie, fond semi-transparent)
        gameContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(30, 40, 50, 0.95), rgba(20, 30, 40, 0.95));" +
                        "-fx-border-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 25;"
        );

        // 🎯 Centre parfaitement le container dans la scène
        StackPane centerWrapper = new StackPane(gameContainer);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(80, 40, 40, 40)); // Espace autour

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(centerWrapper);

        getChildren().addAll(bg, layout);
    }
}