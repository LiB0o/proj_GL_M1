package gl.morpion.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gl.morpion.model.Game;
import gl.morpion.model.GameBoard;
import gl.morpion.model.Symbol;
import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBoard {
    private Game game;
    private HashMap<Pair<Integer, Integer>, Symbol> usedCase;

    public LoadBoard(Game game) {
        this.game= game;
        this.usedCase = game.getUsedCase();
    }
    public void readJsonFromFile() {
        Gson gson = new Gson();

        File file = new File(System.getProperty("user.dir") + "/save/save.json");

        if (!file.exists()) {
            throw new RuntimeException("Fichier " + file.getAbsolutePath() + " introuvable !");
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<CellData>>() {}.getType();
            List<CellData> cells = gson.fromJson(reader, listType);

            this.usedCase = new HashMap<>();
            for (CellData cell : cells) {
                Pair<Integer, Integer> key = new Pair<>(cell.getRow(), cell.getCol());
                Symbol value = Symbol.fromString(cell.getSymbol());
                this.usedCase.put(key, value);
            }

            // test display
            for (Map.Entry<Pair<Integer, Integer>, Symbol> entry : usedCase.entrySet()) {
                Pair<Integer, Integer> key = entry.getKey();
                Symbol value = entry.getValue();
                System.out.println("Clé : (" + key.getKey() + ", " + key.getValue() + ") -> Valeur : " + value);
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

}
