package gl.morpion.controllers;

import gl.morpion.model.GameBoard;
import gl.morpion.model.RectangleBoard;
import gl.morpion.vue.GameBoardView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    public void handle(){
        Label[][] cells=gameBoardView.getCells();
        for(int i=0;i<gameBoard.getRow();i++){
            for(int j=0;j<gameBoard.getColumn();j++){
                int finali=i;
                int finalj=j;
                cells[i][j].setOnMouseClicked(event -> {

                    gameBoardView.dispalytest(finali, finalj);
                });
            }
        }
    }
    /*public void setGameBoardView(GameBoard gameBoard){

    }*/




}
