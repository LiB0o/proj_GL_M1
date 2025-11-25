package gl.morpion.persistence;

import com.google.gson.Gson;
import gl.morpion.model.GameBoard;

import java.io.*;

public class LoadBoard {
    private GameBoard gameBoard;

    public LoadBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
    public void readJson(String path) {
        Gson gson = new Gson();

        InputStream is = getClass().getResourceAsStream(path);

        if (is == null) {
            throw new RuntimeException(path+" introuvable dans src/main/resources");
        }
        try (Reader reader = new InputStreamReader(is)) {

            CellData[] cellData = gson.fromJson(reader, CellData[].class);

            for (CellData c : cellData) {
                System.out.println("(" + c.getRow() + ", " + c.getCol() + ") - " + c.getSymbol());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*public void readJson(String path) {

        Gson gson = new Gson();
        try(FileReader reader = new FileReader(path)){
            CellData[] cellData = gson.fromJson(reader, CellData[].class);
            for(int i = 0; i < cellData.length; i++){
                System.out.println("("+cellData[i].row+", "+cellData[i].col+" - "+cellData[i].symbol);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
}
