package gl.morpion;

import gl.morpion.audio.AudioManager;
import gl.morpion.controllers.menu.MainMenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX application entry point for the Morpion (Tic-Tac-Toe) project.
 * <p>
 * This launcher initializes:
 * </p>
 * <ul>
 *     <li>A single root container ({@link StackPane}) shared by all screens</li>
 *     <li>A single {@link Scene} that is never replaced (only its content changes)</li>
 *     <li>The global CSS stylesheet</li>
 *     <li>The {@link MainMenuController} responsible for navigation</li>
 * </ul>
 *
 * <p>
 * It also starts the menu music and displays the main menu at startup.
 * </p>
 */
public class MainLauncher extends Application {

    /** Default window width. */
    public static final int WIDTH = 1200;

    /** Default window height. */
    public static final int HEIGHT = 800;

    /**
     * JavaFX lifecycle method called by the framework to start the application.
     * <p>
     * Side effects:
     * </p>
     * <ul>
     *     <li>Creates the primary scene and attaches global CSS</li>
     *     <li>Instantiates the main menu controller</li>
     *     <li>Registers a keyboard handler (ESC → back to main menu)</li>
     *     <li>Starts menu music via {@link AudioManager}</li>
     *     <li>Shows the primary stage</li>
     * </ul>
     *
     * @param stage the primary application stage provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        // Root container shared by all screens
        StackPane root = new StackPane();

        // Single Scene for the entire game
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Global CSS
        var css = getClass().getResource("/css/menu.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        // Main menu controller
        MainMenuController controller = new MainMenuController(stage, root);

        // (Optional) ESC = return to main menu
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                controller.showMainMenu();
            }
        });

        // Show the main menu at startup
        AudioManager.startMenuMusic();
        controller.showMainMenu();

        stage.setTitle("Morpion – GL M1");
        stage.setScene(scene);     // The Scene will not be replaced after this
        stage.show();
    }

    /**
     * Standard Java main method.
     * <p>
     * Delegates application launch to JavaFX.
     * </p>
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
