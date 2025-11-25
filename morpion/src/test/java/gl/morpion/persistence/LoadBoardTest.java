package gl.morpion.persistence;

import gl.morpion.model.GameBoard;
import gl.morpion.model.RectangleBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class LoadBoardTest {
    GameBoard board;
    @BeforeEach
    void setUp() {
        board = new RectangleBoard(10, 10);

    }

    @Test
    void readJson() {
        LoadBoard loadBoard = new LoadBoard(board);
        loadBoard.readJson("/save.json");
    }
}