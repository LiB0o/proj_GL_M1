package gl.morpion.view.menu;

import gl.morpion.persistence.SaveManager;
import gl.morpion.persistence.SaveMetadata;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

/**
 * Vue affichant la liste des sauvegardes.
 * Version simple : une colonne texte (toString() de SaveMetadata),
 * + Play / Delete / Back.
 */
public class SaveListView extends BorderPane {

    private final TableView<SaveMetadata> table;
    private final Consumer<SaveMetadata> onLoad;
    private final Runnable onBack;

    public SaveListView(List<SaveMetadata> saves,
                        Consumer<SaveMetadata> onLoad,
                        Runnable onBack) {

        this.onLoad = onLoad;
        this.onBack = onBack;

        // Fond (même classe CSS que le menu)
        getStyleClass().add("main-menu-bg");

        // Titre
        Label title = new Label("Saved games");
        title.getStyleClass().add("title-glow");

        // Table des sauvegardes
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(new Label("No saved games found."));

        // Une seule colonne : affichage via toString() de SaveMetadata
        TableColumn<SaveMetadata, String> nameCol = new TableColumn<>("Save");
        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue() != null ? data.getValue().toString() : ""
                )
        );

        table.getColumns().add(nameCol);
        if (saves != null) {
            table.getItems().addAll(saves);
        }

        // Boutons Play / Delete / Back
        Button playBtn = new Button("Play");
        playBtn.getStyleClass().add("big-button");
        playBtn.setOnAction(e -> handlePlay());

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("pill-button");
        deleteBtn.setOnAction(e -> handleDelete());

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });

        HBox buttons = new HBox(12, playBtn, deleteBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(12));

        VBox centerBox = new VBox(16, title, table, buttons);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(24));
        centerBox.getStyleClass().add("form-card");

        setCenter(centerBox);
        setPadding(new Insets(24));
    }

    private void handlePlay() {
        SaveMetadata selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No save selected", "Please select a saved game first.");
            return;
        }
        if (onLoad != null) {
            onLoad.accept(selected);
        }
    }

    private void handleDelete() {
        SaveMetadata selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No save selected", "Please select a saved game to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete save");
        confirm.setHeaderText("Delete save \"" + selected.getSaveName() + "\" ?");
        confirm.setContentText("This action cannot be undone.");

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                // 🔹 ICI la correction : on passe le fileName (String), pas l'objet entier
                SaveManager.deleteSave(selected.getFileName());
                table.getItems().remove(selected);
            }
        });
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
