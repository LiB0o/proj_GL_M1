package gl.morpion.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>class RectangleBoard</h1>
 * <h2>Elements of RectangleBoard</h2>
 */

public class RectangleBoard extends GameBoard {

	//private int DEFAULT_COLUMN = 30;
	//private int DEFAULT_ROW = 30;

	public static final int DEFAULT_ROW = 10, DEFAULT_COLUMN = 10;

	/**
	 * <h2>Functions of RectangleBoard</h2>
	 */


	public int getDEFAULT_COLUMN() {
		return this.DEFAULT_COLUMN;
	}

	public int getDEFAULT_ROW() {
		return this.DEFAULT_ROW;
	}

	/**
	 * <h3>RectangleBoard</h3>
	 * Constructor of RectangleBoard, call its parent "GameBoard"
	 * @param x rows
	 * @param y columns
	 */
	public RectangleBoard(int x, int y) {
        super(x, y);

		super.setUseCase(setListRectangle(x,y));
	}


	/**
	 * <h3>setListRectangle</h3>
	 * Generate the list of cases the user can play on
	 * @param row
	 * @param column
	 * @return the list of cases the user can play on
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