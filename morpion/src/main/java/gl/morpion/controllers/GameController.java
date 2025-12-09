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
 * <h1>Controller GameController</h1>
 * Main controller for the morpion game.
 * Coordinates the game logic between model (Game, GameBoard, Player) and view (GameBoardView).
 * Handles player interactions and manages game flow from start to finish.
 */

/**
 * <h2>Elements of GameController</h2>
 */
public class GameController {

    /**
     * <h3>private game</h3>
     * Core game logic model that manages turns, rules, and win conditions
     */
    private Game game;
    /**
     * <h3>private GameBoardView</h3>
     * Visual representation of the game board (JavaFX UI component)
     */
    private final GameBoardView gameBoardView;
    //private final int DEFAULT_ROW = 10, DEFAULT_COL = 10; //TODO:  probleme si ROW defferent COL

    /**
     * <h3>private gameBoardController</h3>
     * Secondary controller for board interactions (Player vs Player mode)
     */
    private PvsPController gameBoardController; // ou GameBoardController si c'est lui

    /**
     * <h3>private ended</h3>
     * Flag to prevent further moves after game ends (volatile for thread-safety)
     */
    private volatile boolean ended = false;
    /**
     * <h3>private board</h3>
     * Board use for the game
     */
    private GameBoard board;

    /**
     * <h3>vsBot</h3>
     * Permit to check if there is a bot
     */
    private boolean vsBot = false;

    /**
     * <h3>GameController</h3>
     * Constructor of GameController against a bot
     *
     * @param human the human player
     * @param bot
     * @param vsBot if you are against a bot
     * @param onFinish method to execute in a Thread
     */
    public GameController(Player human, BotPlayer bot, boolean vsBot, Runnable onFinish) {
        this.vsBot = true;

        // utiliser le champ board, pas une variable locale
        this.board = new RectangleBoard(
                RectangleBoard.DEFAULT_ROW,
                RectangleBoard.DEFAULT_COLUMN
        );

        this.game = new Game(board, human, bot, human);
        this.game.addPlayer(human);
        this.game.addPlayer(bot);

        this.gameBoardView = new GameBoardView(board, human, bot);

        // On passe this et onFinish au contrôleur bot
        new PvsBotController(this, game, gameBoardView, human, bot, onFinish);
    }

    // ---------- NOUVEAU : constructeur PVP avec plateau custom ----------
    public GameController(Player player1, Player player2, RectangleBoard customBoard) {
        this.board = customBoard;

        this.game = new Game(customBoard, player1, player2, player1);
        this.game.addPlayer(player1);
        this.game.addPlayer(player2);

        this.gameBoardView = new GameBoardView(customBoard, player1, player2);
        this.gameBoardController = new PvsPController(gameBoardView, customBoard);

        this.board.debugGameBoard();
    }

    // ---------- NOUVEAU : constructeur VS BOT avec plateau custom ----------
    public GameController(Player human, BotPlayer bot, boolean vsBot, Runnable onFinish, RectangleBoard customBoard) {
        this.vsBot = true;
        this.board = customBoard;

        this.game = new Game(customBoard, human, bot, human);
        this.game.addPlayer(human);
        this.game.addPlayer(bot);

        this.gameBoardView = new GameBoardView(customBoard, human, bot);

        // Contrôleur bot avec callback de fin
        new PvsBotController(this, game, gameBoardView, human, bot, onFinish);
    }



    /**
     * <h3>GameController</h3>
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
     * <h3>initializeGame</h3>
     * initialize the game by creating the board, the players list and the current game
     *
     * @param p1 Player 1
     * @param p2 Player 2
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
     * <h3>handleGame</h3>
     *
     * Sets up click event handlers for all board cells and manages the game loop.
     * 
     * @param onFinish: Callback function executed when game ends (typically returns to main menu)
     */
    public void handleGame(Runnable onFinish) {
        // En mode VS BOT, on ne branche pas les clics ici.
        // C'est le contrôleur PvsBotController qui gère la logique des tours.
        if (vsBot) {
            return;
        }

        // Récupère toutes les cases de la vue
        Label[][] cells = gameBoardView.getCells();
        // Dimensions du plateau
        int rows = game.getGameBoard().getRow();
        int cols = game.getGameBoard().getColumn();

        // On attache un handler de clic sur chaque case
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final int r = i;
                final int c = j;

                cells[i][j].setOnMouseClicked(event -> {
                    // Si la partie est déjà finie, on ignore
                    if (ended) return;

                    // On joue le coup
                    boolean played = game.playTurn(r, c);
                    if (!played) {
                        // Coup invalide (case déjà prise, etc.)
                        return;
                    }

                    // On met à jour l'affichage en fonction de l'état du modèle
                    gameBoardView.update(game.getGameBoard(), game.getCurrentPlayer().getSymbol());
                    System.out.println("theEnd = " + this.game.getEnd());

                    // Vérifie victoire
                    if (game.getEnd()) {
                        ended = true;
                        showEndPopup(onFinish, this.game.getCurrentPlayer(), ended);
                        return;
                    }

                    // Vérifie égalité (plateau plein)
                    if (!game.getEnd() && game.allCaseFilled()) {
                        showEndPopup(onFinish, this.game.getCurrentPlayer(), ended);
                        return;
                    }

                    // Sinon, on passe au joueur suivant
                    this.game.swap();
                    this.gameBoardView.setActivePlayer(this.game.getCurrentPlayer());
                });
            }
        }
    }


    /**
     * <h3>showEndPopup</h3>
     * Displays an information popup when the game ends and executes the finish callback.
     * Runs on JavaFX Application Thread to ensure proper UI updates.
     * 
     * @param onFinish Callback to execute after user closes the popup
     */
    public void showEndPopup(Runnable onFinish, Player currentPlayer, Boolean ended) {
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
     * <h3>getGameBoardView</h3>
     * Returns the game board view component.
     * Used to access the visual board for displaying in JavaFX scenes.
     * 
     * @return The GameBoardView instance containing the visual representation of the board
     */
    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }


    /**
     * <h3>getGame</h3>
     * @return : the game instance
     */
    public Game getGame() {
        return game;
    }
    /**
     * Force la vue à se synchroniser avec l'état actuel du modèle (GameBoard).
     * À utiliser après un chargement de partie.
     */
    public void refreshViewFromModel() {
        if (game == null || gameBoardView == null) {
            return;
        }

        // Redessine tout le plateau à partir du GameBoard actuel
        gameBoardView.update(game.getGameBoard(), game.getCurrentPlayer().getSymbol());

        // Met en avant le joueur courant (celui dont c'est le tour)
        gameBoardView.setActivePlayer(game.getCurrentPlayer());
    }




}
