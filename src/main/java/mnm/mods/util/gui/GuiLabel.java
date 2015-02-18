package mnm.mods.util.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;

public class GuiLabel extends GuiComponent {

    private static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    private String string;
    private float angle;

    public GuiLabel(IChatComponent chat) {
        this(chat.getFormattedText(), 0);
    }

    public GuiLabel(String string) {
        this(string, 0);
    }

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
        String[] split = string.split("\r?\n");
        for (String s : split) {
            fr.drawString(s, 0, y, getForeColor());
            y += fr.FONT_HEIGHT;
        }
        GlStateManager.popMatrix();
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

}
