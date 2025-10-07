package gl.morpion.model;

public class Player {

	private Symbol symbol;
	private string name;
	private int points;

	public string getName() {
		return this.name;
	}

	public void setName(string name) {
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
	public Player(string name, int point) {
		// TODO - implement Player.Player
		throw new UnsupportedOperationException();
	}

}