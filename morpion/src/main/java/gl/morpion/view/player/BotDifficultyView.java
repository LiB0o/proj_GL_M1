package gl.morpion.view.player;
import gl.morpion.audio.SoundFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class BotDifficultyView extends BorderPane {

    /**
     * @param onSelect callback appelé avec "EASY", "NORMAL" ou "HARD"
     * @param onBack   callback pour revenir au menu principal
     */
    public BotDifficultyView(Consumer<String> onSelect, Runnable onBack) {
        // Fond identique au menu principal
        getStyleClass().add("main-menu-bg");

        // Titre
        Label title = new Label("Choose Bot Difficulty");
        title.getStyleClass().add("title-glow");

        // Boutons (même style que les gros boutons du menu)
        Button easyBtn = new Button("Easy");
        easyBtn.getStyleClass().add("big-button");
        easyBtn.setOnAction(e -> onSelect.accept("EASY"));

        Button normalBtn = new Button("Normal");
        normalBtn.getStyleClass().add("big-button");
        normalBtn.setOnAction(e -> onSelect.accept("NORMAL"));

        Button hardBtn = new Button("Hard");
        hardBtn.getStyleClass().add("big-button");
        hardBtn.setOnAction(e -> onSelect.accept("HARD"));

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> onBack.run());
        SoundFX.attachReturn(backBtn);


        VBox box = new VBox(14, title, easyBtn, normalBtn, hardBtn, backBtn);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.getStyleClass().add("form-card");

        setCenter(box);
        setPadding(new Insets(24));
    }
}
