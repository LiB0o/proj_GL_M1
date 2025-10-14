package gl.morpion.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriangleBoardTest {
    private GameBoard board;

    private void placeSymbol(GameBoard gameBoard, int x, int y, Symbol symbol){
        boolean success = gameBoard.placeSymbol(symbol, x, y);
        assertTrue(success, "failed to place symbol in the case ("+x+","+y+")");
    }

    @BeforeEach
    void setUp() {
        board = new TriangleBoard(30);
    }

    @Test
    void getColumn() {
        assertEquals(30, board.getColumn());
    }

    @Test
    void getRow() {
        assertEquals(30, board.getRow());
    }

    @Test
    void isAllEmptyCase(){
        for(int i = 0; i < board.getRow(); i++){
            for(int j = 0; j < board.getColumn(); j++){
                assertTrue(board.isEmptyCase(i, j));
            }
        }
    }

    @Test
    void isValidAllCase(){
        for(int i = 0; i < board.getRow(); i++){
            for(int j = 0; j < board.getColumn(); j++){
                if(i > j){
                    assertTrue(board.isValidCase(i, j));
                }
            }
        }
    }

    @Test
    void isValidCase(){
        assertTrue(board.isValidCase(0, 0)); // case top left
        assertTrue(board.isValidCase(29, 29)); // case bottom right

        assertFalse(board.isValidCase(0, 29)); // case top right
        assertTrue(board.isValidCase(15, 15));// case middle

        assertFalse(board.isValidCase(5, 15));
    }
    @Test
    void placeSymbolInvalidPlacement(){
        Symbol symbol = new Symbol("X", TypeOfSymbol.CROSS);
        boolean result = board.placeSymbol(symbol, 2, 3); // place in invalid case
        assertFalse(result);
    }

    @Test
    void placeSymbolValidPlacement(){
        Symbol symbol = new Symbol("O", TypeOfSymbol.CIRCLE);
        boolean result = board.placeSymbol(symbol, 3, 2);
        assertTrue(result);
    }

    @Test
    void getSymbolInCase() {
        Symbol symbol = new Symbol("O", TypeOfSymbol.CIRCLE);
        this.placeSymbol(board, 3, 2, symbol);
        assertEquals(symbol, board.getSymbolInCase(3, 2));
    }

    @Test
    void getTypeSymbolInCase() {
        this.placeSymbol(board, 3, 2, new Symbol("0", TypeOfSymbol.CIRCLE));
        assertEquals(TypeOfSymbol.CIRCLE, board.getSymbolInCase(3, 2).typeOfSymbol);
    }
}