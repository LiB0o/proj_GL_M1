package gl.morpion.model;

public class Player {

	private Symbol symbol;
	private String name;
	private int points;
    private boolean turn;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Constructor of Player
	 * @param name
	 * @param point
	 */
    public Player(String name, int point, Symbol symbol) {
        this.name=name;
        this.points=point;
        this.symbol=symbol;
        this.turn = false;
    }
    public Player(String name, Symbol symbol) {
        this.name=name;
        this.symbol=symbol;
        this.turn = false;
    }

    /**
     * Add a point after a victory
     */

    public void addPoint() {
        this.points = this.points +1;
        System.out.println("Le joueur "+this.name+" a marqué un point ");
    }

    /**
     * Set the value that permit the player to play to true
     */
    public void playTurn() {
        this.turn = true;
    }

    /**
     * Set the value that permit the player to play to false
     */
    public void waitTurn() {
        this.turn = false;
    }

    /**
     * Return the value that permit/stop the player to play
     * @return boolean
     */
    public boolean isTurn() {
        return this.turn;
    }
    public Symbol getSymbol() {
        return this.symbol;
    }

}