package gl.morpion.view.player;

import gl.morpion.model.Game;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PlayerNamesView extends BorderPane {

    private final TextField player1NameField = new TextField();
    private final TextField player2NameField = new TextField();
    private List<Player> players = new ArrayList<>();

    /**
     * @param onStart callback appelé avec (name1, name2) quand on clique "Commencer"
     * @param onBack  callback pour le bouton "Retour"
     */
    public PlayerNamesView(BiConsumer<String, String> onStart, Runnable onBack) {
        // Fond identique au menu
        getStyleClass().add("main-menu-bg");
        // ------ Titre
        Label title = new Label("Player vs Player");
        title.getStyleClass().add("title-glow");

        // ------ Formulaire
        Label p1Label = new Label("Name player 1 (X) :");
        p1Label.getStyleClass().add("form-label");

        Label p2Label = new Label("Name player 2 (O) :");
        p2Label.getStyleClass().add("form-label");

        player1NameField.setPromptText("Ex: Alice");
        player1NameField.getStyleClass().add("text-input");
        player1NameField.setMaxWidth(320);

        player2NameField.setPromptText("Ex: Bob");
        player2NameField.getStyleClass().add("text-input");
        player2NameField.setMaxWidth(320);

        // ------ Boutons (réutilisation du style du menu)
        Button startBtn = new Button("Start");
        startBtn.getStyleClass().add("big-button");
        startBtn.setOnAction(e -> {
            String n1 = safe(player1NameField.getText(), "PLayer 1");
            String n2 = safe(player2NameField.getText(), "Player 2");
            if (onStart != null){
                onStart.accept(n1, n2);
            }
        });
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> { if (onBack != null) onBack.run(); });

        // Carte centrale (look propre + espacement)
        VBox content = new VBox(12,
                title,
                spacer(8),
                p1Label, player1NameField,
                p2Label, player2NameField,
                spacer(8),
                startBtn,
                backBtn
        );
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(32));
        content.getStyleClass().add("form-card");

        setCenter(content);
        setPadding(new Insets(24));
    }//end contructor

    private String safe(String v, String def) {
        if (v == null) return def;
        v = v.trim();
        return v.isEmpty() ? def : v;
    }

    /**
     * @param p1 : r
     * @param def: the default name of a playe
     * @return
     */
    private String safeV2(Player p1, Player def) {
        String v = p1.getName();
        if (v == null) return def.getName();
        v = v.trim();
        return v.isEmpty() ? def.getName() : v;
    }

    private VBox spacer(double h) {
        VBox v = new VBox();
        v.setMinHeight(h);
        return v;
    }
}
