package gl.morpion.controllers;

import gl.morpion.model.*;
import gl.morpion.view.GameBoardView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Main controller for the morpion game.
 * Coordinates the game logic between model (Game, GameBoard, Player) and view (GameBoardView).
 * Handles player interactions and manages game flow from start to finish.
 */
public class GameController {
    /**
     * Core game logic model that manages turns, rules, and win conditions
     */
    private Game game;
    /**
     * Visual representation of the game board (JavaFX UI component)
     */
    private final GameBoardView gameBoardView;
    //private final int DEFAULT_ROW = 10, DEFAULT_COL = 10; //TODO:  probleme si ROW defferent COL
    // Secondary controller for board interactions (Player vs Player mode)
    private final PvsPController gameBoardController; // ou GameBoardController si c'est lui
    // Flag to prevent further moves after game ends (volatile for thread-safety)
    private volatile boolean ended = false;
    private GameBoard board;




    /**
     * Constructor: Initializes the complete game setup including players, symbols, board, and views.
     * @param player1Name : player1's name
     * @param player2Name: player2's name
     */
    public GameController(Player player1Name, Player player2Name) {
        initializeGame(player1Name, player2Name);
        // Create the visual representation of the game board
        this.gameBoardView = new GameBoardView(game.getGameBoard(), player1Name, player2Name);
        // Initialize the board controller to handle cell interactions
        this.gameBoardController = new PvsPController(gameBoardView, (RectangleBoard) game.getGameBoard());
        this.board.debugGameBoard();

    }

    /**
     * initialize the game by creating the board, the players list and the current game
     *

     */
    public void initializeGame(Player p1, Player p2){
        //Player p1 = new Player(player1Name, 0, new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(), TypeOfSymbol.CROSS));
        //Player p2 = new Player(player2Name, 0, new Symbol(getClass().getResource("/gl/morpion/cercle.png").toExternalForm(), TypeOfSymbol.CIRCLE));
        board = new RectangleBoard(RectangleBoard.DEFAULT_ROW, RectangleBoard.DEFAULT_COLUMN);
        //create the game with the board, the players and the current player
        game = new Game(board, p1, p2, p1);
        this.game.addPlayer(p1);
        this.game.addPlayer(p2);
    }

    /** 
     * Sets up click event handlers for all board cells and manages the game loop.
     * Branchement des clics + callback de fin (retour au menu) 
     * 
     * @param onFinish: Callback function executed when game ends (typically returns to main menu)
     */
    public void handleGame(Runnable onFinish) {
        // Get all cell labels from the visual board
        Label[][] cells = gameBoardView.getCells();
        // Get board dimensions (rows and columns)
        int rows = game.getGameBoard().getRow();
        int cols = game.getGameBoard().getColumn();
        // Loop through all cells to attach click handlers
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Capture row and column indices for lambda expression
                final int r = i, c = j;
                // Attach mouse click event listener to each cell
                cells[i][j].setOnMouseClicked(event -> {
                    // Ignore clicks if game has already ended
                    if (ended) return; // si déjà fini, ignorer
                    // Execute the current player's turn at position (r, c)
                    game.playTurn(r, c);// <-- ton API actuelle (orthographe assumée)
                    // Update the visual board to reflect the new game state
                    gameBoardView.update(game.getGameBoard(), game.getCurrentPlayer().getSymbol());
                    System.out.println("theEnd = "+this.game.getEnd());
                    if (game.getEnd()) {
                        // Display end game popup and execute finish callback
                        // Set flag to prevent further moves
                        ended = true;
                        showEndPopup(onFinish, this.game.getCurrentPlayer(), ended);
                    }
                    if(!game.getEnd() && game.allCaseFilled()){
                        showEndPopup(onFinish, this.game.getCurrentPlayer(), ended);

                    }
                    //swap player trick
                    this.game.swap();
                    this.gameBoardView.setActivePlayer(this.game.getCurrentPlayer());// active the current player
                });
            }
        }
    }

    /**
     * Displays an information popup when the game ends and executes the finish callback.
     * Runs on JavaFX Application Thread to ensure proper UI updates.
     * 
     * @param onFinish Callback to execute after user closes the popup
     */
    private void showEndPopup(Runnable onFinish, Player currentPlayer, Boolean ended) {
        // Ensure this runs on the JavaFX Application Thread for UI safety
        Platform.runLater(() -> {
            // Create an information alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Informations".toUpperCase());
            if(ended){
                alert.setContentText("Victory detection : "+
                        currentPlayer.getName()+"("+currentPlayer.getSymbol().getTypeOfSymbol()+
                        ") has won \uD83C\uDFC6".toUpperCase());
            }else {
                alert.setContentText("Victory detection : "+
                        "no winner, match ends in a draw \uD83E\uDD1D".toUpperCase());
            }
            //apply a styles css
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/alert-style.css").toExternalForm()
            );

            // Display the alert and wait for user to close it
            alert.showAndWait();
            // Execute the finish callback if provided (e.g., return to menu)
            if (onFinish != null) onFinish.run();
        });
    }


    /**
     * Returns the game board view component.
     * Used to access the visual board for displaying in JavaFX scenes.
     * 
     * @return The GameBoardView instance containing the visual representation of the board
     */
    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }


    /**
     * @return : the game instance
     */
    public Game getGame() {
        return game;
    }


}
