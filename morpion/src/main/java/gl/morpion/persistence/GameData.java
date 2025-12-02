package gl.morpion.persistence;

import java.util.List;

/**
 * Data class representing a complete saved game state.
 * 
 * <p>This class stores all information needed to restore a game:
 * <ul>
 *   <li>Board state: All cells with their symbols or empty state</li>
 *   <li>Player information: Names and symbols of both players</li>
 *   <li>Current player: The name of the player who should play next</li>
 * </ul>
 * 
 * <p>This class is used for JSON serialization/deserialization with Gson.
 * The structure allows for backward compatibility with older save files that only contain board data.
 * 
 * @author GL M1 Project Team
 * @version 1.0
 * @see SaveBoard
 * @see LoadBoard
 * @see CellData
 */
public class GameData {
    /** The board state as a list of cell data */
    private List<CellData> board;
    
    /** Player 1's name */
    private String player1Name;
    
    /** Player 2's name */
    private String player2Name;
    
    /** The name of the player who should play next */
    private String currentPlayerName;
    
    /** Player 1's symbol filename (e.g., "croix.jpg" or "cercle.png") */
    private String player1Symbol;
    
    /** Player 2's symbol filename (e.g., "croix.jpg" or "cercle.png") */
    private String player2Symbol;
    
    /** Indicates if player 1 is a bot */
    private Boolean player1IsBot;
    
    /** Indicates if player 2 is a bot */
    private Boolean player2IsBot;
    
    /** Bot difficulty level (EASY=2.0, NORMAL=3.0, HARD=3.5) - only used if player is a bot */
    private Float botDifficulty;
    
    /** Win condition (number of aligned symbols needed to win) */
    private Integer winCondition;

    /**
     * Default constructor for Gson deserialization.
     */
    public GameData() {
    }

    /**
     * Gets the board state.
     * 
     * @return The list of cell data representing the board state
     */
    public List<CellData> getBoard() {
        return board;
    }

    /**
     * Sets the board state.
     * 
     * @param board The list of cell data representing the board state
     */
    public void setBoard(List<CellData> board) {
        this.board = board;
    }

    /**
     * Gets player 1's name.
     * 
     * @return Player 1's name
     */
    public String getPlayer1Name() {
        return player1Name;
    }

    /**
     * Sets player 1's name.
     * 
     * @param player1Name Player 1's name
     */
    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    /**
     * Gets player 2's name.
     * 
     * @return Player 2's name
     */
    public String getPlayer2Name() {
        return player2Name;
    }

    /**
     * Sets player 2's name.
     * 
     * @param player2Name Player 2's name
     */
    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    /**
     * Gets the current player's name (who should play next).
     * 
     * @return The current player's name
     */
    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    /**
     * Sets the current player's name (who should play next).
     * 
     * @param currentPlayerName The current player's name
     */
    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    /**
     * Gets player 1's symbol filename.
     * 
     * @return Player 1's symbol filename (e.g., "croix.jpg" or "cercle.png")
     */
    public String getPlayer1Symbol() {
        return player1Symbol;
    }

    /**
     * Sets player 1's symbol filename.
     * 
     * @param player1Symbol Player 1's symbol filename (e.g., "croix.jpg" or "cercle.png")
     */
    public void setPlayer1Symbol(String player1Symbol) {
        this.player1Symbol = player1Symbol;
    }

    /**
     * Gets player 2's symbol filename.
     * 
     * @return Player 2's symbol filename (e.g., "croix.jpg" or "cercle.png")
     */
    public String getPlayer2Symbol() {
        return player2Symbol;
    }

    /**
     * Sets player 2's symbol filename.
     * 
     * @param player2Symbol Player 2's symbol filename (e.g., "croix.jpg" or "cercle.png")
     */
    public void setPlayer2Symbol(String player2Symbol) {
        this.player2Symbol = player2Symbol;
    }

    /**
     * Checks if player 1 is a bot.
     * 
     * @return true if player 1 is a bot, false otherwise
     */
    public Boolean getPlayer1IsBot() {
        return player1IsBot;
    }

    /**
     * Sets whether player 1 is a bot.
     * 
     * @param player1IsBot true if player 1 is a bot, false otherwise
     */
    public void setPlayer1IsBot(Boolean player1IsBot) {
        this.player1IsBot = player1IsBot;
    }

    /**
     * Checks if player 2 is a bot.
     * 
     * @return true if player 2 is a bot, false otherwise
     */
    public Boolean getPlayer2IsBot() {
        return player2IsBot;
    }

    /**
     * Sets whether player 2 is a bot.
     * 
     * @param player2IsBot true if player 2 is a bot, false otherwise
     */
    public void setPlayer2IsBot(Boolean player2IsBot) {
        this.player2IsBot = player2IsBot;
    }

    /**
     * Gets the bot difficulty level.
     * 
     * @return The bot difficulty level (EASY=2.0, NORMAL=3.0, HARD=3.5), or null if no bot
     */
    public Float getBotDifficulty() {
        return botDifficulty;
    }

    /**
     * Sets the bot difficulty level.
     * 
     * @param botDifficulty The bot difficulty level (EASY=2.0, NORMAL=3.0, HARD=3.5)
     */
    public void setBotDifficulty(Float botDifficulty) {
        this.botDifficulty = botDifficulty;
    }

    /**
     * Gets the win condition (number of aligned symbols needed to win).
     * 
     * @return The win condition, or null if not set
     */
    public Integer getWinCondition() {
        return winCondition;
    }

    /**
     * Sets the win condition (number of aligned symbols needed to win).
     * 
     * @param winCondition The win condition
     */
    public void setWinCondition(Integer winCondition) {
        this.winCondition = winCondition;
    }
}

