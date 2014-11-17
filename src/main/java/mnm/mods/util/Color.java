package mnm.mods.util;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Color {

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public Color(int hexColor) {
        this.alpha = hexColor >> 24 & 255;
        this.red = hexColor >> 16 & 255;
        this.green = hexColor >> 8 & 255;
        this.blue = hexColor & 255;
    }

    public Color(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int getColor() {
        int alphaI = alpha << 24;
        int redI = red << 16;
        int greenI = green << 8;
        int blueI = blue;
        return alphaI + redI + greenI + blueI;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        return new Color(red, green, blue, alpha).getColor();
    }

    public static class ColorAdapter extends TypeAdapter<Color> {

        private static final String RED = "r";
        private static final String GREEN = "g";
        private static final String BLUE = "b";
        private static final String ALPHA = "a";

        @Override
        public void write(JsonWriter writer, Color color) throws IOException {
            writer.beginObject()
            .name(RED).value(color.getRed())
            .name(GREEN).value(color.getGreen())
            .name(BLUE).value(color.getBlue())
            .name(ALPHA).value(color.getAlpha())
            .endObject();
        }

        @Override
        public Color read(JsonReader reader) throws IOException {
            int red = 0, green = 0, blue = 0, alpha = 127;
            reader.beginObject();
            while (reader.hasNext()) {
                String next = reader.nextName();
                if (next.equals(RED)) {
                    red = reader.nextInt();
                } else if (next.equals(GREEN)) {
                    green = reader.nextInt();
                } else if (next.equals(BLUE)) {
                    blue = reader.nextInt();
                } else if (next.equals(ALPHA)) {
                    alpha = reader.nextInt();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

            return new Color(red, green, blue, alpha);
        }
    }
}
