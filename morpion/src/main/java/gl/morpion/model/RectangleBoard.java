package gl.morpion.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>class RectangleBoard</h1>
 * Represents a rectangular game board for tic-tac-toe.
 * Extends GameBoard to provide a standard rectangular grid layout
 * where all cells are playable.
 * <h2>Elements of RectangleBoard</h2>
 */
public class RectangleBoard extends GameBoard {

	/**
	 * Default number of rows for a standard board
	 */
	public static final int DEFAULT_ROW = 10;

	/**
	 * Default number of columns for a standard board
	 */
	public static final int DEFAULT_COLUMN = 10;

	/**
	 * <h2>Functions of RectangleBoard</h2>
	 */

	/**
	 * Gets the default number of columns.
	 *
	 * @return the default column count
	 */
	public int getDEFAULT_COLUMN() {
		return this.DEFAULT_COLUMN;
	}

	/**
	 * Gets the default number of rows.
	 *
	 * @return the default row count
	 */
	public int getDEFAULT_ROW() {
		return this.DEFAULT_ROW;
	}

	/**
	 * <h3>RectangleBoard</h3>
	 * Creates a rectangular board with specified dimensions.
	 * All cells in the rectangle are playable.
	 *
	 * @param x number of rows
	 * @param y number of columns
	 */
	public RectangleBoard(int x, int y) {
        super(x, y);

		super.setUseCase(setListRectangle(x,y));
	}


	/**
	 * <h3>setListRectangle</h3>
	 * Generates a list of all playable positions in the rectangular grid.
	 * Creates pairs for every cell in the row x column grid.
	 *
	 * @param row number of rows in the board
	 * @param column number of columns in the board
	 * @return list of coordinate pairs representing all playable positions
	 */
	public List<Pair<Integer, Integer>> setListRectangle(int row, int column){
		List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();

		for(int i =0;i<row;i++){
			for(int j=0;j<column;j++){
				list.add(new Pair<>(i,j));
			}
		}
		return list;
	}


}