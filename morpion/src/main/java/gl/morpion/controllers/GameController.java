package gl.morpion.controllers;

import gl.morpion.model.Game;
import gl.morpion.model.GameBoard;
import gl.morpion.model.RectangleBoard;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.vue.GameBoardView;
import gl.morpion.controllers.PvsPController; // si c'est ton contrôleur plateau
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Main controller for the Tic-Tac-Toe game.
 * Coordinates the game logic between model (Game, GameBoard, Player) and view (GameBoardView).
 * Handles player interactions and manages game flow from start to finish.
 */
public class GameController {

    // Core game logic model that manages turns, rules, and win conditions
    private final Game game;
    
    // Visual representation of the game board (JavaFX UI component)
    private final GameBoardView gameBoardView;
    
    // Secondary controller for board interactions (Player vs Player mode)
    private final PvsPController gameBoardController; // ou GameBoardController si c'est lui
    
    // Flag to prevent further moves after game ends (volatile for thread-safety)
    private volatile boolean ended = false;

    /**
     * Constructor: Initializes the complete game setup including players, symbols, board, and views.
     */
    public GameController() {
        // ---- joueurs & symboles ----
        
        // Create cross symbol for player 1 with image resource
        Symbol cross = new Symbol(
                getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                TypeOfSymbol.CROSS
        );
        
        // Create circle symbol for player 2 with image resource
        Symbol circle = new Symbol(
                getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                TypeOfSymbol.CIRCLE
        );
        
        // Initialize player 1 with name "yassine" and cross symbol
        Player p1 = new Player("yassine", cross);
        
        // Initialize player 2 with name "mehdi" and circle symbol
        Player p2 = new Player("mehdi", circle);

        // Create list to hold both players
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        // ---- plateau & vue ----
        
        // Create a 10x10 rectangular game board (adjust size as needed)
        GameBoard board = new RectangleBoard(10, 10); // ajuste la taille si tu veux
        
        // Initialize the game model with the board and player list
        this.game = new Game(board, players);

        // Create the visual representation of the game board
        this.gameBoardView = new GameBoardView(game.getGameBoard());

        // Si ton contrôleur de plateau est PvsPController :
        // Initialize the board controller to handle cell interactions
        this.gameBoardController = new PvsPController(gameBoardView, (RectangleBoard) game.getGameBoard());
        // Sinon : remplace par ton controller réel.
    }

    /** 
     * Sets up click event handlers for all board cells and manages the game loop.
     * Branchement des clics + callback de fin (retour au menu) 
     * 
     * @param onFinish Callback function executed when game ends (typically returns to main menu)
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
                    game.plaayTurn(r, c);              // <-- ton API actuelle (orthographe assumée)
                    
                    // Update the visual board to reflect the new game state
                    gameBoardView.update(game.getGameBoard());

                    // Check if the game has ended (win or draw condition met)
                    if (game.getFin()) {
                        // Set flag to prevent further moves
                        ended = true;
                        
                        // Display end game popup and execute finish callback
                        showEndPopup(onFinish);
                    }
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
    private void showEndPopup(Runnable onFinish) {
        // Ensure this runs on the JavaFX Application Thread for UI safety
        Platform.runLater(() -> {
            // Create an information alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partie terminée");
            alert.setHeaderText("Information");
            alert.setContentText("PARTIE FINIE");
            
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
}
