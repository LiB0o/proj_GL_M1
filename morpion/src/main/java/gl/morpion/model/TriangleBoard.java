package gl.morpion.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TriangleBoard extends GameBoard {


	/**
	 * @param size
	 */
	public TriangleBoard(int size) {
        super(size, size);
        super.setUseCase(this.setListTriangle(size));
	}

	private List<Pair<Integer, Integer>> setListTriangle(int size){
		List<Pair<Integer, Integer>> list = new ArrayList<>();
		for(int i = 0; i < size; i++){
			for(int j = 0; j <= i; j++){
				list.add(new Pair<>(i, j));
			}
		}
		return list;
	}

}