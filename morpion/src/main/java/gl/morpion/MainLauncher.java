package gl.morpion;

import gl.morpion.controllers.menu.MainMenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainLauncher extends Application {

    @Override
    public void start(Stage stage) {
        MainMenuController controller = new MainMenuController(stage);
        controller.showMainMenu();              // setScene(menu) via le contrôleur
        stage.setTitle("Morpion – GL M1");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
