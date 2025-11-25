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
        if(board.isEmptyCase(row, col) || board.getSymbolInCase(row, col).getImage() == null){
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


    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }
}
