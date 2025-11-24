package gl.morpion.model;

/**
 * <h1>class Player</h1>
 * <h2>Element of Player</h2>
 */

public class Player {

    /**
     * <h3>private symbol</h3>
     * Symbol assign to the player
     */
    private Symbol symbol;
    /**
     * <h3>private name</h3>
     * Name of the player
     */
    private String name;
    /**
     * <h3>points</h3>
     * Point the player get when they get an alignment (is initialised at 0)
     */
    private int points;
    /**
     * <h3>private turn</h3>
     * If it is the player to play
     */
    private boolean turn;

    /**
     * <h2>Functions of Player</h2>
     */


    /**
     * <h3>getName</h3>
     *
     * @return the player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * <h3>setName</h3>
     * Sets the name of the player.
     * @param name the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <h3>getPoints</h3>
     *
     * @return the player's score
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * <h3>setPoints</h3>
     *
     * Sets the score of the player.
     * @param points the new score
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * <h3>Player</h3>
     * Constructor with name, points, and symbol.
     *
     * @param name   the name of the player
     * @param points the initial points of the player
     * @param symbol the player's symbol
     */
    public Player(String name, int points, Symbol symbol) {
        this.name = name;
        this.points = points;
        this.symbol = symbol;
    }

    /**
     * <h3>Player</h3>
     * Constructor with name and symbol.
     * The player's initial score will be 0.
     *
     * @param name   the name of the player
     * @param symbol the player's symbol
     */
    public Player(String name, Symbol symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * <h3>addPoint</h3>
     * Adds a point to the player.
     */
    public void addPoint() {
        this.points = this.points + 1;
        System.out.println("Player " + this.name + " scored a point");
    }

    /**
     * <h3>playTurn</h3>
     * Allows the player to take their turn.
     */
    public void playTurn() {
        this.turn = true;
    }

    /**
     * <h3>waitTurn</h3>
     * Forces the player to wait for their next turn.
     */
    public void waitTurn() {
        this.turn = false;
    }

    /**
     * <h3>isTurn</h3>
     * Checks if it is the player's turn.
     *
     * @return true if it is the player's turn, false otherwise
     */
    public boolean isTurn() {
        return this.turn;
    }

    /**
     * <h3>getSymbol</h3>
     * @return the symbol assign to the player
     */
    public Symbol getSymbol() {
        return this.symbol;
    }

    /**
     * <h3>setSymbol</h3>
     * Sets the player's symbol.
     *
     * @param symbol the new symbol
     */
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}
