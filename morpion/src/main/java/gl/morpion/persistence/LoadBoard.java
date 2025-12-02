package gl.morpion.persistence;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gl.morpion.model.Game;
import gl.morpion.model.GameBoard;
import gl.morpion.model.Symbol;
import javafx.util.Pair;

/**
 * Handles loading the game board state from a JSON file.
 * Supports loading both the new format (with player information) and the legacy format (board only).
 * The file is read from {@code save/save.json} in the project root directory.
 * 
 * <p>This class automatically detects the file format:
 * <ul>
 *   <li>New format: {@link GameData} with board and player information</li>
 *   <li>Legacy format: Simple array of {@link CellData} (board only)</li>
 * </ul>
 * 
 * <p>After loading, the board state is restored to the provided Game instance,
 * and player information (if available) can be retrieved via {@link #getLoadedGameData()}.
 * 
 * @author GL M1 Project Team
 * @version 1.0
 * @see GameData
 * @see CellData
 */
public class LoadBoard {
    private final Game game;
    private HashMap<Pair<Integer, Integer>, Symbol> usedCase;
    private GameData loadedGameData;

    /**
     * <h3>LoadBoard</h3>
     * Constructs a LoadBoard instance for the given game.
     * 
     * @param game The game instance where the loaded board state will be restored
     */
    public LoadBoard(Game game) {
        this.game = game;
        this.usedCase = game.getUsedCase();
    }
    
    /**
     * Returns the loaded game data (players, current player).
     * This method returns the {@link GameData} object that was loaded from the save file.
     * 
     * @return The loaded game data, or null if not yet loaded or if legacy format was used
     */
    public GameData getLoadedGameData() {
        return loadedGameData;
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
     * Converts a filename (e.g., "croix.jpg") to a valid resource URL.
     * Attempts to locate the resource file in the classpath and returns its full URL.
     * 
     * @param fileName The filename (e.g., "croix.jpg" or "cercle.png")
     * @return The complete resource URL, or the filename if the resource is not found
     */
    private String convertFileNameToUrl(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName;
        }
        
        // Determine the resource path based on the filename
        String resourcePath;
        if (fileName.contains("croix")) {
            resourcePath = "/gl/morpion/croix.jpg";
        } else if (fileName.contains("cercle")) {
            resourcePath = "/gl/morpion/cercle.png";
        } else {
            // If the name doesn't match any known symbol, return as is
            return fileName;
        }
        
        // Try to get the resource URL
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                return url.toExternalForm();
            }
        } catch (Exception e) {
            // If resource not found, continue
        }
        
        // If resource not found, try with GameBoardView
        try {
            java.net.URL url = gl.morpion.view.GameBoardView.class.getResource(resourcePath);
            if (url != null) {
                return url.toExternalForm();
            }
        } catch (Exception e) {
            // If resource not found, continue
        }
        
        // Last resort: return the filename (may cause an error but avoids immediate crash)
        return fileName;
    }

    /**
     * Reads and loads the game board state from the save file.
     * 
     * <p>This method:
     * <ul>
     *   <li>Attempts to load the new format ({@link GameData}) first</li>
     *   <li>Falls back to the legacy format (array of {@link CellData}) if the new format fails</li>
     *   <li>Restores all symbols to their correct positions on the board</li>
     *   <li>Updates the game's usedCase HashMap with loaded symbols</li>
     * </ul>
     * 
     * @param fileName The name of the save file to load (e.g., "save_pvp.json" or "save_pvb.json")
     * @throws RuntimeException If the save file doesn't exist or if an I/O error occurs
     */
    public void readJsonFromFile(String fileName) {
        Gson gson = new Gson();

        File projectRoot = getProjectRoot();
        File file = new File(projectRoot, "save/" + fileName);

        if (!file.exists()) {
            throw new RuntimeException("File " + file.getAbsolutePath() + " not found!");
        }

        try (Reader reader = new FileReader(file)) {
            // Try to load as GameData (new format with players)
            try {
                loadedGameData = gson.fromJson(reader, GameData.class);
                if (loadedGameData != null && loadedGameData.getBoard() != null) {
                    // New format with players
                    loadGameData(loadedGameData);
                    return;
                }
            } catch (Exception e) {
                // If it fails, try the legacy format
            }
            
            // Legacy format (board only)
            reader.close();
            try (Reader reader2 = new FileReader(file)) {
                Type listType = new TypeToken<List<CellData>>() {}.getType();
                List<CellData> cells = gson.fromJson(reader2, listType);
                loadBoardOnly(cells);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Loads only the board state (legacy format).
     * Clears the board first, then restores symbols from the loaded cell data.
     * 
     * @param cells The list of cell data to load
     */
    private void loadBoardOnly(List<CellData> cells) {

        GameBoard gameBoard = this.game.getGameBoard();
        
        // Clear the board before restoring symbols
        for (int i = 0; i < gameBoard.getRow(); i++) {
            for (int j = 0; j < gameBoard.getColumn(); j++) {
                if (gameBoard.isValidCase(i, j)) {
                    gameBoard.symbols.get(i)[j] = null;
                }
            }
        }
        
        // Clear the Game's HashMap
        this.game.getUsedCase().clear();
        this.usedCase = new HashMap<>();
        
        // Restore symbols from the file
        for (CellData cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            String symbolStr = cell.getSymbol();
            
            // Don't add empty cells (None) to usedCase
            if (symbolStr != null && !symbolStr.equals("None")) {
                Pair<Integer, Integer> key = new Pair<>(row, col);
                // Convert filename to valid URL
                String imageUrl = convertFileNameToUrl(symbolStr);
                Symbol value = Symbol.fromString(imageUrl);
                if (value != null) {
                    // Ensure the symbol has the complete URL
                    value.setImage(imageUrl);
                    this.usedCase.put(key, value);
                    // Restore the symbol on the board
                    if (gameBoard.isValidCase(row, col)) {
                        gameBoard.placeSymbol(value, row, col);
                    }
                }
            }
        }

        // Update the Game's HashMap with loaded data
        this.game.getUsedCase().putAll(this.usedCase);
        System.out.println("Board loaded successfully:\n" + this.usedCase);
    }
    
    /**
     * Loads the board and player data (new format).
     * This method loads the board state and stores player information for later use.
     * 
     * @param gameData The game data containing board and player information
     */
    private void loadGameData(GameData gameData) {
        // Load the board
        loadBoardOnly(gameData.getBoard());
        
        // Player information will be used by MainMenuController
        // to create players and restore the current player
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
