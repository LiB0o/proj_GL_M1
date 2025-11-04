package gl.morpion.controllers.menu;
import gl.morpion.vue.player.PlayerNamesView;
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
        PlayerNamesView namesView = new PlayerNamesView(
                // onStart: reçoit (name1, name2)
                (name1, name2) -> {
                    // Créer le GameController avec les deux noms (voir point 3)
                    GameController gameController = new GameController(name1, name2);

                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu // bouton Retour intégré
                    );

                    Scene scene = new Scene(gameView, 1400, 900);

                    // garder le même CSS !
                    var css = getClass().getResource("/css/menu.css");
                    if (css != null) scene.getStylesheets().add(css.toExternalForm());

                    // Escape pour revenir au menu
                    scene.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ESCAPE) showMainMenu();
                    });

                    // logiques de fin de partie → retour au menu
                    gameController.handleGame(this::showMainMenu);

                    stage.setScene(scene);
                },
                // onBack → retour au menu principal
                this::showMainMenu
        );

        Scene scene = new Scene(namesView, 1400, 900);

        // garder le même CSS !
        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
    }


    public void openSettings() { /* à faire plus tard */ }
    public void showRules()    { showMode("Rules"); }
    public void toggleLanguage(String code) { /* à faire plus tard */ }
}