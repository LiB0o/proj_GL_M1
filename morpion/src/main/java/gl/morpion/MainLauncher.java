package gl.morpion;

import gl.morpion.controllers.menu.MainMenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainLauncher extends Application {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;

    @Override
    public void start(Stage stage) {
        // 🔹 Conteneur racine UNIQUE pour tous les écrans
        StackPane root = new StackPane();

        // 🔹 Une seule Scene pour tout le jeu
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // 🔹 CSS global
        var css = getClass().getResource("/css/menu.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        // 🔹 Contrôleur principal du menu
        MainMenuController controller = new MainMenuController(stage, root);

        // (optionnel) ESC = retour au menu
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                controller.showMainMenu();
            }
        });

        // Afficher le menu au démarrage
        controller.showMainMenu();

        stage.setTitle("Morpion – GL M1");
        stage.setScene(scene);     // ⚠️ On ne changera plus la Scene après ça
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
