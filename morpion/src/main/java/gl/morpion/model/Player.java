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
	 * 
	 * @param name
	 * @param point
	 */
    //Constructeurs
    public Player(String name, int point, Symbol symbol) {
        this.name=name;
        this.points=point;
        this.symbol=symbol;
    }
    public Player(String name, Symbol symbol) {
        this.name=name;
        this.symbol=symbol;
    }
    //fonction a appler en cas de victoire
    public void addPoint() {
        this.points = this.points +1;
        System.out.println("Le joueur "+this.name+" a marqué un point ");
    }
    //permet à l'utilisateur de jouer
    public void playTurn() {
        this.turn = true;
    }
    //oblige l'utilisateur à attendre son prochain tour
    public void waitTurn() {
        this.turn = false;
    }


    public boolean isTurn() {
        return this.turn;
    }

}