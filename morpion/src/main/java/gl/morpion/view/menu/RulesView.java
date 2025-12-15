package gl.morpion.view.menu;

import gl.morpion.audio.SoundFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * View displaying the game rules.
 * <p>
 * This screen shows a scrollable rules card containing:
 * </p>
 * <ul>
 *     <li>Basic rules</li>
 *     <li>Win conditions</li>
 *     <li>Project strategy notes (useful for the AI later)</li>
 * </ul>
 *
 * <p>
 * The view uses a styled background consistent with the main menu and includes a back button
 * to return to the previous screen.
 * </p>
 */
public class RulesView extends StackPane {

    /**
     * Creates the rules screen.
     *
     * @param onBack callback executed when the user clicks the Back button
     */
    public RulesView(Runnable onBack) {
        setPrefSize(1200, 800);

        // Background (same style as main menu)
        Region bg = new Region();
        bg.getStyleClass().add("main-menu-bg");
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // --------- Title ----------
        Label title = new Label("Game rules");
        title.getStyleClass().add("title-glow");

        // ---------- Text blocks (player-friendly rules) ----------

        String basicText = """
                • The game is played by 2 players.
                • One player uses crosses (X), the other uses circles (O).
                • The board is a rectangular grid of squares.
                • The player with crosses (X) always plays first.
                • Players take turns placing one symbol on an empty square.
                • You are not allowed to play on an occupied square.
                """;

        String winningText = """
                • The first player who aligns 5 of their own symbols in a row
                  wins the game.
                • Alignments can be horizontal, vertical or diagonal.
                • If the board is full and no player has 5 symbols in a row,
                  the game ends in a draw.
                """;

        String strategyText = """
                From the project handout (for the AI later):
                • The board can be analysed in groups of 5 consecutive squares
                  called "quintuplets".
                • A quintuplet with only X or only O is "open" and can still
                  lead to a 5-in-a-row.
                • A quintuplet containing both X and O is "closed" and can
                  never give a win.
                • Strong moves are usually on squares that belong to several
                  promising open quintuplets.
                """;

        // Basic rules section
        Label basicTitle = new Label("Basic rules");
        basicTitle.getStyleClass().add("rules-section-title");

        Text basic = new Text(basicText);
        basic.setWrappingWidth(780);
        basic.setTextAlignment(TextAlignment.LEFT);
        basic.getStyleClass().add("rules-text");

        // Winning rules section
        Label winTitle = new Label("Winning the game");
        winTitle.getStyleClass().add("rules-section-title");

        Text winning = new Text(winningText);
        winning.setWrappingWidth(780);
        winning.setTextAlignment(TextAlignment.LEFT);
        winning.getStyleClass().add("rules-text");

        // Strategy notes section
        Label stratTitle = new Label("From the project strategy");
        stratTitle.getStyleClass().add("rules-section-title");

        Text strategy = new Text(strategyText);
        strategy.setWrappingWidth(780);
        strategy.setTextAlignment(TextAlignment.LEFT);
        strategy.getStyleClass().add("rules-text");

        // Card containing all sections
        VBox card = new VBox(18,
                title,
                basicTitle, basic,
                winTitle, winning,
                stratTitle, strategy
        );
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(30));
        card.getStyleClass().add("rules-card");

        VBox wrapper = new VBox(card);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(40, 40, 40, 40));

        // Scroll (in case the window is smaller)
        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        // Keep the background transparent (avoid a white background)
        scroll.setStyle("-fx-background-color: transparent;");

        // Back button (top-left)
        Button back = new Button("← Back");
        back.getStyleClass().add("pill-button");
        back.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });
        SoundFX.attachReturn(back);

        BorderPane layout = new BorderPane();
        layout.setTop(back);
        BorderPane.setAlignment(back, Pos.TOP_LEFT);
        BorderPane.setMargin(back, new Insets(16, 5, 0, 5));
        layout.setCenter(scroll);
        // Keep layout background transparent to show the main-menu-bg behind it
        layout.setStyle("-fx-background-color: transparent;");

        getChildren().addAll(bg, layout);
    }
}
