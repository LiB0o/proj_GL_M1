package gl.morpion.model;

public class CustomGameConfig {
    private final int rows;
    private final int cols;
    private final String shape;       // "RECTANGLE" ou "SQUARE"
    private final int winCondition;
    private final boolean vsBot;
    private final String player1Name;
    private final String player2Name; // pour vsBot = nom du bot

    public CustomGameConfig(int rows,
                            int cols,
                            String shape,
                            int winCondition,
                            boolean vsBot,
                            String player1Name,
                            String player2Name) {
        this.rows = rows;
        this.cols = cols;
        this.shape = shape;
        this.winCondition = winCondition;
        this.vsBot = vsBot;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String getShape() {
        return shape;
    }

    public int getWinCondition() {
        return winCondition;
    }

    public boolean isVsBot() {
        return vsBot;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }
}
