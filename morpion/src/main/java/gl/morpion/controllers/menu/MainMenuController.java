package gl.morpion.controllers.menu;

import gl.morpion.controllers.GameController;
import gl.morpion.vue.menu.GameBoardWithMenuView;
import gl.morpion.vue.menu.MainMenuView;
import gl.morpion.vue.menu.ModePlaceholderView;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class MainMenuController {

    private final Stage stage;

    public MainMenuController(Stage stage) {
        this.stage = stage;
    }

    public void showMainMenu() {
        MainMenuView menu = new MainMenuView(this);
        stage.setScene(new Scene(menu, 1400, 900));
    }

    public void showMode(String modeName) {
        ModePlaceholderView view = new ModePlaceholderView(
                "Je suis dans " + modeName,
                this::showMainMenu
        );
        stage.setScene(new Scene(view, 1400, 900));
    }

    public void startModePvp() {
        GameController gameController = new GameController();

        // Vue avec bouton "Retour" intégré
        GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                gameController.getGameBoardView(),
                this::showMainMenu  // action du bouton Retour
        );

        // CSS global
        Scene scene = new Scene(gameView, 1400, 900);
        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        // ESC pour revenir au menu (en bonus)
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) showMainMenu();
        });

        // Branchement logique de jeu (fin → retour menu)
        gameController.handleGame(this::showMainMenu);

        stage.setScene(scene);
    }

    public void openSettings() { /* à faire plus tard */ }
    public void showRules()    { showMode("Rules"); }
    public void toggleLanguage(String code) { /* à faire plus tard */ }
}