package gl.morpion.view.player;

import gl.morpion.audio.SoundFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.IntConsumer;

/**
 * Vue qui permet de choisir le nombre de symboles à aligner pour gagner.
 * L'utilisateur tape un entier au clavier (entre MIN et MAX).
 */
public class WinConditionView extends BorderPane {

    private static final int MIN = 3;
    private static final int MAX = 8;

    private final TextField numberField = new TextField();
    private final Label errorLabel = new Label();

    /**
     * @param currentValue valeur actuelle (par ex. 5)
     * @param onValidate   callback appelé avec la nouvelle valeur validée
     * @param onBack       callback pour le bouton "Back"
     */
    public WinConditionView(int currentValue,
                            IntConsumer onValidate,
                            Runnable onBack) {

        // Fond comme le menu principal
        getStyleClass().add("main-menu-bg");

        // --------- Titre ----------
        Label title = new Label("Win condition");
        title.getStyleClass().add("title-glow");

        // --------- Texte d'explication ----------
        Label info = new Label(
                "How many symbols in a row to win ? (" + MIN + " - " + MAX + ")"
        );
        info.getStyleClass().add("form-label");

        // --------- Champ de saisie ----------
        numberField.setText(String.valueOf(currentValue));
        numberField.setPromptText(MIN + " - " + MAX);
        numberField.getStyleClass().add("text-input");
        numberField.setMaxWidth(120);

        // Quand on appuie sur Entrée dans le champ, on valide
        numberField.setOnAction(e -> validateAndSend(onValidate));

        // --------- Label d'erreur ----------
        errorLabel.setText(""); // vide par défaut
        // Si tu veux le styliser, tu peux ajouter une classe CSS:
        // errorLabel.getStyleClass().add("error-label");

        // --------- Boutons ----------
        Button validateBtn = new Button("Validate");
        validateBtn.getStyleClass().add("big-button");
        validateBtn.setOnAction(e -> validateAndSend(onValidate));

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });
        SoundFX.attachReturn(backBtn);

        // --------- Layout principal ----------
        VBox content = new VBox(12,
                title,
                info,
                numberField,
                errorLabel,
                validateBtn,
                backBtn
        );
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(32));
        content.getStyleClass().add("form-card");

        setCenter(content);
        setPadding(new Insets(24));
    }

    /**
     * Lit la valeur tapée, vérifie qu'elle est entre MIN et MAX,
     * puis appelle le callback si tout est OK.
     */
    private void validateAndSend(IntConsumer onValidate) {
        if (onValidate == null) return;

        String txt = numberField.getText();
        if (txt == null || txt.trim().isEmpty()) {
            errorLabel.setText("Please type a number between " + MIN + " and " + MAX + ".");
            return;
        }

        try {
            int value = Integer.parseInt(txt.trim());
            if (value < MIN || value > MAX) {
                errorLabel.setText("Value must be between " + MIN + " and " + MAX + ".");
                return;
            }

            // OK → on efface l'erreur et on envoie la valeur
            errorLabel.setText("");
            onValidate.accept(value);

        } catch (NumberFormatException ex) {
            errorLabel.setText("Please type a valid integer.");
        }
    }
}
