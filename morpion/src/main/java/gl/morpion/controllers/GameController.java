package gl.morpion.controllers;

import gl.morpion.model.*;
import gl.morpion.vue.GameBoardView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    Game game;
    GameBoardController  gameBoardController;
    GameBoardView gameBoardView;
    public GameController() {

        Symbol s = new Symbol(
                getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                TypeOfSymbol.CROSS
        );
        Player p1=new Player("yassine",s);
        Symbol cercle = new Symbol(
                getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                TypeOfSymbol.CIRCLE
        );
        Player p2=new Player("mehdi",cercle);
        List<Player> players=new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        this.game = new Game(new RectangleBoard(10,10),players);
        this.gameBoardView=new GameBoardView(game.getGameBoard());
        this.gameBoardController=new PvsPController(gameBoardView,(RectangleBoard)game.getGameBoard());
    }
    public void handleGame(){
        Label[][] cells=gameBoardView.getCells();
        for(int i=0;i<this.game.getGameBoard().getRow();i++){
            for(int j=0;j< this.game.getGameBoard().getColumn();j++){
                int finali=i;
                int finalj=j;
                cells[i][j].setOnMouseClicked(event -> {
                    this.game.plaayTurn(finali,finalj);
                    //gameBoardView.dispalytest(finali, finalj);
                    this.gameBoardView.update(this.game.getGameBoard());
                    if(this.game.getFin()==true){
                        //this.gameBoardView.exit();
                        this.popup();

                    }

                });
            }
        }


    }
    public void popup(){
        Platform.runLater(()->{
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Information");
            alert.setContentText("PARTIE FINIE");
            alert.showAndWait();
            Platform.exit();     // ⬅️ Quitte l'application juste après


                }

        );
    }
    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }
}
