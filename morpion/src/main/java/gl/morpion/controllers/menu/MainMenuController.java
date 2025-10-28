package gl.morpion.controllers.menu;

import gl.morpion.vue.menu.MainMenuView;
import gl.morpion.vue.menu.ModePlaceholderView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private final Stage stage;

    public MainMenuController(Stage stage) { this.stage = stage; }

    public void showMainMenu() {
        MainMenuView menu = new MainMenuView(this);
        stage.setScene(new Scene(menu, 1200, 800));
    }

    public void showMode(String modeName) {
        ModePlaceholderView view = new ModePlaceholderView(
                "Je suis dans " + modeName,
                this::showMainMenu
        );
        stage.setScene(new Scene(view, 1200, 800));
    }

    public void openSettings() { /* à faire plus tard */ }
    public void showRules()    { showMode("Rules"); }
    public void toggleLanguage(String code) { /* à faire plus tard */ }
}
