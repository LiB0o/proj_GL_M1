package gl.morpion.controllers;

import gl.morpion.model.BotPlayer;
import gl.morpion.model.Game;
import gl.morpion.model.GameBoard;
import gl.morpion.model.Player;
import gl.morpion.view.GameBoardView;
import javafx.scene.control.Label;
import javafx.util.Pair;

/**
 * <h1>controller PvsBotController</h1>
 *
 * Manage a game against a humain and a bot
 * <h2>Elements of PvsBotController</h2>
 */

public class PvsBotController {

    /**
     * <h3>private gameController</h3>
     * The GameController used
     */
    private final GameController gameController;
    /**
     * <h3>game</h3>
     * The Game
     */
    private final Game game;
    /**
     * <h3>boardView</h3>
     * The board for the game
     */
    private final GameBoardView boardView;
    /**
     * <h3>private human</h3>
     * The human player
     */
    private final Player human;
    /**
     * <h3>private bot</h3>
     * The bot player
     */
    private final BotPlayer bot;
    /**
     * <h3>private onFinish</h3>
     */
    private final Runnable onFinish;

    /**
     * <h3>private ended</h3>
     * To block action after the end of the game
     */
    private volatile boolean ended = false;

    /**
     * <h3>PvsBotController</h3>
     *
     * constructor of the controller
     * @param gameController main controller (reused in showEndPopup)
     * @param game           instance of Game
     * @param boardView      view of the board
     * @param human          human player
     * @param bot            bot
     * @param onFinish       callback (ex: return to menu)
     */
    public PvsBotController(GameController gameController,
                            Game game,
                            GameBoardView boardView,
                            Player human,
                            BotPlayer bot,
                            Runnable onFinish) {
        this.gameController = gameController;
        this.game = game;
        this.boardView = boardView;
        this.human = human;
        this.bot = bot;
        this.onFinish = onFinish;

        initCellEvents();
        boardView.setActivePlayer(human);
    }

    /**
     * <h3>private initCellEvents</h3>
     *
     * Initialise clicks of human player on the board
     */
    private void initCellEvents() {
        Label[][] cells = boardView.getCells();
        GameBoard board = game.getGameBoard();

        int rows = board.getRow();
        int cols = board.getColumn();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final int r = row;
                final int c = col;

                cells[r][c].setOnMouseClicked(event -> {
                    if (ended) return;

                    if (game.getCurrentPlayer() != human) return;

                    if (!board.isEmptyCase(r, c)) return;

                    boolean played = game.playTurn(r, c);
                    if (!played) return;

                    bot.symbolPutByPlayer(board.getPair(r, c));

                    boardView.update(game.getGameBoard(), human.getSymbol());

                    if (checkGameEnd()) return;

                    game.setCurrentPlayer(bot);
                    boardView.setActivePlayer(bot);

                    botPlay();
                });
            }
        }
    }

    /**
     * <h3>private botPlay</h3>
     *
     * Bot choose and play its move
     */
    private void botPlay() {
        if (ended) return;

        GameBoard board = game.getGameBoard();

        Pair<Integer, Integer> move = bot.getMaxValue();
        if (move == null) return;

        int row = move.getKey();
        int col = move.getValue();

        if (!board.isValidCase(row, col) || !board.isEmptyCase(row, col)) {
            boolean found = false;
            for (int i = 0; i < board.getRow() && !found; i++) {
                for (int j = 0; j < board.getColumn() && !found; j++) {
                    if (board.isValidCase(i, j) && board.isEmptyCase(i, j)) {
                        row = i;
                        col = j;
                        found = true;
                    }
                }
            }
            if (!found) {
                if (checkGameEnd()) return;
                return;
            }
        }

        boolean played = game.playTurn(row, col);
        if (!played) return;

        bot.symbolPutByBot(board.getPair(row, col));

        boardView.update(game.getGameBoard(), bot.getSymbol());

        if (checkGameEnd()) return;

        game.setCurrentPlayer(human);
        boardView.setActivePlayer(human);
    }

    /**
     * <h3>private checkGameEnd</h3>
     *
     * Check if the game is over after a move (win/draw)
     * call a pop-up already in GameController.
     */
    private boolean checkGameEnd() {
        if (game.getEnd()) {
            ended = true;
            Player winner = game.getCurrentPlayer();
            // true = victoire (comme dans handleGame)
            gameController.showEndPopup(onFinish, winner, true);
            return true;
        }

        if (!game.getEnd() && game.allCaseFilled()) {
            ended = true;
            // false = match nul
            gameController.showEndPopup(onFinish, game.getCurrentPlayer(), false);
            return true;
        }

        return false;
    }
}
