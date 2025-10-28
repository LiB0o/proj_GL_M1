package gl.morpion.model;

public class CircleBoard extends GameBoard {

	private double radius;

	public double getRadius() {
		return this.radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * 
	 * @param radius
	 */
	public CircleBoard(int radius) {
        super(2*radius, 2*radius);
        // TODO - implement CircleBoard.CircleBoard
		throw new UnsupportedOperationException();
	}

}