package gl.morpion.persistence;

import gl.morpion.model.GameBoard;

public class LoadBoard {
    private GameBoard gameBoard;

    public LoadBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
}
