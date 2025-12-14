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

    public CustomModeView(Consumer<CustomGameConfig> onStart, Runnable onBack) {
        // Fond gradient
        getStyleClass().add("main-menu-bg");

        // ----- Titre -----
        Label title = new Label("Custom mode");
        title.getStyleClass().add("title-glow");

        // ----- Taille du plateau -----
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

        // ----- Forme du plateau -----
        Label shapeLabel = new Label("Board shape:");
        shapeLabel.getStyleClass().add("form-label");

        shapeCombo = new ComboBox<>();
        shapeCombo.getItems().addAll("Rectangle", "Square");
        shapeCombo.getSelectionModel().selectFirst();

        shapeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Square".equalsIgnoreCase(newVal)) {
                // carré → mêmes lignes et colonnes
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

        // ----- Win condition -----
        Label winLabel = new Label("Number of symbols to align (3 - 8):");
        winLabel.getStyleClass().add("form-label");

        // de 3 à 8 comme dans les autres modes
        winSpinner = new Spinner<>(3, 8, 5);
        winSpinner.setEditable(true);

        // Petit texte explicatif sous le spinner
        winHintLabel = new Label();
        winHintLabel.getStyleClass().add("rules-text");

        winSpinner.valueProperty().addListener((obs, o, n) -> updateWinMax());

        // ----- Mode de jeu -----
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

        // ----- Noms -----
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

        // 🔁 Logique PVP / Bot pour le champ Player 2
        modeGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            boolean vsBot = newT == pvbRadio;
            if (vsBot) {
                // Mode Bot → nom fixé "BOT", champ désactivé
                player2Field.setText("BOT");
                player2Field.setDisable(true);
                player2Field.setOpacity(0.6);
                p2Label.setText("Bot name (O):");
            } else {
                // Mode PVP → champ éditable
                if ("BOT".equals(player2Field.getText())) {
                    player2Field.clear();
                }
                player2Field.setDisable(false);
                player2Field.setOpacity(1.0);
                p2Label.setText("Name player 2 (O):");
            }
        });

        // ----- Boutons -----
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
                winHintLabel,              // 🔹 texte "3 - 8 / limité par la taille"
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

        // initialiser le texte d'aide
        updateWinMax();
    }

    /**
     * Met à jour la limite logique du nombre de symboles :
     * - min(8, max(rows, cols))
     * - met à jour le texte d'aide
     */
    private void updateWinMax() {
        int rows = rowSpinner.getValue();
        int cols = colSpinner.getValue();
        int maxBoard = Math.max(rows, cols);
        int logicalMax = Math.min(8, maxBoard); // jamais > 8 et jamais > taille du plateau

        SpinnerValueFactory<Integer> vf = winSpinner.getValueFactory();
        if (vf instanceof SpinnerValueFactory.IntegerSpinnerValueFactory intVF) {
            intVF.setMax(logicalMax);
            if (winSpinner.getValue() > logicalMax) {
                winSpinner.getValueFactory().setValue(logicalMax);
            }
        }

        // texte explicatif comme dans tes autres écrans
        winHintLabel.setText(
                "You can choose between 3 and " + logicalMax +
                        " symbols to align (limited by board size " + rows + " × " + cols + ")."
        );
    }

    private CustomGameConfig buildConfig() {
        int rows = rowSpinner.getValue();
        int cols = colSpinner.getValue();

        String shapeLabel = shapeCombo.getValue();
        String shapeKey;
        if ("Square".equalsIgnoreCase(shapeLabel)) {
            shapeKey = "SQUARE";
            cols = rows; // carré
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
