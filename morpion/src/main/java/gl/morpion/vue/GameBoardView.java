package gl.morpion.vue;

import gl.morpion.controllers.GameBoardController;
import gl.morpion.model.GameBoard;
import gl.morpion.model.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class GameBoardView extends Pane {
    private int x;
    private int y;
    private Label[][] cells;
    private GridPane grid;

    public GameBoardView(GameBoard gameBoard) {

        this.x = gameBoard.getColumn();
        this.y = gameBoard.getRow();
        this.grid = new GridPane();
        this.cells = new Label[x][y];

        for (int row = 0; row < x; row++) {
            for (int col = 0; col < y; col++) {
                Label cell = new Label(" ");
                cell.setAlignment(Pos.CENTER);
                cell.setMinSize(50, 50);
                cell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");


                this.cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        getChildren().add(grid);
    }
    public void displayPlayer(Player player, int x, int y){

        ImageView imageView2 = new ImageView(player.getSymbol().getImage());
        imageView2.setFitWidth(40);
        imageView2.setFitHeight(40);
        imageView2.setPreserveRatio(true);

        cells[x][y].setGraphic(imageView2);

    }
    public void dispalytest(int x, int y) {

        Image image = new Image(getClass().getResource("/gl/morpion/croix.jpg").toExternalForm());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        cells[x][y].setGraphic(imageView);
    }
    public Label getLabel(int x,int y) {
        return cells[x][y];
    }
    public Label[][] getCells() {
        return cells;
    }

}
