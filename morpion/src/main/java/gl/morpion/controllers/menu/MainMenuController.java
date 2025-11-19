package gl.morpion.controllers.menu;
import gl.morpion.model.Player;
import gl.morpion.model.Symbol;
import gl.morpion.model.TypeOfSymbol;
import gl.morpion.view.player.PlayerNamesView;
import gl.morpion.controllers.GameController;
import gl.morpion.view.menu.GameBoardWithMenuView;
import gl.morpion.view.menu.MainMenuView;
import gl.morpion.view.menu.ModePlaceholderView;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class MainMenuController {
    private final Stage stage;
    private final int WIDTH = 800, HEIGHT = 900;
    public MainMenuController(Stage stage) {
        this.stage = stage;
    }
    public void showMainMenu() {
        MainMenuView menu = new MainMenuView(this);
        stage.setScene(new Scene(menu, WIDTH, HEIGHT));
    }

    public void showMode(String modeName) {
        if(modeName == "QUIT"){
            Stage s = (Stage)stage.getScene().getWindow();
            s.close();
        }
        ModePlaceholderView view = new ModePlaceholderView("Je suis dans " + modeName,
                this::showMainMenu
        );
        stage.setScene(new Scene(view, WIDTH, HEIGHT));
    }
    public void startModePvp() {
        PlayerNamesView namesView = new PlayerNamesView(
                // onStart: reçoit (name1, name2)
                (name1, name2) -> {
                    Player p1 = new Player(name1, 0, new Symbol(getClass().getResource("/gl/morpion/croix.jpg").toExternalForm(), TypeOfSymbol.CROSS));
                    Player p2 = new Player(name2, 0, new Symbol(getClass().getResource("/gl/morpion/cercle.png").toExternalForm(), TypeOfSymbol.CIRCLE));
                    // Créer le GameController avec les deux noms (voir point 3)
                    GameController gameController = new GameController(p1, p2); // le gzmr recupere le nom des joueurs;
                    //afficher la grille
                    GameBoardWithMenuView gameView = new GameBoardWithMenuView(
                            gameController.getGameBoardView(),
                            this::showMainMenu // bouton Retour intégré
                    );
                    Scene scene = new Scene(gameView, WIDTH, HEIGHT);

                    // garder le même CSS !
                    var css = getClass().getResource("/css/menu.css");
                    if (css != null) scene.getStylesheets().add(css.toExternalForm());

                    // Escape pour revenir au menu
                    scene.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ESCAPE) showMainMenu();
                    });

                    // logiques de fin de partie → retour au menu
                    gameController.handleGame(this::showMainMenu);
                    //gameController.initializeGame();

                    stage.setScene(scene);
                },
                // onBack → retour au menu principal
                this::showMainMenu
        );

        System.out.println("PlayerNameView passe bien");
        //System.out.println("P1 = "+gameController.getListPlayers().get(0)+ "P2 = "+gameController.getListPlayers().get(2));


        Scene scene = new Scene(namesView, WIDTH, HEIGHT);
        // garder le même CSS !
        var css = getClass().getResource("/css/menu.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
    }


    public void openSettings() { /* à faire plus tard */ }
    public void showRules()    { showMode("Rules"); }
    public void toggleLanguage(String code) { /* à faire plus tard */ }
}