package mnm.mods.util;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Represents a color and provides an easy way to convert to and from html color
 * codes.
 */
public class Color {

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    /**
     * Creates a color using an html color code.
     *
     * @param hexColor The html color code
     */
    public Color(int hexColor) {
        this.alpha = hexColor >> 24 & 255;
        this.red = hexColor >> 16 & 255;
        this.green = hexColor >> 8 & 255;
        this.blue = hexColor & 255;
    }

    /**
     * Creates a color using RGBA color format.
     *
     * @param red Red color
     * @param green Green color
     * @param blue Blue color
     * @param alpha Transparency
     */
    public Color(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Gets the html hex color code of this color usable by Minecraft's
     * {@link net.minecraft.client.gui.Gui}.
     *
     * @return The html hex code.
     */
    public int getColor() {
        int alphaI = alpha << 24;
        int redI = red << 16;
        int greenI = green << 8;
        int blueI = blue;
        return alphaI + redI + greenI + blueI;
    }

    /**
     * Gets the red value.
     *
     * @return The red value
     */
    public int getRed() {
        return red;
    }

    /**
     * Gets the green value.
     *
     * @return The green value
     */
    public int getGreen() {
        return green;
    }

    /**
     * Gets the blue value.
     *
     * @return The blue value
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Gets the alpha value.
     *
     * @return The alpha value
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * Convenience method for getting the html code for a RGBA color. Creates a
     * new object and calls {@link Color#getColor()}.
     *
     * @param red The red value
     * @param green The green value
     * @param blue The blue value
     * @param alpha The alpha value
     * @return The hexadecimal representation of this color.
     */
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
