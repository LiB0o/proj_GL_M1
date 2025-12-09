package gl.morpion.persistence;

/**
 * Represents a single cell's data in the game board.
 * 
 * <p>This class is used for JSON serialization/deserialization to save and load
 * the state of individual cells on the game board. Each cell contains:
 * <ul>
 *   <li>Row and column indices (position on the board)</li>
 *   <li>Symbol information (filename like "croix.jpg", "cercle.png", or "None" for empty cells)</li>
 * </ul>
 * 
 * <p>This is a simple data transfer object (DTO) used by {@link SaveBoard} and {@link LoadBoard}
 * to persist the game state.
 * 
 * @author GL M1 Project Team
 * @version 1.0
 * @see GameData
 * @see SaveBoard
 * @see LoadBoard
 */
public class CellData {
    /** The row index of the cell (0-based) */
    private int row;
    
    /** The column index of the cell (0-based) */
    private int col;
    
    /** The symbol filename ("croix.jpg", "cercle.png") or "None" if the cell is empty */
    private String symbol;

    /**
     * Returns a string representation of this cell data.
     * 
     * @return A string in the format "CellData[row=X, col=Y, symbol=Z]"
     */
    @Override
    public String toString() {
        return "CellData[row=" + row + ", col=" + col + ", symbol=" + symbol + "]";
    }

    /**
     * Gets the row index of this cell.
     * 
     * @return The row index (0-based)
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row index of this cell.
     * 
     * @param row The row index (0-based)
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the column index of this cell.
     * 
     * @return The column index (0-based)
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the column index of this cell.
     * 
     * @param col The column index (0-based)
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Gets the symbol filename for this cell.
     * 
     * @return The symbol filename ("croix.jpg", "cercle.png") or "None" if the cell is empty
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol filename for this cell.
     * 
     * @param symbol The symbol filename ("croix.jpg", "cercle.png") or "None" if the cell is empty
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

