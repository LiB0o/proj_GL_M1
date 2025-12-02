package gl.morpion.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gl.morpion.adapters.SymbolViewAdapter;
import javafx.scene.image.ImageView;

public class GsonConfigurations {

    public static Gson configureGson() {
        // Créer une instance Gson en configurant le TypeAdapter personnalisé pour SymbolView
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ImageView.class, new SymbolViewAdapter())
                .create();

        return gson;
    }
}
