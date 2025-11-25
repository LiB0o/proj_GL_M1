package gl.morpion.persistence;

public class CellData {
    private int row;
    private int col;
    private String symbol;

    @Override
    public String toString() {
        return "CellData[row=" + row + ", col=" + col + ", symbol=" + symbol + "]";
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

