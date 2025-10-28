package gl.morpion.model;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 *this classes tests the rectangle board
 */
class RectangleBoardTest {
    GameBoard board = new RectangleBoard(30, 30);

    @Test
    void getColumn() {
        assertEquals(30, board.getColumn());
    }

    @Test
    void getRow() {
        assertEquals(30, board.getRow());
    }

    @Test
    void placeSymbolValidPlacement() {
        Symbol symbol = new Symbol("X", TypeOfSymbol.CROSS);
        boolean result = board.placeSymbol(symbol, 3, 4);
        assertTrue(result);
        assertEquals(symbol, board.getSymbolInCase(3, 4));
        assertFalse(board.isEmptyCase(3, 4));
    }

    @Test
    void placeSymbolInCaseAlreadyOccupied(){
        Symbol symbolX = new Symbol("X", TypeOfSymbol.CROSS);
        Symbol symbolO = new Symbol("O", TypeOfSymbol.CIRCLE);

        board.placeSymbol(symbolX, 3, 4);
        assertFalse(board.placeSymbol(symbolO, 3, 4));
    }

    @Test
    void isValidAllCase(){
        for(int i = 0; i < board.getRow(); i++){
            for(int j = 0; j < board.getColumn(); j++){
                assertTrue(board.isValidCase(i, j));
            }
        }
    }

    @Test
    void isValidCase(){
        Pair<Integer, Integer> pair1 = new Pair<>(3, 29);
        Pair<Integer, Integer> pair2 = new Pair<>(30, 30);
        boolean result1 = board.isValidCase(pair1.getKey(), pair1.getValue());
        boolean result2 = board.isValidCase(pair2.getKey(), pair2.getValue());
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    void getSymbolInCase() {
        Symbol symbol = new Symbol("X", TypeOfSymbol.CROSS);
        board.placeSymbol(symbol, 2, 3);

        assertEquals(board.getSymbolInCase(2, 3), symbol);
        assertEquals(TypeOfSymbol.CROSS, board.getSymbolInCase(2, 3).typeOfSymbol);
    }

    @Test
    void isAllEmptyCase() {
        for(int i = 0; i < board.getRow(); i++){
            for(int j = 0; j < board.getColumn(); j++){
                assertTrue(board.isEmptyCase(i, j));
            }
        }
    }

    @Test
    void isEmptyCase(){
        Symbol symbol = new Symbol("X", TypeOfSymbol.CROSS);
        board.placeSymbol(symbol, 2, 3);
        boolean test = board.isEmptyCase(2, 3);
        assertFalse(test);
    }
    @Test
    void getPair(){
        Pair<Integer, Integer> pair  = board.getPair(2, 3);
        assertEquals(pair.getKey(), 2);
        assertEquals(pair.getValue(), 3);
    }

    void placeSymbols(){
        this.board.placeSymbol(new Symbol("X", TypeOfSymbol.CROSS), 2, 3);
        this.board.placeSymbol(new Symbol("X", TypeOfSymbol.CROSS), 1, 2);
        this.board.placeSymbol(new Symbol("X", TypeOfSymbol.CROSS), 4, 3);
        this.board.placeSymbol(new Symbol("0", TypeOfSymbol.CIRCLE), 1, 3);
        this.board.placeSymbol(new Symbol("0", TypeOfSymbol.CIRCLE), 0, 0);

    }

    @Test
    void getSizeSymbolInPair(){
        placeSymbols();
        HashMap<Pair<Integer, Integer>, Symbol> result = this.board.getSymbolInPair();
        assertFalse(result.isEmpty());
        assertEquals(result.size(), 30*30);
    }
    @Test
    void getSymbolInPair() {
        //place symbols
        placeSymbols();
        HashMap<Pair<Integer, Integer>, Symbol> result = this.board.getSymbolInPair();

        assertEquals(board.getSymbolInCase(2, 3), result.get(board.getPair(2, 3)));
        assertNull(result.get(board.getPair(3,4)));
        assertNotNull(result.get(board.getPair(0, 0)));
    }
}