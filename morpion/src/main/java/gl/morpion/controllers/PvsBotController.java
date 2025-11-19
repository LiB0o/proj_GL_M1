package gl.morpion.controllers;

import gl.morpion.model.BotPlayer;
import gl.morpion.model.Game;
import gl.morpion.model.GameBoard;
import gl.morpion.model.Player;
import gl.morpion.view.GameBoardView;
import gl.morpion.model.*;
import javafx.scene.control.Label;
import javafx.util.Pair;

/**
 * Contrôleur pour le mode Player vs Bot.
 * - Le joueur humain joue manuellement (clic sur la grille).
 * - Le bot joue automatiquement juste après.
 * - La vue est mise à jour après chaque tour.
 */
public class PvsBotController {

    private final Game game;
    private final GameBoardView boardView;
    private final Player human;
    private final BotPlayer bot;

    // Pour bloquer les clics après la fin de partie
    private volatile boolean ended = false;

    /**
     * @param game      instance de Game (avec board + players déjà ajoutés)
     * @param boardView vue de la grille
     * @param human     joueur humain
     * @param bot       joueur bot
     */
    public PvsBotController(Game game, GameBoardView boardView, Player human, BotPlayer bot) {
        this.game = game;
        this.boardView = boardView;
        this.human = human;
        this.bot = bot;

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
                    // Si la partie est finie → on ignore
                    if (ended) return;

                    // On ne laisse cliquer que le joueur humain
                    if (game.getCurrentPlayer() != human) return;

                    // Case déjà occupée → on ne fait rien
                    if (!board.isEmptyCase(r, c)) return;

                    // 🔹 Coup du joueur humain via la logique de Game
                    boolean played = game.playTurn(r, c);
                    if (!played) return; // (sécurité, normalement on est déjà sûr)

                    // On informe le bot que le joueur a joué ici
                    bot.symbolPutByPlayer(board.getPair(r, c));

                    // Mise à jour de la vue
                    boardView.update(game.getGameBoard(), human.getSymbol());

                    // Vérifier fin de partie (victoire / nul)
                    if (checkGameEnd()) return;

                    // 🔹 Passage au bot
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

        // Choix du coup via l'IA du bot
        Pair<Integer, Integer> move = bot.getMaxValue();
        if (move == null) return;

        int row = move.getKey();
        int col = move.getValue();

        // Sécurité : si la case n'est pas jouable, on prend la première case vide valide
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
                // Plus aucun coup possible
                if (checkGameEnd()) return;
                return;
            }
        }

        // 🔹 Coup du bot via Game
        boolean played = game.playTurn(row, col);
        if (!played) return;

        // On informe le bot qu'il vient de jouer là (pour son heuristique)
        bot.symbolPutByBot(board.getPair(row, col));

        // Mise à jour de la vue
        boardView.update(game.getGameBoard(), bot.getSymbol());

        // Vérifier fin de partie (victoire / nul)
        if (checkGameEnd()) return;

        // 🔹 Retour au joueur humain
        game.setCurrentPlayer(human);
        boardView.setActivePlayer(human);
    }

    /**
     * Vérifie si la partie est terminée après un coup (victoire ou match nul).
     * Utilise l’API réelle de ta classe Game :
     *  - game.getEnd() pour savoir s’il y a une victoire
     *  - game.allCaseFilled() pour savoir s’il y a match nul
     *
     * @return true si la partie est finie, false sinon
     */
    private boolean checkGameEnd() {
        // Victoire détectée par Game.playTurn()
        if (game.getEnd()) {
            ended = true;
            Player winner = game.getCurrentPlayer();
            if (winner != null) {
                System.out.println("🏆 " + winner.getName()
                        + " (" + winner.getSymbol().getTypeOfSymbol() + ") wins!");
            } else {
                System.out.println("🏆 Someone wins (currentPlayer null ?)"); // cas théorique
            }
            return true;
        }

        // Pas de victoire, mais toutes les cases sont remplies → match nul
        if (!game.getEnd() && game.allCaseFilled()) {
            ended = true;
            System.out.println("🤝 Draw! (board full, no winner)");
            return true;
        }

        return false;
    }
}
