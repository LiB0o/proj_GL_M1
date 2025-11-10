package gl.morpion.view;

import gl.morpion.model.GameBoard;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;


public class GameBoardView extends BorderPane {
    private int x;
    private int y;
    private Label[][] cells;
    private GridPane grid;

    private HBox hBox = new HBox(100);
    private VBox vBox1 = new VBox(10);
    private VBox vBox2 = new VBox(10);

    private Player player1, player2;
    Polygon triangleVisible1, triangleVisible2;

    GameBoard gameBoard;

    public GameBoardView(GameBoard gameBoard, Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameBoard = gameBoard;
        this.x = gameBoard.getColumn();
        this.y = gameBoard.getRow();
        this.grid = new GridPane();
        this.cells = new Label[x][y];

        for (int row = 0; row < x; row++) {
            for (int col = 0; col < y; col++) {
                Label cell = new Label(" "); //TODO: it must store the symbol in the Label
                cell.setAlignment(Pos.CENTER);
                cell.setContentDisplay(ContentDisplay.CENTER);
                cell.setMinSize(50, 50);
                cell.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");

                this.cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }
        //style grid
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 0, 0, 0)); // top - right - bottom - left

        HBox hBox = this.createPlayerPanel(this.player1, this.player2);
        this.setTop(hBox);
        this.setCenter(grid);
    }

    private HBox createPlayerPanel(Player player1, Player player2){
        HBox hBox = new HBox(30); // space = 10
        hBox.setAlignment(Pos.CENTER);

        this.vBox1.setAlignment(Pos.CENTER);
        this.vBox2.setAlignment(Pos.CENTER);

        this.triangleVisible1 = createTriangleActivePlayer(true);
        this.triangleVisible2 = createTriangleActivePlayer(false);


        this.vBox1.getChildren().addAll(
                createUserAvatar("/gl/morpion/avatar_player1.jpeg"),
                this.triangleVisible1,
                this.createLabelName(player1.getName()),
                new Label("", this.symbolViewInPanel(this.player1.getSymbol()))
        );
        this.vBox2.getChildren().addAll(
                createUserAvatar("/gl/morpion/avatar_player2.jpeg"),
                this.triangleVisible2,
                this.createLabelName(player2.getName()),
                new Label("", this.symbolViewInPanel(this.player2.getSymbol()))
        );

        hBox.getChildren().addAll(vBox1, vBox2);

        return  hBox;
    }

    private Polygon createTriangleActivePlayer(Boolean isVisible){
        Polygon triangleVisible = new Polygon();
        triangleVisible.getPoints().addAll(
                0.0, 0.0,   // left oint
                20.0, 0.0,  // right point
                10.0, 10.0  // bottom point
        );
        triangleVisible.setVisible(isVisible);
        return triangleVisible;
    }

    private Label createLabelName(String playerName){
        Label labelPlayerName = new Label(playerName);
        labelPlayerName.setStyle("-fx-text-fill: WHITE; -fx-font-size: 20px");
        return labelPlayerName;
    }
    public void setActivePlayer(Player current) {
        this.triangleVisible1.setVisible(current == player1);
        this.triangleVisible2.setVisible(current == player2);
    }

    private StackPane createUserAvatar(String imageUser) {
        Circle circle = new Circle(30, Color.LIGHTGRAY);
        circle.setStroke(Color.GRAY);

        Image img = new Image(getClass().getResource(imageUser).toString());
        ImageView view = new ImageView(img);
        view.setFitWidth(40);
        view.setFitHeight(40);
        view.setClip(new Circle(20, 20, 20)); // masque rond

        StackPane avatar = new StackPane(circle, view);
        return avatar;
    }

    /**
     * @param gameBoard : th current gameBoard
     */
    public void update(GameBoard gameBoard, Symbol symbol) {
        for (int row = 0; row < gameBoard.getRow(); row++) {
            for (int col = 0; col < gameBoard.getColumn(); col++) {
                //Symbol symbol = gameBoard.getSymbolInCase(row, col);
                //if empty case, it show symbol in the gameBoard
                if (!gameBoard.isEmptyCase(row, col)/*symbol != null*/) {
                     symbol = gameBoard.getSymbolInCase(row, col);
                    ImageView img = symbolView(symbol);
                    cells[row][col].setGraphic(img);
                } else {
                    cells[row][col].setGraphic(null);
                }
            }
        }
        this.gameBoard.debugGameBoard();
    }

    private ImageView symbolView(Symbol symbol){
        ImageView img = new ImageView(symbol.getImage());
        img.setFitWidth(40);
        img.setFitHeight(40);
        img.setPreserveRatio(true);
        return img;
    }
    private ImageView symbolViewInPanel(Symbol symbol){
        ImageView img = symbolView(symbol);
        img.setFitWidth(30);
        img.setFitHeight(30);
        return img;
    }

    public Label[][] getCells() {
        return cells;
    }

}
