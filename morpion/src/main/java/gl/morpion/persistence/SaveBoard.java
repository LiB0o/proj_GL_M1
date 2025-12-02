package gl.morpion.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import gl.morpion.adapters.SymbolViewAdapter;
import gl.morpion.controllers.GameController;
import gl.morpion.model.BotPlayer;
import gl.morpion.model.Game;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.view.GameBoardView;

/**
 * Handles saving the game board state to a JSON file.
 * Supports saving both the board state and player information (names, current player, symbols).
 * The saved data is written to {@code save/save.json} in the project root directory.
 * 
 * <p>This class provides two save methods:
 * <ul>
 *   <li>{@link #saveBoard(Game)} - Saves the board with full player information</li>
 *   <li>{@link #saveBoard()} - Saves only the board (backward compatibility)</li>
 * </ul>
 * 
 * @author GL M1 Project Team
 * @version 1.0
 */
public class SaveBoard {
    private final GameBoardView boardView;
    private final SymbolViewAdapter symbol;

    /**
     * Constructs a SaveBoard instance for the given game board view.
     * 
     * @param boardView The game board view containing the board state to save
     */
    public SaveBoard(GameBoardView boardView) {
        this.boardView = boardView;
        this.symbol = new SymbolViewAdapter();
    }

    /**
     * Writes a single cell (column) to JSON format.
     * 
     * @param writer The JSON writer to write to
     * @param row The row index of the cell
     * @param col The column index of the cell
     * @param imageUrl The image URL of the symbol in this cell, or null if empty
     * @throws IOException If an I/O error occurs while writing
     */
    private void writeBoardColumn(JsonWriter writer, int row, int col, String imageUrl) throws IOException {
        writer.beginObject();
        writer.name("row").value(row);
        writer.name("col").value(col);
        if(boardView.getGameBoard().isEmptyCase(row, col) || imageUrl == null){
            writer.name("symbol").value("None");
        }else {
            this.symbol.write(writer, imageUrl);
        }
        writer.endObject();
    }

    /**
     * Writes the entire board to JSON format using JsonWriter.
     * This method is used for the legacy format (board only, without player data).
     * 
     * @param writer The JSON writer to write to
     * @param board The game board view to save
     */
    public void writeBoard(JsonWriter writer, GameBoardView board) {
        int rows = board.getGameBoard().getRow();
        int cols = board.getGameBoard().getColumn();
        try {
            writer.beginArray();
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    if(board.getGameBoard().isEmptyCase(i, j)){
                        writeBoardColumn(writer, i, j, null);
                    }else {
                        Symbol symbol = board.getGameBoard().getSymbolInCase(i, j);
                        String imageUrl = (symbol != null) ? symbol.getImage() : null;
                        writeBoardColumn(writer, i, j, imageUrl);
                    }
                }
            }
            writer.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the project root directory (proj_GL_M1).
     * Automatically detects if the current working directory is "morpion" and navigates up one level.
     * 
     * @return The project root directory as a File object
     */
    private File getProjectRoot() {
        File currentDir = new File(System.getProperty("user.dir"));
        
        // If we're in the morpion directory, go up one level
        if (currentDir.getName().equals("morpion")) {
            return currentDir.getParentFile();
        }
        
        // Otherwise, we're probably already in the root directory
        return currentDir;
    }

