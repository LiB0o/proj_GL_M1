package gl.morpion.persistence;

import com.google.gson.stream.JsonWriter;
import gl.morpion.adapters.SymbolViewAdapter;
import gl.morpion.model.GameBoard;
import gl.morpion.view.GameBoardView;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class SaveBoard {
    private GameBoardView boardView;
    private SymbolViewAdapter symbol;

    public SaveBoard(GameBoardView boardView) {
        this.boardView = boardView;
        this.symbol = new SymbolViewAdapter();
    }

    /**
     * Write a column Write a column in JSON format
     * @param writer
     * @param row: line
     * @param col:column
     * @param imageUrl: image view url
     * @throws IOException
     */
    private void writeBoardColumn(JsonWriter writer, int row, int col, String imageUrl) throws IOException {
        writer.beginObject();
        writer.name("row").value(row);
        writer.name("col").value(col);
        if(boardView.getGameBoard().isEmptyCase(row, col) || boardView.getGameBoard().getSymbolInCase(row, col).getImage() == null){
            writer.name("symbol").value("None");
        }else {
            //writer.name("symbol").value(board.getSymbolInCase(row, col).getImage());
            this.symbol.write(writer, imageUrl);
        }
        writer.endObject();
    }

    /**
     * Write the entire board in JSON format.
     * @param writer :
     * @param board : game board
     */
    public void writeBoard(JsonWriter writer, GameBoardView board) {
        int rows = board.getGameBoard().getRow();
        int cols = board.getGameBoard().getColumn();
        try {
            writer.beginArray();
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    if(board.getGameBoard().getSymbolInCase(i, j) != null){
                        writeBoardColumn(writer, i, j, board.getGameBoard().getSymbolInCase(i, j).getImage());
                    }else {
                        writeBoardColumn(writer,i, j,  null);
                    }
                }
            }
            writer.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBoard() {
        try {
            String projectRoot = System.getProperty("user.dir");
            File saveDir = new File(projectRoot, "save");
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }

            File saveFile = new File(saveDir, "save.json");

            // write JSON
            JsonWriter writer = new JsonWriter(new FileWriter(saveFile));
            writer.setIndent("  ");
            this.writeBoard(writer, this.boardView);
            writer.close();

            System.out.println("JSON write board in save.json !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
