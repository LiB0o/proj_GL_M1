package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

/**
 * This class is used to represent a game board. There can be different shapes of boards (square/rectangle, circle, triangle, etc.).
 */
public abstract class GameBoard {

	public List<Symbol[]> symbols;//la grille
	private final int column;
	private final int row;
	public List<Pair<Integer, Integer>> useCase;

    public Symbol getSymbolAt(int x, int y) {
        return symbols.get(x)[y];
    }
	/**
	 * Get the number of column of the Board
	 * @return int
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * Get the number of row of the Board
	 * @return int
	 */
	public int getRow() {
		return this.row;
	}


	/**
	 *  Place a symbol inside the List<Symbol[]> symbols
	 *  Return a true if the symbol was inserted
	 *
	 * @param symbol : the symbol that must be placed
	 * @param x : line of the board
	 * @param y : column of the board
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
	 * Determine whether a box is valid or not
	 * @param x : the x coordinate
	 * @param y : the y coordinate
	 * @return : if the case (x, y) is empty
	 */
	public boolean isValidCase(int x, int y) {
		Pair<Integer, Integer> test = new Pair<>(x,y);
		return this.useCase.contains(test);
	}

	/**
	 * Allows you to get a symbol in a given box.
	 * @param x : the x coordinate
	 * @param y : the y coordinate
	 */
	public Symbol getSymbolInCase(int x, int y) {
		return this.symbols.get(x)[y];
	}

	/**
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
	 * Set useCase (will be call inside the children constructor)
	 * @param listUse : list of case where the player can play (given by the children)
	 */
	protected void setUseCase(List<Pair<Integer, Integer>> listUse){
		this.useCase = listUse;
	}

	/**
	 * Return if the place selected is empty
	 * For test purpose, will print the state of the case
	 * @param x : the x coordinate
	 * @param y : the y coordinate
	 * @return : return true if a case is empty
	 */
	public boolean isEmptyCase(int x, int y) {

		Symbol[] line = this.symbols.get(x);
		System.out.println("GameBoard isEmptyCase :"+line[y]); //test

        return (line[y] == null);
	}

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

	public Pair<Integer, Integer> getPair(int x, int y){
		return new Pair<>(x, y);
	}
}