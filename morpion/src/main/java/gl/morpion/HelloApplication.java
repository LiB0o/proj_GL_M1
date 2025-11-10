package gl.morpion;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        /*RectangleBoard gameBoard=new RectangleBoard(10,10);

        GameBoardView gameBoardView= new GameBoardView(gameBoard);

        PvsPController controller= new PvsPController(gameBoardView,gameBoard);
        controller.handle();*/

        /*Symbol s = new Symbol(
                getClass().getResource("croix.jpg").toExternalForm(),
                TypeOfSymbol.CROSS
        );
        Symbol cercle = new Symbol(
                getClass().getResource("cercle.png").toExternalForm(),
                TypeOfSymbol.CIRCLE
        );
        Player p1 = new Player("yassine",0,s);
        Player p2 = new Player("lison",0,cercle);

        gameBoardView.displayPlayer(p1,0,0);
        gameBoardView.displayPlayer(p2,5,8);*/


        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        /*GameController gameController = new GameController();
        gameController.handleGame();
        Scene scene = new Scene(gameController.getGameBoardView(), 800, 800);

        //Scene scene = new Scene(gameBoardView, 800, 800);


        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/
    }

    public static void main(String[] args) {
        launch();
    }
}

//Force Modif