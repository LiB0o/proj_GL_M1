package gl.morpion.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RectangleBoard extends GameBoard {

	private int DEFAULT_COLUMN = 30;
	private int DEFAULT_ROW = 30;

	public int getDEFAULT_COLUMN() {
		return this.DEFAULT_COLUMN;
	}

	public int getDEFAULT_ROW() {
		return this.DEFAULT_ROW;
	}

	/**
	 *
	 * @param x
	 * @param y
	 */
	public RectangleBoard(int x, int y) {
        super(x, y);

		super.setUseCase(setListRectangle(x,y));
	}


	/**
	 * Generate the list of cases the user can play on
	 * @param row
	 * @param column
	 * @return
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