package gl.morpion.view.menu;

import gl.morpion.audio.SoundFX;
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
 * View displaying the list of saved games.
 * <p>
 * Simple version: a single text column (based on {@link SaveMetadata#toString()}),
 * plus Play / Delete / Back actions.
 * </p>
 */
public class SaveListView extends BorderPane {

    /** Table displaying available saves. */
    private final TableView<SaveMetadata> table;

    /** Callback executed when the user chooses to load a selected save. */
    private final Consumer<SaveMetadata> onLoad;

    /** Callback executed when the user clicks the Back button. */
    private final Runnable onBack;

    /**
     * Creates a SaveListView.
     *
     * @param saves  list of saves to display (may be {@code null})
     * @param onLoad callback executed when the user clicks Play (may be {@code null})
     * @param onBack callback executed when the user clicks Back (may be {@code null})
     */
    public SaveListView(List<SaveMetadata> saves,
                        Consumer<SaveMetadata> onLoad,
                        Runnable onBack) {

        this.onLoad = onLoad;
        this.onBack = onBack;

        // Background (same CSS class as the menu)
        getStyleClass().add("main-menu-bg");

        // Title
        Label title = new Label("Saved games");
        title.getStyleClass().add("title-glow");

        // Saves table
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(new Label("No saved games found."));

        // Single column: display using SaveMetadata.toString()
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

        // Play / Delete / Back buttons
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
        SoundFX.attachReturn(backBtn);

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

    /**
     * Handles the "Play" action by loading the currently selected save.
     * <p>
     * If no save is selected, an informational alert is displayed.
     * </p>
     */
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

    /**
     * Handles the "Delete" action by deleting the currently selected save.
     * <p>
     * A confirmation dialog is shown before deleting. If confirmed, the save file is removed
     * through {@link SaveManager#deleteSave(String)} and the entry is removed from the table.
     * </p>
     */
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
                // Pass the fileName (String), not the object itself
                SaveManager.deleteSave(selected.getFileName());
                table.getItems().remove(selected);
            }
        });
    }

    /**
     * Displays an informational alert dialog.
     *
     * @param title the alert title
     * @param msg   the message to display
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
