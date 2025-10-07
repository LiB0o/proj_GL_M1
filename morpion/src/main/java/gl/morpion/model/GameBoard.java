package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

/**
 * This class is used to represent a game board. There can be different shapes of boards (square/rectangle, circle, triangle, etc.).
 */
public abstract class GameBoard {

	List<List<Symbol>> symbols;
	private int column;
	private int row;
	private List<Pair<Integer, Integer>> useCase;

	public int getColumn() {
		return this.column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return this.row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	/**
	 *  
	 *  
	 * @param symbol
	 * @param x
	 * @param y
	 */
	protected boolean placeSymbol(Symbol symbol, int x, int y) {
		return true;
	}

	/**
	 * determine whether a box is valid or not
	 * @param x
	 * @param y
	 */
	protected boolean isValidCase(int x, int y) {
		return true;
	}

	/**
	 * allows you to get a symbol in a given box.
	 * @param row
	 * @param col
	 */
	protected Symbol getSymbolInCase(int row, int col) {
		return null;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public GameBoard(int x, int y) {

	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	protected boolean isEmptyCase(int x, int y) {
		return true; // a supprimer
	}

}