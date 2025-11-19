package gl.morpion.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

import static java.lang.System.out;

public class SymbolViewAdapter extends TypeAdapter<ImageView> {
    @Override
    public void write(JsonWriter jsonWriter, ImageView imageView) throws IOException {
        if (imageView == null) {
            jsonWriter.name("symbol").value("None");
            return;
        }

        Image image = imageView.getImage();
        if (image == null) {
            jsonWriter.name("symbol").value("None");
            return;
        }

        // Write the image URL in the JSON file
        jsonWriter.value(image.getUrl());
    }

    @Override
    public ImageView read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
