package gl.morpion.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gl.morpion.model.Symbol;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class SymbolViewAdapter {
    public void write(JsonWriter jsonWriter, String imageUrl) throws IOException {
        if (imageUrl == null) {
            jsonWriter.name("symbol").value("None");
            return;
        }
        // Write the image URL in the JSON file
        //String imageUrl = imageView.getImage().getUrl();
        if(imageUrl.contains("cercle.png")) {
            jsonWriter.name("symbol").value("cercle.png");
        }else if(imageUrl.contains("croix.jpg")) {
            jsonWriter.name("symbol").value("croix.jpg");
        }else {
            jsonWriter.name("symbol").value("None");
        }
    }

    //TODO : load
    public ImageView read(JsonReader jsonReader) throws IOException {
        String symbol = jsonReader.nextString();
        return createImageViewFromSymbol(symbol);
    }

    private ImageView createImageViewFromSymbol(String symbolUrl) {
        ImageView imageView = new ImageView();
        if(symbolUrl != null) {
            imageView.setImage(new Image(symbolUrl));
        } else {
            imageView.setImage(null);
        }
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        return imageView;
    }
}
