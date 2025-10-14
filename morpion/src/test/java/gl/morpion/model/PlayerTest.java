package gl.morpion.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getName() {
        Symbol croix = new Symbol("croix.pnj",TypeOfSymbol.CROSS);
        Player test = new Player("AAA",croix);

        assertEquals("AAA",test.getName());
    }

    @Test
    void setName() {
        Symbol croix = new Symbol("croix.pnj",TypeOfSymbol.CROSS);
        Player test = new Player("AAA",croix);

        assertEquals("AAA",test.getName());
        test.setName("BBB");
        assertEquals("BBB",test.getName());
    }

    @Test
    void getPoints() {
        Symbol croix = new Symbol("croix.pnj",TypeOfSymbol.CROSS);
        Player test = new Player("AAA",0,croix);

        assertEquals(0,test.getPoints());

    }

    @Test
    void setPoints() {
        Symbol croix = new Symbol("croix.pnj",TypeOfSymbol.CROSS);
        Player test = new Player("AAA",0,croix);

        assertEquals(0,test.getPoints());
        test.setPoints(10);
        assertEquals(10,test.getPoints());
    }

    @Test
    void addPoint() {
        Symbol croix = new Symbol("croix.pnj",TypeOfSymbol.CROSS);
        Player test = new Player("AAA",0,croix);

        assertEquals(0,test.getPoints());
        test.addPoint();
        assertNotEquals(10,test.getPoints());
        assertEquals(1,test.getPoints());
    }

    @Test
    void playTurn() {
        Symbol croix = new Symbol("croix.pnj",TypeOfSymbol.CROSS);
        Player test = new Player("AAA",croix);

        assertFalse(test.isTurn());
        test.playTurn();
        assertTrue(test.isTurn());
    }

    @Test
    void waitTurn() {
        Symbol croix = new Symbol("croix.pnj",TypeOfSymbol.CROSS);
        Player test = new Player("AAA",croix);

        assertFalse(test.isTurn());
        test.playTurn();
        assertTrue(test.isTurn());
        test.waitTurn();
        assertFalse(test.isTurn());
    }

    @Test
    void isTurn() {
        Symbol croix = new Symbol("croix.pnj",TypeOfSymbol.CROSS);
        Player test = new Player("AAA",croix);

        assertFalse(test.isTurn());
    }
}