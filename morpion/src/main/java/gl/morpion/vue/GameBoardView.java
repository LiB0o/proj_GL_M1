package gl.morpion.vue;

import gl.morpion.controllers.GameBoardController;
import gl.morpion.model.GameBoard;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import javafx.application.Platform;
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
    public void update(GameBoard gameBoard) {
        for (int row = 0; row < gameBoard.getRow(); row++) {
            for (int col = 0; col < gameBoard.getColumn(); col++) {
                Symbol symbol = gameBoard.getSymbolAt(row, col);

                if (symbol != null) {
                    ImageView img = new ImageView(symbol.getImage());
                    img.setFitWidth(40);
                    img.setFitHeight(40);
                    img.setPreserveRatio(true);
                    cells[row][col].setGraphic(img);
                } else {
                    cells[row][col].setGraphic(null);
                }
            }
        }
    }

    public void dispalytest(int x, int y) {

        Image image = new Image(getClass().getResource("/gl/morpion/croix.jpg").toExternalForm());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        cells[x][y].setGraphic(imageView);
    }
    public void exit() {
        Platform.exit();
    }

    public Label getLabel(int x,int y) {
        return cells[x][y];
    }
    public Label[][] getCells() {
        return cells;
    }

}
