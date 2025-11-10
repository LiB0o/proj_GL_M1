package gl.morpion.controllers;

import gl.morpion.model.RectangleBoard;
import gl.morpion.view.GameBoardView;
import javafx.scene.control.Label;

public class PvsPController extends GameBoardController
{
    RectangleBoard gameBoard;
    GameBoardView gameBoardView;

    public PvsPController(GameBoardView gameBoardView,RectangleBoard gameBoard)
    {
        super();
        this.gameBoard=gameBoard;

        this.gameBoardView=gameBoardView;

    }


}
