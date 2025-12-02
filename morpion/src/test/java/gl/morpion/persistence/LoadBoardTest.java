package gl.morpion.persistence;

import gl.morpion.model.*;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LoadBoardTest {
    private Game game;
    private GameBoard board;
    private Player p1;
    private Player p2;

    @BeforeEach
    void setUp() {
        board = new RectangleBoard(10, 10);
        p1 = new Player("P1", new Symbol("croix.jpg", TypeOfSymbol.CROSS));
        p2 = new Player("p2", new Symbol("cercle.png", TypeOfSymbol.CIRCLE));
        game = new Game(board, p1, p2, p1);
    }

    @Test
    void readJsonFromFile_ShouldLoadSymbolsCorrectly() {
        // Créer un fichier de sauvegarde de test
        createTestSaveFile();
        
        // Charger le plateau
        LoadBoard loadBoard = new LoadBoard(game);
        loadBoard.readJsonFromFile();
        
        // Vérifier que les symboles ont été chargés
        HashMap<Pair<Integer, Integer>, Symbol> usedCase = game.getUsedCase();
        assertNotNull(usedCase, "La HashMap usedCase ne devrait pas être null");
        
        // Vérifier que les symboles ont été restaurés sur le plateau
        Symbol symbolAt00 = board.getSymbolInCase(0, 0);
        assertNotNull(symbolAt00, "Un symbole devrait être présent à la position (0,0)");
        assertTrue(symbolAt00.getImage().contains("croix"), "Le symbole à (0,0) devrait être une croix");
        
        Symbol symbolAt01 = board.getSymbolInCase(0, 1);
        assertNotNull(symbolAt01, "Un symbole devrait être présent à la position (0,1)");
        assertTrue(symbolAt01.getImage().contains("cercle"), "Le symbole à (0,1) devrait être un cercle");
    }

    @Test
    void readJsonFromFile_ShouldHandleEmptyCells() {
        // Créer un fichier de sauvegarde avec des cases vides
        createTestSaveFileWithEmptyCells();
        
        // Charger le plateau
        LoadBoard loadBoard = new LoadBoard(game);
        loadBoard.readJsonFromFile();
        
        // Vérifier que les cases vides ne sont pas dans usedCase
        HashMap<Pair<Integer, Integer>, Symbol> usedCase = game.getUsedCase();
        
        // Les cases avec "None" ne devraient pas être dans usedCase
        for (int i = 0; i < board.getRow(); i++) {
            for (int j = 0; j < board.getColumn(); j++) {
                if (board.isEmptyCase(i, j)) {
                    Pair<Integer, Integer> key = new Pair<>(i, j);
                    assertFalse(usedCase.containsKey(key), 
                        "Les cases vides ne devraient pas être dans usedCase");
                }
            }
        }
    }

    @Test
    void readJsonFromFile_ShouldThrowExceptionWhenFileNotFound() {
        // Supprimer le fichier s'il existe
        File projectRoot = getProjectRoot();
        File saveFile = new File(projectRoot, "save/save.json");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        
        // Essayer de charger un fichier inexistant
        LoadBoard loadBoard = new LoadBoard(game);
        assertThrows(RuntimeException.class, () -> {
            loadBoard.readJsonFromFile();
        }, "Devrait lancer une exception si le fichier n'existe pas");
    }

    @Test
    void readJsonFromFile_ShouldClearBoardBeforeLoading() {
        // Placer quelques symboles sur le plateau initial
        board.placeSymbol(new Symbol("croix.jpg", TypeOfSymbol.CROSS), 5, 5);
        board.placeSymbol(new Symbol("cercle.png", TypeOfSymbol.CIRCLE), 6, 6);
        
        // Créer un fichier de sauvegarde avec d'autres symboles
        createTestSaveFile();
        
        // Charger le plateau
        LoadBoard loadBoard = new LoadBoard(game);
        loadBoard.readJsonFromFile();
        
        // Vérifier que les anciens symboles ont été supprimés
        assertTrue(board.isEmptyCase(5, 5), "La case (5,5) devrait être vide après le chargement");
        assertTrue(board.isEmptyCase(6, 6), "La case (6,6) devrait être vide après le chargement");
        
        // Vérifier que les nouveaux symboles sont présents
        assertFalse(board.isEmptyCase(0, 0), "La case (0,0) devrait contenir un symbole après le chargement");
    }

    /**
     * Obtient le répertoire racine du projet (proj_GL_M1)
     * @return Le répertoire racine du projet
     */
    private File getProjectRoot() {
        File currentDir = new File(System.getProperty("user.dir"));
        
        // Si on est dans le dossier morpion, remonter d'un niveau
        if (currentDir.getName().equals("morpion")) {
            return currentDir.getParentFile();
        }
        
        // Sinon, on est probablement déjà dans le répertoire racine
        return currentDir;
    }

    /**
     * Crée un fichier de sauvegarde de test avec quelques symboles
     */
    private void createTestSaveFile() {
        try {
            File projectRoot = getProjectRoot();
            File saveDir = new File(projectRoot, "save");
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }

            File saveFile = new File(saveDir, "save.json");
            try (FileWriter writer = new FileWriter(saveFile)) {
                writer.write("[\n");
                writer.write("  {\"row\": 0, \"col\": 0, \"symbol\": \"croix.jpg\"},\n");
                writer.write("  {\"row\": 0, \"col\": 1, \"symbol\": \"cercle.png\"},\n");
                writer.write("  {\"row\": 0, \"col\": 2, \"symbol\": \"None\"}\n");
                writer.write("]");
            }
        } catch (IOException e) {
            fail("Impossible de créer le fichier de test : " + e.getMessage());
        }
    }

    /**
     * Crée un fichier de sauvegarde de test avec principalement des cases vides
     */
    private void createTestSaveFileWithEmptyCells() {
        try {
            File projectRoot = getProjectRoot();
            File saveDir = new File(projectRoot, "save");
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }

            File saveFile = new File(saveDir, "save.json");
            try (FileWriter writer = new FileWriter(saveFile)) {
                writer.write("[\n");
                writer.write("  {\"row\": 0, \"col\": 0, \"symbol\": \"None\"},\n");
                writer.write("  {\"row\": 0, \"col\": 1, \"symbol\": \"None\"},\n");
                writer.write("  {\"row\": 0, \"col\": 2, \"symbol\": \"None\"}\n");
                writer.write("]");
            }
        } catch (IOException e) {
            fail("Impossible de créer le fichier de test : " + e.getMessage());
        }
    }
}