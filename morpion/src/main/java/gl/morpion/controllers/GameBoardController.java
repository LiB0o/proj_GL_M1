package gl.morpion.controllers;

import gl.morpion.model.GameBoard;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.view.GameBoardView;

/**
 * Basic controller for managing the game board model and its associated view.
 * Acts as a bridge between the GameBoard model and GameBoardView.
 * Provides getter and setter methods for accessing and modifying board components.
 */
public class GameBoardController {
    // The game board model containing the game logic and state
    GameBoard gameBoard;
    
    // The visual representation of the game board (JavaFX UI component)
    GameBoardView view;

    /**
     * Default constructor.
     * Creates an empty GameBoardController without initializing board or view.
     */
    public GameBoardController(){}

    /**
     * Returns the current game board model.
     * 
     * @return The GameBoard instance managed by this controller
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    
    /**
     * Sets the game board model to be managed by this controller.
     * 
     * @param gameBoard The GameBoard instance to associate with this controller
     */
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
    
    /**
     * Returns the current game board view.
     * 
     * @return The GameBoardView instance managed by this controller
     */
    public GameBoardView getView() {
        return view;
    }
    
    /**
     * Sets the game board view to be managed by this controller.
     * 
     * @param view The GameBoardView instance to associate with this controller
     */
    public void setView(GameBoardView view) {
        this.view = view;
    }


}
