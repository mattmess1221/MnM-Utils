package mnm.mods.util.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;

/**
 * Gui component label used to show text on the screen.
 *
 * @author Matthew
 */
public class GuiLabel extends GuiComponent {

    private static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    private String string;
    private float angle;

    /**
     * Creates a label from an chat component.
     *
     * @param chat The text
     */
    public GuiLabel(IChatComponent chat) {
        this(chat.getFormattedText(), 0);
    }

    /**
     * Creates a label from a string
     *
     * @param string The string
     */
    public GuiLabel(String string) {
        this(string, 0);
    }

    /**
     * Creates a label from a string and angle.
     *
     * @param string The string
     * @param angle The angle
     */
    public GuiLabel(String string, float angle) {
        this.string = string;
        this.angle = angle % 360;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {

        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 0, 0, angle);
        if (angle < 180) {
            GlStateManager.translate(-angle / 1.5, -angle / 4, 0);
        } else {
            GlStateManager.translate(-angle / 15, angle / 40, 0);
        }

        int y = getBounds().height / 2 - fr.FONT_HEIGHT / 2;
        String[] split = getString().split("\r?\n");
        for (String s : split) {
            fr.drawString(s, 0, y, getForeColor());
            y += fr.FONT_HEIGHT;
        }
        GlStateManager.popMatrix();
        super.drawComponent(mouseX, mouseY);
    }

    /**
     * Sets the string of this label
     *
     * @param string The string
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * Gets the string of this label
     *
     * @return The string
     */
    public String getString() {
        return string;
    }

}
