package gl.morpion.persistence;

import gl.morpion.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class LoadBoardTest {
    private Game game;
    private GameBoard board;
    private Player p1;
    private Player p2;

    @BeforeEach
    void setUp() {
        board = new RectangleBoard(10, 10);
        p1 = new Player("P1", new Symbol("croix", TypeOfSymbol.CROSS));
        p2 = new Player("p2", new Symbol("cercle", TypeOfSymbol.CROSS));
        game = new Game(board, p1, p2, p1);

    }

    @Test
    void readJson() {
        LoadBoard loadBoard = new LoadBoard(game);
        loadBoard.readJsonFromFile();
    }
}