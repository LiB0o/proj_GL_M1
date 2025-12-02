package gl.morpion.view.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue pour le mode Custom avec option de chargement d'un plateau sauvegardé.
 * Affiche un message et un bouton pour charger un plateau existant.
 */
public class CustomView extends StackPane {

    /**
     * Constructor: Crée une vue Custom avec deux boutons de chargement (PvP et PvB).
     * 
     * @param onLoadPvP Callback exécuté quand le bouton "Load PvP" est cliqué
     * @param onLoadPvB Callback exécuté quand le bouton "Load PvB" est cliqué
     * @param onBack Callback exécuté quand le bouton "Retour" est cliqué
     */
    public CustomView(Runnable onLoadPvP, Runnable onLoadPvB, Runnable onBack) {
        // Taille préférée pour la vue (1200x800 pixels)
        setPrefSize(1200, 800);

        // Fond
        // Créer une région de fond avec un style de dégradé
        Region bg = new Region();
        bg.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0f14, #14202a);");
        // Lier les dimensions du fond à la taille du conteneur parent (responsive)
        bg.prefWidthProperty().bind(widthProperty());
        bg.prefHeightProperty().bind(heightProperty());

        // Contenu centré
        // Créer un label de message avec texte blanc et police en gras
        Label msg = new Label("Mode Custom");
        msg.setTextFill(Color.WHITE);
        msg.setFont(Font.font("Montserrat", FontWeight.BOLD, 36));

        Label subMsg = new Label("Load a saved board");
        subMsg.setTextFill(Color.web("#e8f6ff"));
        subMsg.setFont(Font.font("Montserrat", FontWeight.NORMAL, 18));

        // Create load PvP button
        Button loadPvPBtn = new Button("Load Player vs Player");
        loadPvPBtn.getStyleClass().add("big-button");
        loadPvPBtn.setOnAction(e -> {
            if (onLoadPvP != null) {
                onLoadPvP.run();
            }
        });

        // Create load PvB button
        Button loadPvBBtn = new Button("Load Player vs Bot");
        loadPvBBtn.getStyleClass().add("big-button");
        loadPvBBtn.setOnAction(e -> {
            if (onLoadPvB != null) {
                onLoadPvB.run();
            }
        });

        // Create back button
        Button back = new Button("← Back");
        back.setStyle(
                "-fx-background-radius:12; -fx-background-color: rgba(255,255,255,0.10);" +
                        "-fx-text-fill: white; -fx-padding: 8 18 8 18; -fx-border-color: rgba(255,255,255,0.35); -fx-border-width:1.2; -fx-border-radius:12;"
        );
        // Attach click handler that executes the onBack callback
        back.setOnAction(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        // Create a vertical box to center the message and buttons with 16px spacing
        VBox box = new VBox(16, msg, subMsg, loadPvPBtn, loadPvBBtn, back);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(16));

        // Ajouter le fond et la boîte de contenu au graphe de scène (en couches)
        getChildren().addAll(bg, box);
    }
}

