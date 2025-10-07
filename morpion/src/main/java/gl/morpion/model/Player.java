package gl.morpion.model;

public class Player {

	private Symbol symbol;
	private String name;
	private int points;

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
	public Player(String name, int point) {
		// TODO - implement Player.Player
		throw new UnsupportedOperationException();
	}

}