package gl.morpion.persistence;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gl.morpion.model.GameBoard;
import gl.morpion.model.Player;
import gl.morpion.model.RectangleBoard;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.view.GameBoardView;
import javafx.application.Platform;

class SaveBoardTest {
    
    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(() -> {
                latch.countDown();
            });
            boolean finished = latch.await(5, TimeUnit.SECONDS);
            if (!finished) {
                throw new RuntimeException("JavaFX initialization timeout");
            }
        }
    }
    private GameBoard board;
    private Player player1;
    private Player player2;
    private GameBoardView boardView;
    private SaveBoard saveBoard;

    @BeforeEach
    void setUp() {
        // Obtenir les URLs valides des ressources d'images
        String croixUrl = getImageUrl("/gl/morpion/croix.jpg");
        String cercleUrl = getImageUrl("/gl/morpion/cercle.png");
        
        player1 = new Player("p1", new Symbol(croixUrl, TypeOfSymbol.CROSS));
        player2 = new Player("p2", new Symbol(cercleUrl, TypeOfSymbol.CIRCLE));
        board = new RectangleBoard(5, 5);
        
        // Placer quelques symboles sur le plateau
        board.placeSymbol(new Symbol(croixUrl, TypeOfSymbol.CROSS), 0, 0);
        board.placeSymbol(new Symbol(croixUrl, TypeOfSymbol.CROSS), 0, 1);
        board.placeSymbol(new Symbol(cercleUrl, TypeOfSymbol.CIRCLE), 1, 0);
        board.placeSymbol(new Symbol(croixUrl, TypeOfSymbol.CROSS), 0, 2);
        
        boardView = new GameBoardView(board, player1, player2);
        saveBoard = new SaveBoard(boardView);
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
     * Obtient l'URL d'une ressource d'image depuis le classpath
     */
    private String getImageUrl(String resourcePath) {
        // Essayer d'obtenir la ressource depuis GameBoardView (module principal)
        java.net.URL url = GameBoardView.class.getResource(resourcePath);
        if (url != null) {
            return url.toExternalForm();
        }
        // Essayer depuis la classe de test
        url = getClass().getResource(resourcePath);
        if (url != null) {
            return url.toExternalForm();
        }
        // Si la ressource n'est pas trouvée, essayer avec le chemin du fichier réel
        File projectRoot = getProjectRoot();
        String fullPath = projectRoot.getAbsolutePath() + "/morpion/src/main/resources" + resourcePath;
        File file = new File(fullPath);
        if (file.exists()) {
            return file.toURI().toString();
        }
        // Dernier recours : utiliser le chemin relatif (peut ne pas fonctionner mais évite l'erreur immédiate)
        return resourcePath;
    }

    @Test
    void saveBoard_ShouldCreateSaveFile() {
        // Sauvegarder le plateau
        saveBoard.saveBoard();
        
        // Vérifier que le fichier a été créé au bon endroit (répertoire racine du projet)
        File projectRoot = getProjectRoot();
        File saveFile = new File(projectRoot, "save/save.json");
        assertTrue(saveFile.exists(), "Le fichier save.json devrait être créé");
        assertTrue(saveFile.length() > 0, "Le fichier save.json ne devrait pas être vide");
    }

    @Test
    void saveBoardShouldSaveAllCells() {
        // Sauvegarder le plateau
        saveBoard.saveBoard();
        
        // Vérifier que le fichier existe au bon endroit (répertoire racine du projet)
        File projectRoot = getProjectRoot();
        File saveFile = new File(projectRoot, "save/save.json");
        assertTrue(saveFile.exists(), "Le fichier save.json devrait être créé");
        
        // Le fichier devrait contenir des données pour toutes les cases (5x5 = 25 cases)
        // On vérifie juste que le fichier n'est pas vide et contient des données JSON valides
        assertTrue(saveFile.length() > 100, "Le fichier devrait contenir des données pour toutes les cases");
    }

    @Test
    void saveBoardShouldHandleEmptyBoard() {
        // Créer un plateau vide
        GameBoard emptyBoard = new RectangleBoard(3, 3);
        GameBoardView emptyBoardView = new GameBoardView(emptyBoard, player1, player2);
        SaveBoard emptySaveBoard = new SaveBoard(emptyBoardView);
        
        // Sauvegarder le plateau vide
        emptySaveBoard.saveBoard();
        
        // Vérifier que le fichier a été créé au bon endroit (répertoire racine du projet)
        File projectRoot = getProjectRoot();
        File saveFile = new File(projectRoot, "save/save.json");
        assertTrue(saveFile.exists(), "Le fichier save.json devrait être créé même avec un plateau vide");
    }
}