package mnm.mods.util.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IChatComponent;

public class GuiLabel extends GuiComponent {

    private static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    private String string;

    public GuiLabel(IChatComponent chat) {
        this(chat, 0, 0);
    }

    public GuiLabel(IChatComponent chat, int x, int y) {
        this(chat.getFormattedText(), x, y);
    }

    public GuiLabel(String string) {
        this(string, 0, 0);
    }

    public GuiLabel(String string, int x, int y) {
        super(x, y, fr.getStringWidth(string), fr.FONT_HEIGHT);
        this.string = string;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        fr.drawString(string, 0, 0, -1);
    }

}
