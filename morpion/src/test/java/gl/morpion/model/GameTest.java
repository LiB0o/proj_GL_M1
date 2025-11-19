package gl.morpion.model;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    GameBoard gameBoard;
    Game game;
    public List<Player> players = new ArrayList<>();
    public Symbol bot = new Symbol("Cross", TypeOfSymbol.CROSS);
    public Symbol human = new Symbol("human", TypeOfSymbol.CIRCLE);

    public Player pBot = new Player("PBot", 0, bot);

    public Player phum = new Player("Human", 0, human);

    private void placeSomeSymbolColumn(){
        for(int i = 0; i < gameBoard.getRow(); i++){
            if(i < 4){
                gameBoard.placeSymbol(bot, i, 0);
                //System.out.println(STR."point : (\{i},0) = \{gameBoard.getSymbolInCase(i, 0)}");
            }
        }
    }

    public void placeSomeSymbolLine() {
        for (int j = 0; j < gameBoard.getColumn(); j++) {
            if (j < 4) {
                gameBoard.placeSymbol(bot, 0, j);
                System.out.println(STR."point : (0,\{j}) = \{gameBoard.getSymbolInCase(0, j)}");
            }
        }
    }

    private void placeSymbolDiagonalUpLeftDownRight(){
        for(int i = 0; i < gameBoard.getRow(); i++){
            for(int j = 0; j < gameBoard.getColumn(); j++){
                if (i < 4){
                    if(i == j){
                        gameBoard.placeSymbol(bot, i, j);
                    }
                }
            }
        }
    }

    private void placeDiagonalUpRightDownLeft(){
        gameBoard.placeSymbol(bot, 0, 4);
        gameBoard.placeSymbol(bot, 1, 3);
        gameBoard.placeSymbol(bot, 2, 2);
        gameBoard.placeSymbol(bot, 3, 1);
    }

    @BeforeEach
    void setUp() {
        players.add(pBot);
        players.add(phum);
        gameBoard = new RectangleBoard(10, 10);
        game = new Game(gameBoard, null, null, null);// a cooriger
    }

    @Test
    void checkClassicVictoryVertical(){
        placeSomeSymbolColumn();
        Pair<Boolean, Symbol> victory = game.checkClassicVictory(4);
        assertEquals(victory.getValue(), bot);
        assertTrue(victory.getKey());
    }
    @Test
    void checkClassicVictoryHorizontal(){
        placeSomeSymbolLine();
        Pair<Boolean, Symbol> victory = game.checkClassicVictory(4);
        assertEquals(victory.getValue(), bot);
        assertTrue(victory.getKey());
    }

    @Test
    void checkDiagonalUpLeftDownRight(){
        placeSymbolDiagonalUpLeftDownRight();
        Pair<Boolean, Symbol> victory = game.checkClassicVictory(4);
        assertEquals(victory.getValue(), bot);
        assertTrue(victory.getKey());
    }

    @Test
    void checkDiagonalUpRightDownLeft(){
        placeDiagonalUpRightDownLeft();
        Pair<Boolean, Symbol> victory = game.checkClassicVictory(4);
        assertEquals(victory.getValue(), bot);
        assertTrue(victory.getKey());
    }

    @Test
    void checkNoVictory(){
        gameBoard.placeSymbol(bot, 0, 0);
        gameBoard.placeSymbol(human, 0, 1);
        gameBoard.placeSymbol(bot, 0, 2);
        gameBoard.placeSymbol(human, 0, 3);
        Pair<Boolean, Symbol> victory = game.checkClassicVictory(4);
        assertFalse(victory.getKey());
    }
}