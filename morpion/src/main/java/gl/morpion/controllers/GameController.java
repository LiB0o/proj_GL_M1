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

public class GameController {

    private final Game game;
    private final GameBoardView gameBoardView;
    private final PvsPController gameBoardController; // ou GameBoardController si c'est lui
    private volatile boolean ended = false;

    public GameController() {
        // ---- joueurs & symboles ----
        Symbol cross = new Symbol(
                getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(),
                TypeOfSymbol.CROSS
        );
        Symbol circle = new Symbol(
                getClass().getResource("/gl/morpion/cercle.png").toExternalForm(),
                TypeOfSymbol.CIRCLE
        );
        Player p1 = new Player("yassine", cross);
        Player p2 = new Player("mehdi", circle);

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        // ---- plateau & vue ----
        GameBoard board = new RectangleBoard(10, 10); // ajuste la taille si tu veux
        this.game = new Game(board, players);

        this.gameBoardView = new GameBoardView(game.getGameBoard());

        // Si ton contrôleur de plateau est PvsPController :
        this.gameBoardController = new PvsPController(gameBoardView, (RectangleBoard) game.getGameBoard());
        // Sinon : remplace par ton controller réel.
    }

    /** Branchement des clics + callback de fin (retour au menu) */
    public void handleGame(Runnable onFinish) {
        Label[][] cells = gameBoardView.getCells();
        int rows = game.getGameBoard().getRow();
        int cols = game.getGameBoard().getColumn();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final int r = i, c = j;
                cells[i][j].setOnMouseClicked(event -> {
                    if (ended) return; // si déjà fini, ignorer
                    game.plaayTurn(r, c);              // <-- ton API actuelle (orthographe assumée)
                    gameBoardView.update(game.getGameBoard());

                    if (game.getFin()) {
                        ended = true;
                        showEndPopup(onFinish);
                    }
                });
            }
        }
    }

    private void showEndPopup(Runnable onFinish) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partie terminée");
            alert.setHeaderText("Information");
            alert.setContentText("PARTIE FINIE");
            alert.showAndWait();
            if (onFinish != null) onFinish.run();
        });
    }

    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }
}
