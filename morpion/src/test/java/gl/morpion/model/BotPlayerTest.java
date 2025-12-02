package gl.morpion.model;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.Assert.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BotPlayerTest {

    Symbol croix = new Symbol("",TypeOfSymbol.CROSS);
    GameBoard board;
    BotPlayer bot;

    @BeforeEach
    void setUp() {

        board = new RectangleBoard(10,10);
        bot = new BotPlayer("Bot",0,3.5f,croix,5,board.useCase);
    }

    @Test
    public void setBotBoard() {

        HashMap<Pair<Integer, Integer>, Float> vide = new HashMap<>();
        //System.out.println(vide);
        assertNotEquals(vide,bot.boardView);
        vide.putAll(bot.boardView);

        //System.out.println(vide.get(new Pair<>(0,0)));
        assertEquals(vide,bot.boardView);

    }

    @Test
    void getLevel() {
        assertEquals(3.5f, bot.getLevel());
    }

    @Test
    void setLevel() {
        bot.setLevel(1.0f);
        assertEquals(1.0f, bot.getLevel());
    }

    @Test
    void totalValueofCase() {

        assertEquals(3.0f,bot.totalValueofCase(new Pair<>(0,0)));
    }


    @Test
    void recomputeNeighbour() {
        Float init_value = bot.totalValueofCase(new Pair<>(0,0)); //3.0f
        bot.symbolPutByBot(new Pair<>(1,0));
        assertNotEquals(init_value,bot.totalValueofCase(new Pair<>(0,0)));
        bot.recomputeNeighbour(new Pair<>(0,0));
        assertEquals(0.0f, bot.boardView.get(new Pair<>(1,0)));
    }

    @Test
    void symbolPutByBot() {
        assertEquals(3.0f,bot.totalValueofCase(new Pair<>(0,0)));
        bot.symbolPutByBot(new Pair<>(0,0));
        assertEquals(0.0f,bot.boardView.get(new Pair<>(0,0)));
    }

    @Test
    void symbolPutByPlayer() {
        assertEquals(3.0f,bot.totalValueofCase(new Pair<>(0,0)));
        bot.symbolPutByPlayer(new Pair<>(0,0));
        assertEquals(-1.0f,bot.boardView.get(new Pair<>(0,0)));
    }

    @Test
    void resetValueCase() {
        assertEquals(3.0f,bot.totalValueofCase(new Pair<>(0,0)));
        bot.symbolPutByPlayer(new Pair<>(0,0));
        assertEquals(-1.0f,bot.boardView.get(new Pair<>(0,0)));
        bot.resetValueCase(new Pair<>(0,0));
        assertEquals(1.0f,bot.boardView.get(new Pair<>(0,0)));
    }

    @Test
    void getMaxValue() {
        //System.out.println(bot.boardView);
        assertEquals(new Pair<Integer,Integer>(5,4), bot.getMaxValue());
    }


}