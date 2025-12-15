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

/**
 * View used to collect player names before starting a game.
 * <p>
 * This screen supports both modes:
 * </p>
 * <ul>
 *     <li>Player vs Player: two input fields (player 1 and player 2)</li>
 *     <li>Player vs Bot: one input field (player 1), player 2 is automatically set to {@code "BOT"}</li>
 * </ul>
 *
 * <p>
 * Side effects: none (it only collects user input and triggers callbacks).
 * </p>
 */
public class PlayerNamesView extends BorderPane {

    private final TextField player1NameField = new TextField();
    private final TextField player2NameField = new TextField();
    private List<Player> players = new ArrayList<>();

    /**
     * Original constructor: Player vs Player mode.
     * <p>
     * This constructor is kept for compatibility with the existing PvP flow.
     * </p>
     *
     * @param onStart callback invoked with (name1, name2) when the user clicks "Start"
     * @param onBack  callback executed when the user clicks "Back"
     */
    public PlayerNamesView(BiConsumer<String, String> onStart, Runnable onBack) {
        this(false, onStart, onBack); // false = not vsBot → classic PvP mode
    }

    /**
     * Constructor allowing to choose whether the screen is for a vsBot mode or not.
     * <p>
     * {@code vsBot = false} → Player vs Player (2 fields).<br>
     * {@code vsBot = true}  → Player vs Bot (1 field, Player 2 is forced to {@code "BOT"}).
     * </p>
     *
     * @param vsBot   whether the second player is a bot (hides player 2 input when true)
     * @param onStart callback invoked with the resolved player names (name1, name2)
     * @param onBack  callback executed when the user clicks "Back"
     */
    public PlayerNamesView(boolean vsBot, BiConsumer<String, String> onStart, Runnable onBack) {
        // Same background as the menu
        getStyleClass().add("main-menu-bg");

        // ------ Title
        Label title = new Label(vsBot ? "Player vs Bot" : "Player vs Player");
        title.getStyleClass().add("title-glow");

        // ------ Form
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

        // If BOT mode → hide player 2 label/field
        if (vsBot) {
            p2Label.setVisible(false);
            p2Label.setManaged(false);
            player2NameField.setVisible(false);
            player2NameField.setManaged(false);
        }

        // ------ Buttons (reuse menu styles)
        Button startBtn = new Button("Start");
        startBtn.getStyleClass().add("big-button");
        startBtn.setOnAction(e -> {
            // PvP: n1 & n2 typed by players
            // vsBot: n1 typed, n2 = "BOT"
            String n1 = safe(player1NameField.getText(), "PLayer 1");
            String n2 = vsBot
                    ? "BOT"
                    : safe(player2NameField.getText(), "Player 2");

            if (onStart != null) {
                onStart.accept(n1, n2);
            }
        });

        // Return to menu
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("pill-button");
        backBtn.setOnAction(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });
        SoundFX.attachReturn(backBtn);

        // Central card (clean layout + spacing)
        VBox content = new VBox(12,
                title,
                spacer(8),
                p1Label, player1NameField,
                // In vsBot mode we do not show label/field 2
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
    } // end constructor

    /**
     * Returns a safe (non-empty) string value.
     * <p>
     * The input is trimmed; if it is {@code null} or blank, the provided default value is returned.
     * </p>
     *
     * @param v   the raw string value
     * @param def the default value to use if {@code v} is null/blank
     * @return {@code v} trimmed, or {@code def} if {@code v} is null/blank
     */
    private String safe(String v, String def) {
        if (v == null) return def;
        v = v.trim();
        return v.isEmpty() ? def : v;
    }

    /**
     * Returns a safe player name by falling back to a default player name.
     * <p>
     * Uses {@link Player#getName()} and applies the same logic as {@link #safe(String, String)}.
     * </p>
     *
     * @param p1  the player whose name should be validated
     * @param def the default player used as fallback
     * @return the validated player name, or the default player's name if empty/null
     */
    private String safeV2(Player p1, Player def) {
        String v = p1.getName();
        if (v == null) return def.getName();
        v = v.trim();
        return v.isEmpty() ? def.getName() : v;
    }

    /**
     * Creates a vertical spacer with a minimum height.
     *
     * @param h the minimum height in pixels
     * @return a {@link VBox} acting as a spacer
     */
    private VBox spacer(double h) {
        VBox v = new VBox();
        v.setMinHeight(h);
        return v;
    }
}
