package mnm.mods.util.gui;

import mnm.mods.util.text.FancyFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

/**
 * Gui component label used to show text on the screen.
 *
 * @author Matthew
 */
public class GuiLabel extends GuiComponent {

    private FancyFontRenderer fr;
    private IChatComponent string;
    private float angle;

    public GuiLabel() {
        this.fr = new FancyFontRenderer(Minecraft.getMinecraft().fontRendererObj);
    }

    /**
     * Creates a label from a chat component.
     *
     * @param chat The text
     */
    public GuiLabel(IChatComponent chat) {
        this();
        this.string = chat;
    }

    public GuiLabel(IChatComponent string, float angle) {
        this(string);
        this.angle = angle % 360;
    }

    /**
     * Creates a label from a string
     *
     * @param string The string
     * @deprecated Use {@link #GuiLabel(IChatComponent)}
     */
    @Deprecated
    public GuiLabel(String string) {
        this(string, 0);
    }

    /**
     * Creates a label from a string and angle.
     *
     * @param string The string
     * @param angle The angle
     */
    @Deprecated
    public GuiLabel(String string, float angle) {
        this(new ChatComponentText(string), 0);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (string == null)
            return;
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 0, 0, angle);
        if (angle < 180) {
            GlStateManager.translate(-angle / 1.5, -angle / 4, 0);
        } else {
            GlStateManager.translate(-angle / 15, angle / 40, 0);
        }

        fr.drawChat(string, getBounds().x, getBounds().y, true);

        GlStateManager.popMatrix();
        super.drawComponent(mouseX, mouseY);
    }

    /**
     * Sets the string of this label
     *
     * @param string The string
     */
    public void setString(IChatComponent string) {
        this.string = string;
    }

    /**
     * Gets the string of this label
     *
     * @return The string
     */
    public IChatComponent getString() {
        return string;
    }

}
