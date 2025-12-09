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
 * High-level save / load manager built on top of SaveBoard / LoadBoard.
 *
 * - saveGame(...) : crée un GameData complet et l'écrit dans save/<nom>.json
 * - listSaves()   : liste tous les fichiers .json dans save/
 * - loadGameData(fileName) : lit un GameData à partir d'un fichier
 */
public final class SaveManager {

    private static final String SAVE_DIR_NAME = "save";
    private static final DateTimeFormatter TS_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SaveManager() {
        // static-only
    }

    // ==== utilitaires fichiers ====

    private static File getProjectRoot() {
        File currentDir = new File(System.getProperty("user.dir"));
        if ("morpion".equals(currentDir.getName())) {
            return currentDir.getParentFile();
        }
        return currentDir;
    }

    private static File getSaveDir() {
        File root = getProjectRoot();
        File dir = new File(root, SAVE_DIR_NAME);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    private static String normalizeFileName(String raw) {
        if (raw == null || raw.isBlank()) {
            return "save_" + System.currentTimeMillis();
        }
        // remplace espaces / caractères bizarres
        String s = raw.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
        if (!s.toLowerCase().endsWith(".json")) {
            s = s + ".json";
        }
        return s;
    }

    // ==== SAVE ====

    /**
     * Sauvegarde la partie actuelle dans un fichier dédié.
     *
     * @param game         l'objet Game (pour la board, les joueurs, etc.)
     * @param boardView    la vue du plateau (sert à SaveBoard)
     * @param saveName     nom choisi par l'utilisateur
     * @param mode         mode de jeu (PVP, PVBOT, ...)
     * @param botDifficulty niveau du bot (ou null si pas de bot)
     * @param winCondition  nb de symboles pour gagner
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

        // 1) On réutilise ton SaveBoard pour construire un GameData "de base"
        SaveBoard sb = new SaveBoard(boardView);
        sb.saveBoard(game); // écrit dans save/save.json

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

        // 2) On enrichit avec les métadonnées
        GameBoard gb = game.getGameBoard();

        data.setSaveName(saveName);
        data.setMode(mode != null ? mode.name() : null);
        data.setBotDifficulty(botDifficulty);
        data.setRows(gb != null ? gb.getRow() : null);
        data.setCols(gb != null ? gb.getColumn() : null);
        data.setWinCondition(winCondition);
        data.setSavedAt(LocalDateTime.now().format(TS_FORMAT));

        // 3) On écrit dans un fichier dédié
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
     * Retourne la liste de toutes les sauvegardes disponibles dans save/.
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
     * Charge le GameData complet à partir d'un nom de fichier.
     * (La reconstruction du Game / GameController se fait ailleurs).
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
     * Supprime une sauvegarde.
     */
    public static void deleteSave(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        File file = new File(getSaveDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
