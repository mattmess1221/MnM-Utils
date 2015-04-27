package mnm.mods.util;

import java.util.Random;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a color and provides an easy way to convert to and from html color
 * codes.
 */
public class Color {

    private static Random random = new Random();

    @SerializedName("r")
    private final int red;
    @SerializedName("g")
    private final int green;
    @SerializedName("b")
    private final int blue;
    @SerializedName("a")
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
        this.red = red % 256;
        this.green = green % 256;
        this.blue = blue % 256;
        this.alpha = alpha % 256;
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

    /**
     * Generates a random color.
     *
     * @return A random color
     */
    public static Color random() {
        return new Color(random.nextInt());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color) {
            Color color = (Color) obj;
            return color.alpha == alpha && color.red == red && color.green == green
                    && color.blue == blue;
        }
        return false;
    }

}
