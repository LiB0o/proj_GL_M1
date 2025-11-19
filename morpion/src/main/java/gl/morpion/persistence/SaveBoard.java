package gl.morpion.persistence;

import com.google.gson.stream.JsonWriter;
import gl.morpion.adapters.SymbolViewAdapter;
import gl.morpion.model.GameBoard;
import gl.morpion.view.GameBoardView;
import javafx.scene.image.ImageView;

import java.io.IOException;



public class SaveBoard {
    private GameBoard board;
    private SymbolViewAdapter symbol;

    public SaveBoard(GameBoard board) {
        this.board = board;
        this.symbol = new SymbolViewAdapter();
    }

    private void writeBoardColumn(JsonWriter writer, int row, int col, GameBoard board) throws IOException {
        writer.beginObject();
        writer.name("row").value(row);
        writer.name("col").value(col);
        if(board.isEmptyCase(row, col)){
            writer.name("symbol").value("None");
        }else {
            writer.name("symbol").value(board.getSymbolInCase(row, col).getImage());

        }
        writer.endObject();
    }

    public void writeBoard(JsonWriter writer, GameBoard board) {

        int rows = board.getRow();
        int cols = board.getColumn();
        try {
            writer.beginArray();
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {

                    writeBoardColumn(writer, i, j, board);
                }
            }
            writer.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }
}
