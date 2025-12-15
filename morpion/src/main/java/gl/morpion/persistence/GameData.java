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

    // === GAME STATE (board + players) ===
    private List<CellData> board;
    private String player1Name;
    private String player2Name;
    private String currentPlayerName;
    private String player1Symbol;
    private String player2Symbol;

    // === METADATA ===
    private String saveName;       // Display name in the menu
    private String mode;           // PVP, PVBOT, CUSTOM_PVP, CUSTOM_PVBOT
    private String botDifficulty;  // Bot level (float as String) or null
    private Integer rows;          // Number of rows
    private Integer cols;          // Number of columns
    private Integer winCondition;  // Number of aligned symbols required to win
    private String savedAt;        // Timestamp

    /**
     * Creates an empty {@code GameData} instance.
     * <p>
     * This no-argument constructor is typically required by JSON serialization/deserialization
     * libraries.
     * </p>
     */
    public GameData() {}

    // BOARD

    /**
     * Returns the list of saved cells representing the board state.
     *
     * @return the board cell data list, or {@code null} if not set
     */
    public List<CellData> getBoard() { return board; }

    /**
     * Sets the saved board state.
     *
     * @param board the list of saved cells representing the board
     */
    public void setBoard(List<CellData> board) { this.board = board; }

    // PLAYERS

    /**
     * Returns player 1 name.
     *
     * @return player 1 name
     */
    public String getPlayer1Name() { return player1Name; }

    /**
     * Sets player 1 name.
     *
     * @param player1Name player 1 name
     */
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }

    /**
     * Returns player 2 name (or bot name depending on the mode).
     *
     * @return player 2 name or bot name
     */
    public String getPlayer2Name() { return player2Name; }

    /**
     * Sets player 2 name (or bot name depending on the mode).
     *
     * @param player2Name player 2 name (or bot name)
     */
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }

    /**
     * Returns the name of the player who was current at the time of saving.
     *
     * @return the current player name
     */
    public String getCurrentPlayerName() { return currentPlayerName; }

    /**
     * Sets the name of the player who was current at the time of saving.
     *
     * @param currentPlayerName the current player name
     */
    public void setCurrentPlayerName(String currentPlayerName) { this.currentPlayerName = currentPlayerName; }

    /**
     * Returns player 1 symbol identifier as stored in the save (typically a filename or code).
     *
     * @return player 1 symbol identifier
     */
    public String getPlayer1Symbol() { return player1Symbol; }

    /**
     * Sets player 1 symbol identifier as stored in the save (typically a filename or code).
     *
     * @param player1Symbol player 1 symbol identifier
     */
    public void setPlayer1Symbol(String player1Symbol) { this.player1Symbol = player1Symbol; }

    /**
     * Returns player 2 symbol identifier as stored in the save (typically a filename or code).
     *
     * @return player 2 symbol identifier
     */
    public String getPlayer2Symbol() { return player2Symbol; }

    /**
     * Sets player 2 symbol identifier as stored in the save (typically a filename or code).
     *
     * @param player2Symbol player 2 symbol identifier
     */
    public void setPlayer2Symbol(String player2Symbol) { this.player2Symbol = player2Symbol; }

    // METADATA

    /**
     * Returns the display name of the save (as shown in the UI).
     *
     * @return the save name
     */
    public String getSaveName() { return saveName; }

    /**
     * Sets the display name of the save (as shown in the UI).
     *
     * @param saveName the save name
     */
    public void setSaveName(String saveName) { this.saveName = saveName; }

    /**
     * Returns the saved game mode as a string.
     *
     * @return the mode string (e.g., "PVP", "PVBOT", "CUSTOM_PVP", "CUSTOM_PVBOT")
     */
    public String getMode() { return mode; }

    /**
     * Sets the saved game mode as a string.
     *
     * @param mode the mode string
     */
    public void setMode(String mode) { this.mode = mode; }

    /**
     * Returns the saved bot difficulty.
     *
     * @return bot difficulty as a string (float), or {@code null} if not applicable
     */
    public String getBotDifficulty() { return botDifficulty; }

    /**
     * Sets the saved bot difficulty.
     *
     * @param botDifficulty bot difficulty as a string (float), or {@code null}
     */
    public void setBotDifficulty(String botDifficulty) { this.botDifficulty = botDifficulty; }

    /**
     * Returns the saved number of rows for the board.
     *
     * @return number of rows, or {@code null} if not set
     */
    public Integer getRows() { return rows; }

    /**
     * Sets the saved number of rows for the board.
     *
     * @param rows number of rows
     */
    public void setRows(Integer rows) { this.rows = rows; }

    /**
     * Returns the saved number of columns for the board.
     *
     * @return number of columns, or {@code null} if not set
     */
    public Integer getCols() { return cols; }

    /**
     * Sets the saved number of columns for the board.
     *
     * @param cols number of columns
     */
    public void setCols(Integer cols) { this.cols = cols; }

    /**
     * Returns the saved win condition.
     *
     * @return number of aligned symbols required to win, or {@code null} if not set
     */
    public Integer getWinCondition() { return winCondition; }

    /**
     * Sets the saved win condition.
     *
     * @param winCondition number of aligned symbols required to win
     */
    public void setWinCondition(Integer winCondition) { this.winCondition = winCondition; }

    /**
     * Returns the timestamp of when the game was saved.
     *
     * @return save timestamp as a string
     */
    public String getSavedAt() { return savedAt; }

    /**
     * Sets the timestamp of when the game was saved.
     *
     * @param savedAt save timestamp as a string
     */
    public void setSavedAt(String savedAt) { this.savedAt = savedAt; }
}
