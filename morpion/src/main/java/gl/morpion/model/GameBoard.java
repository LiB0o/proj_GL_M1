package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

/**
 * <h1>class abstract GameBoard</h1>
 * <h2>Elements of GameBoard</h2>
 *
 * This class is used to represent a game board. There can be different shapes of boards (square/rectangle, circle, triangle, etc.).
 */
public abstract class GameBoard {

	/**
	 * <h3>symbols</h3>
	 * The board with its symbols
	 */
	public List<Symbol[]> symbols;
	/**
	 *<h3>private column</h3>
	 * Number of columns on the board
	 */
	private final int column;
	/**
	 *<h3>private row</h3>
	 * Number of rows on the board
	 */
	private final int row;
	/**
	 *<h3>useCase</h3>
	 * List of the positions where a symbol is
	 */
	public List<Pair<Integer, Integer>> useCase;

	/**
	 * <h2>Functions of GameBoard</h2>
	 */

	/*
	 * <h3>getSymbolAt</h3>
	 *
	 * @param x row
	 * @param y column
	 * @return the symbol at the given position (Symbol or null)
	 *
	public Symbol getSymbolAt(int x, int y) {
        return symbols.get(x)[y];
    }*/

	public int getColumn() {
		return this.column;
	}

	public int getRow() {
		return this.row;
	}


	/**
	 * <h3>placeSymbol</h3>
	 *  Place a symbol inside the List<Symbol[]> symbols
	 *
	 * @param symbol : the symbol that must be placed
	 * @param x : line of the board
	 * @param y : column of the board
	 * @return true if the symbol is successfully placed
	 */
	public boolean placeSymbol(Symbol symbol, int x, int y) {

		boolean result = false;

		if(this.isValidCase(x,y) && this.isEmptyCase(x,y)){
			this.symbols.get(x)[y] = symbol;
			result = true;
		}

		return result;
	}

	/**
	 * <h3>isValidCase</h3>
	 *
	 * Determine whether a case is valid or not
	 *
	 * @param x : the row coordinate
	 * @param y : the column coordinate
	 * @return true if the case (x, y) is empty
	 */
	public boolean isValidCase(int x, int y) {
		Pair<Integer, Integer> test = new Pair<>(x,y);
		return this.useCase.contains(test);
	}

	/**
	 * <h3>getSymbolInCase</h3>
	 *
	 * @param x row
	 * @param y column
	 * @return the symbol at the given position (Symbol or null)
	 */
	public Symbol getSymbolInCase(int x, int y) {
		return this.symbols.get(x)[y];
	}

	/**
	 * <h3>GameBoard</h3>
	 * Constructor of GameBoard, cannot be used without children
	 * @param column : number of column
	 * @param row : number of line
	 */
	public GameBoard(int row,int column) {

		this.row = row;
		this.column = column;

		List<Symbol[]> tab = new ArrayList<>();
		for(int i=0;i<row;i++){
			tab.add(new Symbol[column]);
		}
		this.symbols = tab;
	}

	/**
	 * <h3>protected setUseCase</h3>
	 * Will be call inside the children constructor
	 * Permit to set where the player will be able to play
	 * @param listUse : list of case where the player can play (given by the children)
	 */
	protected void setUseCase(List<Pair<Integer, Integer>> listUse){
		this.useCase = listUse;
	}

	/**
	 * <h3>isEmptyCase</h3>
	 * Return if the place selected is empty
	 *
	 * @param x : the row coordinate
	 * @param y : the column coordinate
	 * @return : return true if a case is empty
	 */
	public boolean isEmptyCase(int x, int y) {

		Symbol[] line = this.symbols.get(x);
        return (line[y] == null);
	}

	/**
	 * <h3>getSymbolInPair</h3>
	 * @return a map with positions(x,y) as keys and Symbol/null as a values
	 */
	public HashMap<Pair<Integer, Integer>, Symbol> getSymbolInPair(){
		HashMap<Pair<Integer, Integer>, Symbol> result = new HashMap<>();
		for(int i = 0; i < row; i++){
			for(int j = 0; j < column; j++){
				if(this.isValidCase(i, j)){
					result.put(new Pair<>(i, j), this.getSymbolInCase(i, j));
				}
			}
		}
		return result;
	}

	/**
	 * <h3>getPair</h3>
	 *
	 * Generate a Pair(Integer, Integer)
	 * @param x first value
	 * @param y second value
	 * @return the generated Pair
	 */
	public Pair<Integer, Integer> getPair(int x, int y){
		return new Pair<>(x, y);
	}

	//------------------debug-------------------//
	public String debugSymbol(Symbol symbol){
		if(symbol != null){
			if(symbol.getTypeOfSymbol() == TypeOfSymbol.CROSS){
				return "CROSS";
			}else if(symbol.getTypeOfSymbol() == TypeOfSymbol.CIRCLE){
				return "CIRCLE";
			}
		}
		return "NONE";
	}


	public void debugGameBoard() {
		int rows = this.getRow();
		int cols = this.getColumn();
		for(int i= 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				System.out.print("("+i+", "+j+" - "+ this.debugSymbol(this.getSymbolInCase(i, j))+")");
			}
			System.out.println();
		}
	}
}