package gl.morpion.view.menu;
import gl.morpion.audio.SoundFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Vue d'entrée pour le mode Custom :
 * - New custom game (configurer une nouvelle partie)
 * - Load saved game (charger une sauvegarde)
 * - Back (retour au menu principal)
 */
public class CustomView extends StackPane {

    /**
     * @param onNewGame callback quand on clique sur "New custom game"
     * @param onLoad    callback quand on clique sur "Load saved game"
     * @param onBack    callback quand on clique sur "Back"
     */
    public CustomView(Runnable onNewGame, Runnable onLoad, Runnable onBack) {
        setPrefSize(1200, 800);

        // Fond avec le même style que le menu principal
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // Titre
        Label title = new Label("Custom mode");
        title.getStyleClass().add("title-glow");

        Label subMsg = new Label("Choose an option");
        subMsg.getStyleClass().add("form-label");

        // Bouton "New custom game"
        Button newGameBtn = new Button("New custom game");
        newGameBtn.getStyleClass().add("big-button");
        newGameBtn.setOnAction(e -> {
            if (onNewGame != null) onNewGame.run();
        });

        // Bouton "Load saved game"
        Button loadBtn = new Button("Load saved game");
        loadBtn.getStyleClass().add("big-button");
        loadBtn.setOnAction(e -> {
            if (onLoad != null) onLoad.run();
        });

        // Bouton "Back"
        Button back = new Button("← Back");
        back.getStyleClass().add("pill-button");
        back.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });
        SoundFX.attachReturn(back);
        VBox box = new VBox(16, title, subMsg, newGameBtn, loadBtn, back);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(16));
        box.getStyleClass().add("form-card");

        getChildren().addAll(bg, box);
    }
}
