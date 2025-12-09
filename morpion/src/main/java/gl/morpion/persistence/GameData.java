package gl.morpion.persistence;

import java.util.List;

/**
 * Data class representing a complete saved game state.
 *
 * Stores:
 *  - board state
 *  - players (names + symbols)
 *  - current player
 *  - metadata: save name, mode, bot difficulty, board size, winCondition, timestamp
 */
public class GameData {

    // === ÉTAT DU JEU (board + joueurs) ===
    private List<CellData> board;
    private String player1Name;
    private String player2Name;
    private String currentPlayerName;
    private String player1Symbol;
    private String player2Symbol;

    // === MÉTADONNÉES ===
    private String saveName;       // Nom visible dans le menu
    private String mode;           // PVP, PVBOT, CUSTOM_PVP, CUSTOM_PVBOT
    private String botDifficulty;  // niveau bot (float string) ou null
    private Integer rows;          // nb lignes
    private Integer cols;          // nb colonnes
    private Integer winCondition;  // nb symboles alignés nécessaires
    private String savedAt;        // timestamp

    public GameData() {}

    // BOARD
    public List<CellData> getBoard() { return board; }
    public void setBoard(List<CellData> board) { this.board = board; }

    // PLAYERS
    public String getPlayer1Name() { return player1Name; }
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }

    public String getPlayer2Name() { return player2Name; }
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }

    public String getCurrentPlayerName() { return currentPlayerName; }
    public void setCurrentPlayerName(String currentPlayerName) { this.currentPlayerName = currentPlayerName; }

    public String getPlayer1Symbol() { return player1Symbol; }
    public void setPlayer1Symbol(String player1Symbol) { this.player1Symbol = player1Symbol; }

    public String getPlayer2Symbol() { return player2Symbol; }
    public void setPlayer2Symbol(String player2Symbol) { this.player2Symbol = player2Symbol; }

    // METADATA
    public String getSaveName() { return saveName; }
    public void setSaveName(String saveName) { this.saveName = saveName; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getBotDifficulty() { return botDifficulty; }
    public void setBotDifficulty(String botDifficulty) { this.botDifficulty = botDifficulty; }

    public Integer getRows() { return rows; }
    public void setRows(Integer rows) { this.rows = rows; }

    public Integer getCols() { return cols; }
    public void setCols(Integer cols) { this.cols = cols; }

    public Integer getWinCondition() { return winCondition; }
    public void setWinCondition(Integer winCondition) { this.winCondition = winCondition; }

    public String getSavedAt() { return savedAt; }
    public void setSavedAt(String savedAt) { this.savedAt = savedAt; }
}
