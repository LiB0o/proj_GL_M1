package gl.morpion.view.menu;

import gl.morpion.audio.SoundFX;
import gl.morpion.audio.AudioManager;
import gl.morpion.controllers.menu.MainMenuController;
import gl.morpion.settings.SettingsModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Settings screen view.
 * <p>
 * This view allows the user to configure application settings such as:
 * </p>
 * <ul>
 *     <li>Fullscreen mode</li>
 *     <li>Mute state</li>
 *     <li>Music volume</li>
 *     <li>SFX volume</li>
 *     <li>Screen resolution (preset or custom)</li>
 * </ul>
 *
 * <p>
 * Side effects: updates and persists {@link SettingsModel}, and applies changes immediately
 * through {@link MainMenuController} and {@link AudioManager}.
 * </p>
 */
public class SettingsView extends StackPane {

    /**
     * Creates the Settings view.
     *
     * @param controller the main menu controller used to navigate and apply UI changes
     * @param settings   the current settings model (loaded from persistence)
     */
    public SettingsView(MainMenuController controller, SettingsModel settings) {

        // ===== Background =====
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // ===== Title =====
        Label title = new Label("Settings");
        title.getStyleClass().add("title-glow");

        // ===== Fullscreen =====
        CheckBox fullscreen = new CheckBox("Fullscreen");
        fullscreen.setSelected(settings.isFullscreen());
        fullscreen.getStyleClass().add("form-label");

        fullscreen.selectedProperty().addListener((o, oldV, newV) -> {
            settings.setFullscreen(newV);
            settings.save();
            controller.applyFullscreen(newV);
        });

        // ===== Music =====
        Label musicLabel = new Label("Music volume");
        musicLabel.getStyleClass().add("form-label");

        Slider music = new Slider(0, 100, settings.getMusicVolume());
        music.valueProperty().addListener((o, a, b) -> {
            settings.setMusicVolume(b.intValue());
            settings.save();
            AudioManager.setMusicVolume(settings.isMuted() ? 0.0 : b.doubleValue() / 100.0);
        });

        // ===== SFX =====
        Label sfxLabel = new Label("SFX volume");
        sfxLabel.getStyleClass().add("form-label");

        Slider sfx = new Slider(0, 100, settings.getSfxVolume());
        sfx.valueProperty().addListener((o, a, b) -> {
            settings.setSfxVolume(b.intValue());
            settings.save();
            AudioManager.setSfxVolume(b.doubleValue() / 100.0);
            AudioManager.playClick();
        });

        // ===== Mute =====
        CheckBox mute = new CheckBox("Mute all sounds");
        mute.setSelected(settings.isMuted());
        mute.getStyleClass().add("form-label");

        mute.selectedProperty().addListener((o, a, b) -> {
            settings.setMuted(b);
            settings.save();
            AudioManager.setMuted(b);
            // Ensure mute is reflected on the music channel
            AudioManager.setMusicVolume(settings.isMuted() ? 0.0 : settings.getMusicVolume() / 100.0);
        });

        // ===== Resolution =====
        Label resLabel = new Label("Resolution");
        resLabel.getStyleClass().add("form-label");

        ChoiceBox<String> res = new ChoiceBox<>();
        res.getItems().addAll("1200x800", "1600x900", "1920x1080");
        res.setValue(settings.getResolution());

        // Custom input (editable)
        TextField customRes = new TextField(settings.getResolution());
        customRes.getStyleClass().add("text-input");   // keep your style
        customRes.setPrefWidth(180);
        customRes.setPromptText("ex: 1600x900");

        Button apply = new Button("Apply");
        apply.getStyleClass().add("big-button");
        apply.setPrefWidth(140);

        // When selecting a preset -> also fill the field and apply immediately
        res.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> {
            if (b == null) return;
            customRes.setText(b);

            settings.setResolution(b);
            settings.save();
            controller.applyResolution(b);
        });

        // Local utility function
        Runnable applyCustom = () -> {
            String value = customRes.getText().trim();

            // Expected format: 1234x567
            if (!value.matches("\\d{3,5}x\\d{3,5}")) {
                customRes.setStyle(customRes.getStyle() + "; -fx-border-color: red;");
                return;
            }

            String[] d = value.split("x");
            int w = Integer.parseInt(d[0]);
            int h = Integer.parseInt(d[1]);

            // Reasonable bounds
            if (w < 800 || h < 600 || w > 7680 || h > 4320) {
                customRes.setStyle(customRes.getStyle() + "; -fx-border-color: red;");
                return;
            }

            // Reset border if OK (keep the base style)
            customRes.setStyle(null);
            customRes.getStyleClass().add("text-input");

            settings.setResolution(value);
            settings.save();
            controller.applyResolution(value);
        };

        apply.setOnAction(e -> applyCustom.run());
        customRes.setOnAction(e -> applyCustom.run()); // Enter key in the field => apply

        HBox resRow = new HBox(12, res, customRes, apply);
        resRow.setAlignment(Pos.CENTER);

        // ===== Back =====
        Button back = new Button("Back");
        back.getStyleClass().add("big-button");
        back.setOnAction(e -> controller.showMainMenu());
        SoundFX.attachReturn(back);

        // ===== Card =====
        VBox card = new VBox(16,
                title,
                fullscreen,
                mute,
                musicLabel, music,
                sfxLabel, sfx,
                resLabel, resRow,
                back
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(32));
        card.setMaxWidth(720); // slightly wider because of the field + button
        card.getStyleClass().add("form-card");

        getChildren().addAll(bg, card);
    }
}
