package gl.morpion.view.menu;

import gl.morpion.audio.SoundFX;
import gl.morpion.model.CustomGameConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * CustomModeView is the UI screen for configuring and starting a custom game.
 * <p>
 * The user can configure the game board size, shape, win condition, and player names (Player vs Player or Player vs Bot).
 * </p>
 */
public class CustomModeView extends BorderPane {

    private final Spinner<Integer> rowSpinner;
    private final Spinner<Integer> colSpinner;
    private final Spinner<Integer> winSpinner;
    private final ComboBox<String> shapeCombo;
    private final RadioButton pvpRadio;
    private final RadioButton pvbRadio;
    private final TextField player1Field;
    private final TextField player2Field;
    private final Label p2Label;
    private final Label winHintLabel;

    /**
     * Creates the Custom Mode view with the specified behavior for starting the game and navigating back.
     *
     * @param onStart a {@link Consumer} to be executed when the user starts the game with the configured settings
     * @param onBack  a {@link Runnable} to be executed when the user clicks "Back"
     */
    public CustomModeView(Consumer<CustomGameConfig> onStart, Runnable onBack) {
        // Background gradient
        getStyleClass().add("main-menu-bg");

        // ----- Title -----
        Label title = new Label("Custom mode");
        title.getStyleClass().add("title-glow");

        // ----- Board Size -----
        Label sizeLabel = new Label("Board size (3 - 10 rows × 3 - 10 columns):");
        sizeLabel.getStyleClass().add("form-label");

        rowSpinner = new Spinner<>(3, 10, 5);
        rowSpinner.setEditable(false);

        colSpinner = new Spinner<>(3, 10, 5);
        colSpinner.setEditable(false);

        HBox sizeBox = new HBox(8,
                new Label("Rows:"), rowSpinner,
                new Label("Columns:"), colSpinner
        );
        sizeBox.setAlignment(Pos.CENTER);

        // ----- Board Shape -----
        Label shapeLabel = new Label("Board shape:");
        shapeLabel.getStyleClass().add("form-label");

        shapeCombo = new ComboBox<>();
        shapeCombo.getItems().addAll("Rectangle", "Square");
        shapeCombo.getSelectionModel().selectFirst();

        shapeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Square".equalsIgnoreCase(newVal)) {
                // Square → same number of rows and columns
                colSpinner.getValueFactory().setValue(rowSpinner.getValue());
            }
            updateWinMax();
        });

        rowSpinner.valueProperty().addListener((obs, o, n) -> {
            if ("Square".equalsIgnoreCase(shapeCombo.getValue())) {
                colSpinner.getValueFactory().setValue(n);
            }
            updateWinMax();
        });

        colSpinner.valueProperty().addListener((obs, o, n) -> updateWinMax());

        // ----- Win Condition -----
        Label winLabel = new Label("Number of symbols to align (3 - 8):");
        winLabel.getStyleClass().add("form-label");

        // Range from 3 to 8 as in other modes
        winSpinner = new Spinner<>(3, 8, 5);
        winSpinner.setEditable(true);

        // Help text under the spinner
        winHintLabel = new Label();
        winHintLabel.getStyleClass().add("rules-text");

        winSpinner.valueProperty().addListener((obs, o, n) -> updateWinMax());

        // ----- Game Mode -----
        Label modeLabel = new Label("Game mode:");
        modeLabel.getStyleClass().add("form-label");

        ToggleGroup modeGroup = new ToggleGroup();
        pvpRadio = new RadioButton("Player vs Player");
        pvbRadio = new RadioButton("Player vs Bot");

        pvpRadio.setToggleGroup(modeGroup);
        pvbRadio.setToggleGroup(modeGroup);
        pvpRadio.setSelected(true);

        HBox modeBox = new HBox(16, pvpRadio, pvbRadio);
        modeBox.setAlignment(Pos.CENTER);

        // ----- Player Names -----
        Label p1Label = new Label("Name player 1 (X):");
        p1Label.getStyleClass().add("form-label");
        player1Field = new TextField();
        player1Field.setPromptText("Ex: Alice");
        player1Field.getStyleClass().add("text-input");
        player1Field.setMaxWidth(320);

        p2Label = new Label("Name player 2 (O):");
        p2Label.getStyleClass().add("form-label");
        player2Field = new TextField();
        player2Field.setPromptText("Ex: Bob / Bot");
        player2Field.getStyleClass().add("text-input");
        player2Field.setMaxWidth(320);

        // Logic for PVP / Bot for Player 2 field
        modeGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            boolean vsBot = newT == pvbRadio;
            if (vsBot) {
                // Bot mode → fixed name "BOT", disable field
                player2Field.setText("BOT");
                player2Field.setDisable(true);
                player2Field.setOpacity(0.6);
                p2Label.setText("Bot name (O):");
            } else {
                // PVP mode → editable field
                if ("BOT".equals(player2Field.getText())) {
                    player2Field.clear();
                }
                player2Field.setDisable(false);
                player2Field.setOpacity(1.0);
                p2Label.setText("Name player 2 (O):");
            }
        });

        // ----- Buttons -----
        Button startBtn = new Button("Start game");
        startBtn.getStyleClass().add("big-button");
        startBtn.setOnAction(e -> {
            if (onStart != null) {
                CustomGameConfig cfg = buildConfig();
                onStart.accept(cfg);
            }
        });

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });
        SoundFX.attachReturn(backBtn);

        VBox content = new VBox(
                16,
                title,
                sizeLabel, sizeBox,
                shapeLabel, shapeCombo,
                winLabel, winSpinner,
                winHintLabel,              // Help text "3 - 8 / limited by size"
                modeLabel, modeBox,
                p1Label, player1Field,
                p2Label, player2Field,
                startBtn,
                backBtn
        );
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(32));
        content.getStyleClass().add("form-card");

        setCenter(content);
        setPadding(new Insets(24));

        // Initialize the help text
        updateWinMax();
    }

    /**
     * Updates the logical limit of the number of symbols to align.
     * <p>
     * The maximum is constrained to the smaller of 8 and the larger dimension of the board (rows or columns).
     * </p>
     * It also updates the help text shown below the win condition spinner.
     */
    private void updateWinMax() {
        int rows = rowSpinner.getValue();
        int cols = colSpinner.getValue();
        int maxBoard = Math.max(rows, cols);
        int logicalMax = Math.min(8, maxBoard); // Never > 8 and never > board size

        SpinnerValueFactory<Integer> vf = winSpinner.getValueFactory();
        if (vf instanceof SpinnerValueFactory.IntegerSpinnerValueFactory intVF) {
            intVF.setMax(logicalMax);
            if (winSpinner.getValue() > logicalMax) {
                winSpinner.getValueFactory().setValue(logicalMax);
            }
        }

        // Help text as seen in other screens
        winHintLabel.setText(
                "You can choose between 3 and " + logicalMax +
                        " symbols to align (limited by board size " + rows + " × " + cols + ")."
        );
    }

    /**
     * Builds a {@link CustomGameConfig} based on the user's input in the UI.
     *
     * @return a {@link CustomGameConfig} instance
     */
    private CustomGameConfig buildConfig() {
        int rows = rowSpinner.getValue();
        int cols = colSpinner.getValue();

        String shapeLabel = shapeCombo.getValue();
        String shapeKey;
        if ("Square".equalsIgnoreCase(shapeLabel)) {
            shapeKey = "SQUARE";
            cols = rows; // Square board
        } else {
            shapeKey = "RECTANGLE";
        }

        int win = winSpinner.getValue();
        int maxAlign = Math.max(rows, cols);
        int logicalMax = Math.min(8, maxAlign);
        if (win > logicalMax) {
            win = logicalMax;
            winSpinner.getValueFactory().setValue(win);
        }

        boolean vsBot = pvbRadio.isSelected();

        String name1 = player1Field.getText().trim();
        if (name1.isEmpty()) name1 = "Player 1";

        String name2;
        if (vsBot) {
            name2 = "BOT";
        } else {
            String raw = player2Field.getText().trim();
            name2 = raw.isEmpty() ? "Player 2" : raw;
        }

        return new CustomGameConfig(
                rows,
                cols,
                shapeKey,
                win,
                vsBot,
                name1,
                name2
        );
    }
}
