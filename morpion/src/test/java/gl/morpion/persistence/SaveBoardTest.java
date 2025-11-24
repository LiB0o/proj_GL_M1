package gl.morpion.persistence;

import com.google.gson.stream.JsonWriter;
import gl.morpion.model.*;
import gl.morpion.view.GameBoardView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SaveBoardTest {
    GameBoard board;
    Player player1;
    Player player2;

    SaveBoard saveBoard;

    @BeforeEach
    void setUp() {
        player1 = new Player("p1", new Symbol("croix", TypeOfSymbol.CROSS));
        player2 = new Player("p2", new Symbol("cercle", TypeOfSymbol.CROSS));
        board = new RectangleBoard(5, 5);
        board.placeSymbol(new Symbol("croix", TypeOfSymbol.CROSS), 0, 0);
        board.placeSymbol(new Symbol("croix", TypeOfSymbol.CROSS), 0, 1);
        this.saveBoard = new SaveBoard(board);
    }

    @Test
    void writeBoard() {
        try {
            JsonWriter writer = new JsonWriter(new FileWriter("testBoard.json"));
            writer.setIndent("  ");
           // saveBoard.writeBoard(writer, null);
            writer.close();
            System.out.println("JSON write in testBoard.json !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}