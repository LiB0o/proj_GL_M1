package gl.morpion.model;

/**
 * A {@link GameBoard} implementation representing a circular board.
 * <p>
 * This board is currently defined by a radius value and is constructed on top of a
 * rectangular grid sized {@code 2 * radius} by {@code 2 * radius}.
 * </p>
 */
public class CircleBoard extends GameBoard {

    /** The radius of the circular board (in grid units). */
    private double radius;

    /**
     * Returns the radius of this circular board.
     *
     * @return the current radius
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Sets the radius of this circular board.
     *
     * @param radius the new radius value
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Creates a circular board backed by a {@code 2 * radius} by {@code 2 * radius} grid.
     * <p>
     * Note: this constructor is not implemented yet and will throw an exception.
     * </p>
     *
     * @param radius the radius used to size the underlying grid
     * @throws UnsupportedOperationException always, until the implementation is provided
     */
    public CircleBoard(int radius) {
        super(2 * radius, 2 * radius);
        // TODO - implement CircleBoard.CircleBoard
        throw new UnsupportedOperationException();
    }

}
