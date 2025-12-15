package gl.morpion.view;

import com.google.gson.stream.JsonWriter;
import gl.morpion.model.GameBoard;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.persistence.SaveBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * JavaFX view representing the game board UI.
 * <p>
 * This class is responsible for rendering:
 * </p>
 * <ul>
 *     <li>The grid of cells (labels) used to display placed symbols</li>
 *     <li>A top player panel showing player names, avatars and current active player indicator</li>
 *     <li>An "Undo" button placed at the bottom of the view</li>
 * </ul>
 *
 * <p>
 * Side effects:
 * </p>
 * <ul>
 *     <li>Updates the UI graphics of the grid cells when {@link #update(GameBoard, Symbol)} is called</li>
 *     <li>Delegates saving to {@link SaveBoard} through {@link #save()} / {@link #save(gl.morpion.model.Game)}</li>
 * </ul>
 */
public class GameBoardView extends BorderPane {
    private int x;
    private int y;
    private Label[][] cells;
    private GridPane grid;
    private ImageView imageView;

    private HBox hBox = new HBox(100);
    private VBox vBox1 = new VBox(10);
    private VBox vBox2 = new VBox(10);

    private Player player1, player2;
    Polygon triangleVisible1, triangleVisible2;
    private GameBoard gameBoard;
    private SaveBoard save;
    private Button undoButton;

    /**
     * Creates a new {@link GameBoardView} for a given board and players.
     * <p>
     * The constructor initializes a {@link GridPane} made of {@link Label} cells
     * and sets up the top player panel and the bottom Undo button.
     * </p>
     *
     * @param gameBoard the model board used to get size and symbols
     * @param player1   the first player (typically "X")
     * @param player2   the second player (typically "O")
     */
    public GameBoardView(GameBoard gameBoard, Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameBoard = gameBoard;
        this.save = new SaveBoard(this);
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

        // Create the Undo button and add it to the bottom
        this.undoButton = new Button("Undo");
        this.undoButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        HBox bottomBox = new HBox(undoButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        this.setBottom(bottomBox);
    }

    /**
     * Saves the current board state using {@link SaveBoard}.
     * <p>
     * Side effects: writes a save file to disk through {@link SaveBoard}.
     * </p>
     */
    public void save() {
        this.save.saveBoard();
    }

    /**
     * Saves the provided game state using {@link SaveBoard}.
     * <p>
     * Side effects: writes a save file to disk through {@link SaveBoard}.
     * </p>
     *
     * @param game the current game instance containing players, board, and turn info
     */
    public void save(gl.morpion.model.Game game) {
        this.save.saveBoard(game);
    }

    /**
     * Builds the top player panel, displaying avatars, active player indicator,
     * player names, and player symbol icons.
     *
     * @param player1 the first player
     * @param player2 the second player
     * @return an {@link HBox} containing the player panels
     */
    private HBox createPlayerPanel(Player player1, Player player2) {
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

        return hBox;
    }

    /**
     * Creates the small triangle indicator showing which player is currently active.
     *
     * @param isVisible whether the triangle is initially visible
     * @return a {@link Polygon} triangle indicator
     */
    private Polygon createTriangleActivePlayer(Boolean isVisible) {
        Polygon triangleVisible = new Polygon();
        triangleVisible.getPoints().addAll(
                0.0, 0.0,   // left point
                20.0, 0.0,  // right point
                10.0, 10.0  // bottom point
        );
        triangleVisible.setVisible(isVisible);
        return triangleVisible;
    }

    /**
     * Creates a styled label for a player name.
     *
     * @param playerName the player name to display
     * @return a {@link Label} styled for the player panel
     */
    private Label createLabelName(String playerName) {
        Label labelPlayerName = new Label(playerName);
        labelPlayerName.setStyle("-fx-text-fill: WHITE; -fx-font-size: 20px");
        return labelPlayerName;
    }

    /**
     * Updates which player is currently active in the UI.
     * <p>
     * Side effects: changes visibility of active-player indicators.
     * </p>
     *
     * @param current the current player
     */
    public void setActivePlayer(Player current) {
        this.triangleVisible1.setVisible(current == player1);
        this.triangleVisible2.setVisible(current == player2);
    }

    /**
     * Creates a user avatar UI element made of a circular background and an image clipped in a circle.
     *
     * @param imageUser classpath resource path of the avatar image
     * @return a {@link StackPane} containing the avatar graphics
     */
    private StackPane createUserAvatar(String imageUser) {
        Circle circle = new Circle(30, Color.LIGHTGRAY);
        circle.setStroke(Color.GRAY);

        Image img = new Image(getClass().getResource(imageUser).toString());
        ImageView view = new ImageView(img);
        view.setFitWidth(40);
        view.setFitHeight(40);
        view.setClip(new Circle(20, 20, 20)); // round mask

        StackPane avatar = new StackPane(circle, view);
        return avatar;
    }

    /**
     * Updates the board view by synchronizing the grid graphics with the model.
     * <p>
     * For each non-empty cell, an {@link ImageView} is created from the stored {@link Symbol}
     * and displayed in the corresponding label.
     * </p>
     *
     * @param gameBoard the current board model to render
     * @param symbol    a symbol reference (unused as input; updated inside the method)
     */
    public void update(GameBoard gameBoard, Symbol symbol) {
        for (int row = 0; row < gameBoard.getRow(); row++) {
            for (int col = 0; col < gameBoard.getColumn(); col++) {
                //Symbol symbol = gameBoard.getSymbolInCase(row, col);
                //if empty case, it show symbol in the gameBoard
                if (!gameBoard.isEmptyCase(row, col)/*symbol != null*/) {
                    symbol = gameBoard.getSymbolInCase(row, col);
                    this.imageView = symbolView(symbol);
                    cells[row][col].setGraphic(imageView);
                } else {
                    cells[row][col].setGraphic(null);
                }
            }
        }
        this.gameBoard.debugGameBoard();
    }

    /**
     * Builds an {@link ImageView} for a given game {@link Symbol}.
     *
     * @param symbol the symbol to render (must provide an image URL/path)
     * @return an {@link ImageView} configured for a grid cell
     */
    public ImageView symbolView(Symbol symbol) {
        ImageView img = new ImageView(new Image(symbol.getImage()));
        img.setFitWidth(40);
        img.setFitHeight(40);
        img.setPreserveRatio(true);
        return img;
    }

    /**
     * Builds a smaller {@link ImageView} for a symbol, used in the player panel.
     *
     * @param symbol the symbol to render
     * @return a smaller {@link ImageView} for the top player panel
     */
    private ImageView symbolViewInPanel(Symbol symbol) {
        ImageView img = symbolView(symbol);
        img.setFitWidth(30);
        img.setFitHeight(30);
        return img;
    }

    /**
     * Returns the grid cells used to display symbols.
     *
     * @return a 2D array of {@link Label} representing the board cells
     */
    public Label[][] getCells() {
        return cells;
    }

    /**
     * Returns player 1.
     *
     * @return player 1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Sets player 1.
     *
     * @param player1 the new player 1 instance
     */
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    /**
     * Returns player 2.
     *
     * @return player 2
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Sets player 2.
     *
     * @param player2 the new player 2 instance
     */
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    /**
     * Returns the current game board model associated with this view.
     *
     * @return the {@link GameBoard}
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Sets the game board model associated with this view.
     *
     * @param gameBoard the new {@link GameBoard}
     */
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Returns the last {@link ImageView} used to render a symbol in a cell.
     *
     * @return the last created {@link ImageView}
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Returns the Undo button created by this view.
     * <p>
     * This is typically used by the controller to attach undo logic.
     * </p>
     *
     * @return the Undo {@link Button}
     */
    public Button getUndoButton() {
        return undoButton;
    }

}
