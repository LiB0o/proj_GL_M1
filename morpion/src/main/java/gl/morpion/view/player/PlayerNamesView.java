package gl.morpion.view.player;

import gl.morpion.audio.SoundFX;
import gl.morpion.model.Game;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
     * Constructeur d'origine : mode Player vs Player
     * => NE CHANGE PAS, on le garde pour ton startModePvp()
     *
     * @param onStart callback appelé avec (name1, name2) quand on clique "Start"
     * @param onBack  callback pour le bouton "Back"
     */
    public PlayerNamesView(BiConsumer<String, String> onStart, Runnable onBack) {
        this(false, onStart, onBack); // false = pas vsBot → mode PvP classique
    }

    /**
     * Nouveau constructeur : permet de choisir si on est en mode vsBot ou non.
     * vsBot = false → Player vs Player (2 champs)
     * vsBot = true  → Player vs Bot (1 seul champ, Player 2 = "BOT")
     */
    public PlayerNamesView(boolean vsBot, BiConsumer<String, String> onStart, Runnable onBack) {
        // Fond identique au menu
        getStyleClass().add("main-menu-bg");

        // ------ Titre
        Label title = new Label(vsBot ? "Player vs Bot" : "Player vs Player");
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

        // Si on est en mode BOT → on cache le champ du joueur 2
        if (vsBot) {
            p2Label.setVisible(false);
            p2Label.setManaged(false);
            player2NameField.setVisible(false);
            player2NameField.setManaged(false);
        }

        // ------ Boutons (réutilisation du style du menu)
        Button startBtn = new Button("Start");
        startBtn.getStyleClass().add("big-button");
        startBtn.setOnAction(e -> {
            // En PvP : n1 & n2 saisis par les joueurs
            // En vsBot : n1 saisi, n2 = "BOT"
            String n1 = safe(player1NameField.getText(), "PLayer 1");
            String n2 = vsBot
                    ? "BOT"
                    : safe(player2NameField.getText(), "Player 2");

            if (onStart != null) {
                onStart.accept(n1, n2);
            }
        });

        //return menu
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });
        SoundFX.attachReturn(backBtn);

        // Carte centrale (look propre + espacement)
        VBox content = new VBox(12,
                title,
                spacer(8),
                p1Label, player1NameField,
                // En mode vsBot on ne montre pas le label/champ 2
                (vsBot ? spacer(0) : p2Label),
                (vsBot ? spacer(0) : player2NameField),
                spacer(8),
                startBtn,
                backBtn
        );
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(32));
        content.getStyleClass().add("form-card");

        setCenter(content);
        setPadding(new Insets(24));
    }//end constructor



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
