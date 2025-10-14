package gl.morpion.controllers;

import gl.morpion.model.GameBoard;
import gl.morpion.vue.GameBoardView;

public class GameBoardController {
    GameBoard gameBoard;
    GameBoardView view;

    public GameBoardController(){}

    public GameBoard getGameBoard() {
        return gameBoard;
    }
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
    public GameBoardView getView() {
        return view;
    }
    public void setView(GameBoardView view) {
        this.view = view;
    }
}
