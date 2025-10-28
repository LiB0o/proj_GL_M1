package gl.morpion.model;


public class Player {

    private Symbol symbol;
    private String name;
    private int points;
    private boolean turn;

    /**
     * Gets the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the player.
     *
     * @param name the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the current score of the player.
     *
     * @return the player's score
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Sets the score of the player.
     *
     * @param points the new score
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
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
     * Adds a point to the player.
     * Call this method when the player wins a game.
     */
    public void addPoint() {
        this.points = this.points + 1;
        System.out.println("Player " + this.name + " scored a point");
    }

    /**
     * Allows the player to take their turn.
     */
    public void playTurn() {
        this.turn = true;
    }

    /**
     * Forces the player to wait for their next turn.
     */
    public void waitTurn() {
        this.turn = false;
    }

    /**
     * Checks if it is the player's turn.
     *
     * @return true if it is the player's turn, false otherwise
     */
    public boolean isTurn() {
        return this.turn;
    }
    public Symbol getSymbol() {
        return this.symbol;
    }

    /**
     * Gets the player's symbol.
     *
     * @return the player's symbol
     */
    public Symbol getSymbol() {
        return this.symbol;
    }

    /**
     * Sets the player's symbol.
     *
     * @param symbol the new symbol
     */
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}
