package gl.morpion.controllers;

import gl.morpion.model.RectangleBoard;
import gl.morpion.view.GameBoardView;
import javafx.scene.control.Label;
/**
 * <h1>Controller PvsPController</h1>
 * Controller for Player vs Player game mode.
 * Handles game logic and UI interactions for two human players
 * competing against each other on a rectangular board.
 */
public class PvsPController extends GameBoardController
{
    /**
     * The rectangular game board
     */
    RectangleBoard gameBoard;

    /**
     * The game board view for UI rendering
     */
    GameBoardView gameBoardView;

    /**
     * <h3>PvsPController</h3>
     * Creates a new PvP controller with specified view and board.
     *
     * @param gameBoardView the view component for rendering the game board
     * @param gameBoard the rectangular board model
     */
    public PvsPController(GameBoardView gameBoardView, RectangleBoard gameBoard)
    {
        super();
        this.gameBoard=gameBoard;
        this.gameBoardView=gameBoardView;
    }

    /**
     * <h3>setupUndo</h3>
     * Configures the undo button for Player vs Player mode.
     * When clicked, undoes the last move and restores the previous player's turn.
     * Updates the view to reflect the restored game state.
     *
     * @param game the game instance to perform undo operations on
     */
    public void setupUndo(gl.morpion.model.Game game) {
        gameBoardView.getUndoButton().setOnAction(event -> {
            if (game.canUndo()) {
                game.undo(); // Automatically restores the player who had played
                gameBoardView.update(game.getGameBoard(), game.getCurrentPlayer().getSymbol());
                gameBoardView.setActivePlayer(game.getCurrentPlayer());
            }
        });
    }

}
