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

    public GuiLabel(String string, int x, int y) {
        super(x, y, fr.getStringWidth(string), fr.FONT_HEIGHT);
        this.string = string;
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

        fr.drawString(string, 0, y, getForeColor());
        GlStateManager.popMatrix();
    }

}
