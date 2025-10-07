package gl_morpion.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
    }

    @Test
    void setPoints() {
    }

    @Test
    void addPoint() {
    }

    @Test
    void playTurn() {
    }

    @Test
    void waitTurn() {
    }

    @Test
    void isTurn() {
    }
}