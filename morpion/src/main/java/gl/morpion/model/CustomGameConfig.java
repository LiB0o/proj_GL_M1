package gl.morpion.model;

/**
 * Immutable configuration object for the Custom game mode.
 * <p>
 * This class stores all parameters required to create a game instance with a
 * user-defined board (size/shape), win condition, and player setup (PvP or PvBot).
 * </p>
 */
public class CustomGameConfig {

    /** Number of rows for the custom board. */
    private final int rows;

    /** Number of columns for the custom board. */
    private final int cols;

    /**
     * Board shape identifier (e.g., "RECTANGLE" or "SQUARE").
     * <p>
     * This value is typically used by the UI/controller layer to decide which
     * {@link GameBoard} implementation to create.
     * </p>
     */
    private final String shape;

    /** Number of aligned symbols required to win the game. */
    private final int winCondition;

    /** Whether the game is played against a bot (true) or against another player (false). */
    private final boolean vsBot;

    /** Display name for player 1. */
    private final String player1Name;

    /**
     * Display name for player 2, or the bot name when {@code vsBot == true}.
     */
    private final String player2Name;

    /**
     * Creates a new custom game configuration.
     *
     * @param rows         the number of rows for the board
     * @param cols         the number of columns for the board
     * @param shape        the board shape identifier (e.g., "RECTANGLE" or "SQUARE")
     * @param winCondition the number of aligned symbols needed to win
     * @param vsBot        {@code true} to play versus a bot, {@code false} for player versus player
     * @param player1Name  the display name of the first player
     * @param player2Name  the display name of the second player (or bot name when vsBot is true)
     */
    public CustomGameConfig(int rows,
                            int cols,
                            String shape,
                            int winCondition,
                            boolean vsBot,
                            String player1Name,
                            String player2Name) {
        this.rows = rows;
        this.cols = cols;
        this.shape = shape;
        this.winCondition = winCondition;
        this.vsBot = vsBot;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    /**
     * Returns the configured number of rows.
     *
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the configured number of columns.
     *
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns the configured board shape identifier.
     *
     * @return the board shape (e.g., "RECTANGLE" or "SQUARE")
     */
    public String getShape() {
        return shape;
    }

    /**
     * Returns the configured win condition.
     *
     * @return the number of aligned symbols needed to win
     */
    public int getWinCondition() {
        return winCondition;
    }

    /**
     * Indicates whether the game is configured to be played against a bot.
     *
     * @return {@code true} if the game is versus a bot, {@code false} otherwise
     */
    public boolean isVsBot() {
        return vsBot;
    }

    /**
     * Returns the configured name for player 1.
     *
     * @return player 1 name
     */
    public String getPlayer1Name() {
        return player1Name;
    }

    /**
     * Returns the configured name for player 2 (or bot name if vsBot is enabled).
     *
     * @return player 2 name (or bot name)
     */
    public String getPlayer2Name() {
        return player2Name;
    }
}
