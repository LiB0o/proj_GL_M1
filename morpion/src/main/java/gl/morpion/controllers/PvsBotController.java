package gl.morpion.controllers;

import gl.morpion.model.BotPlayer;
import gl.morpion.model.Game;
import gl.morpion.model.GameBoard;
import gl.morpion.model.Player;
import gl.morpion.view.GameBoardView;
import javafx.scene.control.Label;
import javafx.util.Pair;

public class PvsBotController {

    private final GameController gameController;
    private final Game game;
    private final GameBoardView boardView;
    private final Player human;
    private final BotPlayer bot;
    private final Runnable onFinish;

    // Pour bloquer les clics après la fin de partie
    private volatile boolean ended = false;

    /**
     * @param gameController contrôleur principal (pour réutiliser showEndPopup)
     * @param game           instance de Game
     * @param boardView      vue de la grille
     * @param human          joueur humain
     * @param bot            bot
     * @param onFinish       callback (ex: retour au menu)
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
     * Initialise les clics du joueur humain sur la grille.
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
     * Le bot choisit et joue automatiquement un coup.
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
     * Vérifie si la partie est terminée après un coup (victoire ou match nul)
     * et appelle le popup déjà présent dans GameController.
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