    /**
     * Saves the game board state along with player information to a JSON file.
     * 
     * <p>This method saves:
     * <ul>
     *   <li>The complete board state (all cells with their symbols or empty state)</li>
     *   <li>Player names (player1 and player2)</li>
     *   <li>The current player's name (who should play next)</li>
     *   <li>Player symbols (croix.jpg or cercle.png)</li>
     * </ul>
     * 
     * <p>The file is saved to {@code save/save_pvp.json} or {@code save/save_pvb.json} 
     * depending on the game mode, in the project root directory.
     * If the save directory doesn't exist, it will be created automatically.
     * 
     * @param game The game instance containing players and current player information.
     *              If null, only the board state will be saved (backward compatibility).
     * @param gameController The game controller to check the game mode (can be null).
     */
    public void saveBoard(Game game, GameController gameController) {
        try {
            File projectRoot = getProjectRoot();
            File saveDir = new File(projectRoot, "save");
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }

            // Determine save file name based on game mode
            String fileName = "save_pvp.json"; // Default: Player vs Player
            
            // First check GameController for vsBot flag (most reliable)
            if (gameController != null && gameController.isVsBot()) {
                fileName = "save_pvb.json"; // Player vs Bot
            } else if (game != null && game.getPlayers() != null && game.getPlayers().size() >= 2) {
                // Fallback: check if any player is a bot
                Player p1 = game.getPlayers().get(0);
                Player p2 = game.getPlayers().get(1);
                if (p1 instanceof BotPlayer || p2 instanceof BotPlayer) {
                    fileName = "save_pvb.json"; // Player vs Bot
                }
            }
            
            File saveFile = new File(saveDir, fileName);

            // Create save data structure
            GameData gameData = new GameData();
            
            // Save the board state
            List<CellData> boardData = new ArrayList<>();
            int rows = boardView.getGameBoard().getRow();
            int cols = boardView.getGameBoard().getColumn();
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    CellData cell = new CellData();
                    cell.setRow(i);
                    cell.setCol(j);
                    if(boardView.getGameBoard().isEmptyCase(i, j)){
                        cell.setSymbol("None");
                    }else {
                        Symbol symbol = boardView.getGameBoard().getSymbolInCase(i, j);
                        if (symbol != null) {
                            String imageUrl = symbol.getImage();
                            // Extract filename from URL
                            if (imageUrl != null) {
                                if (imageUrl.contains("croix.jpg")) {
                                    cell.setSymbol("croix.jpg");
                                } else if (imageUrl.contains("cercle.png")) {
                                    cell.setSymbol("cercle.png");
                                } else {
                                    cell.setSymbol("None");
                                }
                            } else {
                                cell.setSymbol("None");
                            }
                        } else {
                            cell.setSymbol("None");
                        }
                    }
                    boardData.add(cell);
                }
            }
            gameData.setBoard(boardData);
            
            // Save player information
            if (game != null && game.getPlayers() != null && game.getPlayers().size() >= 2) {
                Player p1 = game.getPlayers().get(0);
                Player p2 = game.getPlayers().get(1);
                Player currentPlayer = game.getCurrentPlayer();
                
                gameData.setPlayer1Name(p1.getName());
                gameData.setPlayer2Name(p2.getName());
                gameData.setCurrentPlayerName(currentPlayer != null ? currentPlayer.getName() : p1.getName());
                
                // Check if players are bots and save bot information
                boolean p1IsBot = p1 instanceof BotPlayer;
                boolean p2IsBot = p2 instanceof BotPlayer;
                gameData.setPlayer1IsBot(p1IsBot);
                gameData.setPlayer2IsBot(p2IsBot);
                
                // Save bot difficulty and win condition if at least one player is a bot
                if (p1IsBot || p2IsBot) {
                    BotPlayer bot = p1IsBot ? (BotPlayer) p1 : (BotPlayer) p2;
                    gameData.setBotDifficulty(bot.getLevel());
                    gameData.setWinCondition(bot.getWinCondition());
                } else {
                    // For non-bot games, save win condition from Game
                    gameData.setWinCondition(Game.getDefaultMaxNumberSymbolAlign());
                }
                
                // Save player symbols
                if (p1.getSymbol() != null && p1.getSymbol().getImage() != null) {
                    String symbol1 = p1.getSymbol().getImage();
                    if (symbol1.contains("croix.jpg")) {
                        gameData.setPlayer1Symbol("croix.jpg");
                    } else if (symbol1.contains("cercle.png")) {
                        gameData.setPlayer1Symbol("cercle.png");
                    }
                }
                if (p2.getSymbol() != null && p2.getSymbol().getImage() != null) {
                    String symbol2 = p2.getSymbol().getImage();
                    if (symbol2.contains("croix.jpg")) {
                        gameData.setPlayer2Symbol("croix.jpg");
                    } else if (symbol2.contains("cercle.png")) {
                        gameData.setPlayer2Symbol("cercle.png");
                    }
                }
            }

            // Write to JSON file using Gson
            Gson gson = new Gson();
            try (FileWriter writer = new FileWriter(saveFile)) {
                gson.toJson(gameData, writer);
            }

            System.out.println("Game board saved successfully to " + fileName + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Saves the game board state along with player information to a JSON file.
     * Convenience method that calls {@link #saveBoard(Game, GameController)} with null GameController.
     * 
     * @param game The game instance containing players and current player information.
     */
    public void saveBoard(Game game) {
        saveBoard(game, null);
    }
    
    /**
     * Saves the board state without player information (backward compatibility method).
     * This method calls {@link #saveBoard(Game, GameController)} with null parameters.
     */
    public void saveBoard() {
        saveBoard((Game) null, (GameController) null);
    }
}
