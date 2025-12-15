package gl.morpion.model;

/**
 * Represents a reversible move for undo functionality.
 * Stores complete game state before a move is played, allowing
 * the game to be restored to its exact previous state.
 */
public class Move {

    /**
     * Row coordinate of the move
     */
    private final int x;

    /**
     * Column coordinate of the move
     */
    private final int y;

    /**
     * The player who was active before this move
     */
    private final Player playerBefore;

    /**
     * Whether the game was ended before this move
     */
    private final boolean endBefore;

    /**
     * Player 1 points before this move
     */
    private final int p1PointsBefore;

    /**
     * Player 2 points before this move
     */
    private final int p2PointsBefore;

    /**
     * Creates a new Move snapshot capturing game state before a move.
     *
     * @param x row coordinate where the move will be played
     * @param y column coordinate where the move will be played
     * @param playerBefore the player who is about to play this move
     * @param endBefore whether the game was ended before this move
     * @param p1PointsBefore player 1 score before this move
     * @param p2PointsBefore player 2 score before this move
     */
    public Move(int x, int y,
                Player playerBefore,
                boolean endBefore,
                int p1PointsBefore,
                int p2PointsBefore) {
        this.x = x;
        this.y = y;
        this.playerBefore = playerBefore;
        this.endBefore = endBefore;
        this.p1PointsBefore = p1PointsBefore;
        this.p2PointsBefore = p2PointsBefore;
    }

    /**
     * Gets the row coordinate of this move.
     *
     * @return the row coordinate
     */
    public int getX() { return x; }

    /**
     * Gets the column coordinate of this move.
     *
     * @return the column coordinate
     */
    public int getY() { return y; }

    /**
     * Gets the player who was active before this move.
     *
     * @return the player before this move
     */
    public Player getPlayerBefore() { return playerBefore; }

    /**
     * Checks whether the game was ended before this move.
     *
     * @return true if game was ended, false otherwise
     */
    public boolean isEndBefore() { return endBefore; }

    /**
     * Gets player 1 score before this move.
     *
     * @return player 1 points
     */
    public int getP1PointsBefore() { return p1PointsBefore; }

    /**
     * Gets player 2 score before this move.
     *
     * @return player 2 points
     */
    public int getP2PointsBefore() { return p2PointsBefore; }
}
