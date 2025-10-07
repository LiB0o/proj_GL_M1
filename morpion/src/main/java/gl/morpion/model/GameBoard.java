package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

/**
 * This class is used to represent a game board. There can be different shapes of boards (square/rectangle, circle, triangle, etc.).
 */
public abstract class GameBoard {

	public List<Symbol[]> symbols;
	private int column;
	private int row;
	public List<Pair<Integer, Integer>> useCase;

	/**
	 * Get the number of column of the Board
	 * @return int
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 *
	 * @param column
	 */
	public void setColumn(int column) {
		this.column = column; //Maybe useless
	}

	/**
	 * Get the number of row of the Board
	 * @return int
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 *
	 * @param row
	 */
	public void setRow(int row) {
		this.row = row; //Maybe useless
	}

	/**
	 *  Place a symbol inside the List<Symbol[]> symbols
	 *  Return a true if the symbol was inserted
	 *
	 * @param symbol
	 * @param x : line of the board
	 * @param y : column of the board
	 */
	protected boolean placeSymbol(Symbol symbol, int x, int y) {

		boolean result = false;

		if(this.isValidCase(x,y) && this.isEmptyCase(x,y)){
			this.symbols.get(x)[y] = symbol;
			result = true;
		}

		return result;
	}

	/**
	 * Determine whether a box is valid or not
	 * @param x
	 * @param y
	 */
	protected boolean isValidCase(int x, int y) {
		Pair<Integer, Integer> test = new Pair<>(x,y);
		return this.useCase.contains(test);
	}

	/**
	 * Allows you to get a symbol in a given box.
	 * @param row
	 * @param col
	 */
	protected Symbol getSymbolInCase(int row, int col) {
		return this.symbols.get(row)[col];
	}

	/**
	 * Constructor of GameBoard, cannot be used without children
	 * @param column
	 * @param row
	 */
	public GameBoard(int row,int column) {

		this.row = row;
		this.column = column;

		List<Symbol[]> tab = new ArrayList<Symbol[]>();
		for(int i=0;i<row;i++){
			tab.add(new Symbol[column]);
		}
		this.symbols = tab;
	}

	/**
	 * Set useCase (will be call inside the children constructor)
	 * @param listUse : list of case where the player can play (given by the children)
	 */
	protected void setUseCase(List<Pair<Integer, Integer>> listUse){
		this.useCase = listUse;
	}

	/**
	 * Return if the place selected is empty
	 * For test purpose, will print the state of the case
	 * @param x : row
	 * @param y : column
	 */
	protected boolean isEmptyCase(int x, int y) {

		Symbol[] line = this.symbols.get(x);
		System.out.println("GameBoard isEmptyCase :"+line[y]); //test

        return (line[y] == null);
	}

}