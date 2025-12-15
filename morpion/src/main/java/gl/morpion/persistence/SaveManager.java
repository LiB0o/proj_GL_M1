package gl.morpion.persistence;

import com.google.gson.Gson;

import gl.morpion.model.Game;
import gl.morpion.model.GameBoard;
import gl.morpion.model.GameMode;
import gl.morpion.view.GameBoardView;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * High-level save/load manager built on top of {@link SaveBoard} / {@link LoadBoard}.
 * <p>
 * Responsibilities:
 * </p>
 * <ul>
 *     <li>{@link #saveGame(Game, GameBoardView, String, GameMode, String, int)}: creates a full {@link GameData}
 *     and writes it to {@code save/<name>.json}</li>
 *     <li>{@link #listSaves()}: lists all available {@code .json} save files in the {@code save/} directory</li>
 *     <li>{@link #loadGameData(String)}: reads a {@link GameData} from a file</li>
 *     <li>{@link #deleteSave(String)}: deletes a save file</li>
 * </ul>
 *
 * <p>
 * This class is static-only and cannot be instantiated.
 * </p>
 */
public final class SaveManager {

    /** Directory name (relative to the detected project root) where saves are stored. */
    private static final String SAVE_DIR_NAME = "save";

    /** Date-time format used for saved timestamps. */
    private static final DateTimeFormatter TS_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Private constructor to prevent instantiation.
     */
    private SaveManager() {
        // static-only
    }

    // ==== File utilities ====

    /**
     * Attempts to detect the project root directory.
     * <p>
     * The method uses the current working directory ({@code user.dir}). If the current directory is named
     * {@code "morpion"}, it returns its parent directory; otherwise, it returns the current directory.
     * </p>
     *
     * @return the detected project root directory
     */
    private static File getProjectRoot() {
        File currentDir = new File(System.getProperty("user.dir"));
        if ("morpion".equals(currentDir.getName())) {
            return currentDir.getParentFile();
        }
        return currentDir;
    }

    /**
     * Returns the directory where saves are stored, creating it if needed.
     *
     * @return the save directory
     */
    private static File getSaveDir() {
        File root = getProjectRoot();
        File dir = new File(root, SAVE_DIR_NAME);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    /**
     * Normalizes a user-provided save name into a safe file name.
     * <p>
     * This method:
     * </p>
     * <ul>
     *     <li>Creates a fallback name if {@code raw} is null/blank</li>
     *     <li>Replaces spaces and unsupported characters with underscores</li>
     *     <li>Ensures the file name ends with {@code .json}</li>
     * </ul>
     *
     * @param raw the raw save name provided by the user
     * @return a normalized file name ending with {@code .json}
     */
    private static String normalizeFileName(String raw) {
        if (raw == null || raw.isBlank()) {
            return "save_" + System.currentTimeMillis();
        }
        // Replace spaces / unsupported characters
        String s = raw.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
        if (!s.toLowerCase().endsWith(".json")) {
            s = s + ".json";
        }
        return s;
    }

    // ==== SAVE ====

    /**
     * Saves the current game state into a dedicated file.
     * <p>
     * This method first uses {@link SaveBoard} to create a base {@link GameData} JSON in a temporary
     * {@code save/save.json} file. Then it enriches that {@link GameData} with metadata (save name, mode,
     * bot difficulty, board size, win condition, timestamp) and writes the final JSON to a normalized file name.
     * </p>
     *
     * @param game          the {@link Game} instance (board, players, current player, etc.)
     * @param boardView     the board view (used by {@link SaveBoard})
     * @param saveName      the user-visible save name
     * @param mode          the game mode (PVP, PVBOT, CUSTOM_PVP, CUSTOM_PVBOT, ...)
     * @param botDifficulty the bot difficulty level (or {@code null} if no bot)
     * @param winCondition  the number of aligned symbols required to win
     * @throws IllegalArgumentException if {@code game} or {@code boardView} is {@code null}
     * @throws RuntimeException         if the temporary save cannot be read or the final save cannot be written
     */
    public static void saveGame(Game game,
                                GameBoardView boardView,
                                String saveName,
                                GameMode mode,
                                String botDifficulty,
                                int winCondition) {

        if (game == null || boardView == null) {
            throw new IllegalArgumentException("game and boardView must not be null");
        }

        // 1) Reuse SaveBoard to build a "base" GameData
        SaveBoard sb = new SaveBoard(boardView);
        sb.saveBoard(game); // writes to save/save.json

        File tempFile = new File(getSaveDir(), "save.json");
        Gson gson = new Gson();
        GameData data;

        try (Reader reader = new FileReader(tempFile)) {
            data = gson.fromJson(reader, GameData.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read temp save.json", e);
        }

        if (data == null) {
            data = new GameData();
        }

        // 2) Enrich with metadata
        GameBoard gb = game.getGameBoard();

        data.setSaveName(saveName);
        data.setMode(mode != null ? mode.name() : null);
        data.setBotDifficulty(botDifficulty);
        data.setRows(gb != null ? gb.getRow() : null);
        data.setCols(gb != null ? gb.getColumn() : null);
        data.setWinCondition(winCondition);
        data.setSavedAt(LocalDateTime.now().format(TS_FORMAT));

        // 3) Write into a dedicated file
        String fileName = normalizeFileName(saveName);
        File saveFile = new File(getSaveDir(), fileName);

        try (Writer writer = new FileWriter(saveFile)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write save file " + saveFile, e);
        }

        System.out.println("Game saved to " + saveFile.getAbsolutePath());
    }

    // ==== LIST ====

    /**
     * Returns the list of all available saves in the {@code save/} directory.
     * <p>
     * Each JSON file is parsed as {@link GameData} and converted into a {@link SaveMetadata} entry.
     * If parsing fails for a file, the error is logged and the file is skipped.
     * </p>
     *
     * @return a list of {@link SaveMetadata} entries, or an empty list if none exist
     */
    public static List<SaveMetadata> listSaves() {
        File dir = getSaveDir();
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".json"));
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }

        List<SaveMetadata> result = new ArrayList<>();
        Gson gson = new Gson();

        for (File f : files) {
            try (Reader reader = new FileReader(f)) {
                GameData data = gson.fromJson(reader, GameData.class);
                if (data == null) continue;

                String saveName = (data.getSaveName() != null) ? data.getSaveName() : f.getName();
                String mode = (data.getMode() != null) ? data.getMode() : "UNKNOWN";
                String diff = data.getBotDifficulty();
                String ts = (data.getSavedAt() != null)
                        ? data.getSavedAt()
                        : TS_FORMAT.format(
                        LocalDateTime.ofEpochSecond(f.lastModified() / 1000, 0,
                                java.time.ZoneOffset.UTC)
                );

                result.add(new SaveMetadata(
                        f.getName(),
                        saveName,
                        mode,
                        diff,
                        ts
                ));

            } catch (Exception e) {
                System.err.println("Cannot read save file " + f + ": " + e.getMessage());
            }
        }

        return result;
    }

    // ==== LOAD ====

    /**
     * Loads a full {@link GameData} instance from a given save file name.
     * <p>
     * Note: reconstructing the {@link Game} / controller / views from this data is handled elsewhere.
     * </p>
     *
     * @param fileName the save file name (must exist inside the {@code save/} directory)
     * @return the loaded {@link GameData}
     * @throws IllegalArgumentException if {@code fileName} is null or blank
     * @throws RuntimeException         if the save file does not exist or cannot be read
     */
    public static GameData loadGameData(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName must not be empty");
        }

        File file = new File(getSaveDir(), fileName);
        if (!file.exists()) {
            throw new RuntimeException("Save file not found: " + file.getAbsolutePath());
        }

        Gson gson = new Gson();
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, GameData.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read save file " + file, e);
        }
    }

    /**
     * Deletes a save file if it exists.
     *
     * @param fileName the save file name to delete
     */
    public static void deleteSave(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        File file = new File(getSaveDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
