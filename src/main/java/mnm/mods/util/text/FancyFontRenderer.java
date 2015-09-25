package mnm.mods.util.text;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.IChatComponent;

public class FancyFontRenderer extends Gui {

    private final FontRenderer fontRenderer;

    public FancyFontRenderer(FontRenderer fr) {
        this.fontRenderer = fr;
    }

    public void drawChat(IChatComponent chat, int x, int y, boolean shadow) {

        int x1 = x;
        List<IChatComponent> list = (chat.getSiblings());
        list = Lists.newArrayList(list);
        list.add(0, chat);
        for (IChatComponent c : list) {
            if (c instanceof FancyChatComponent) {
                FancyChatComponent fcc = (FancyChatComponent) c;
                int length = fontRenderer.getStringWidth(c.getUnformattedText());
                drawRect(x1, y, x1 + length, y - fontRenderer.FONT_HEIGHT, fcc.getFancyStyle().getHighlight().getColor());
                drawHorizontalLine(x1, x1 + length, y + fontRenderer.FONT_HEIGHT - 1, fcc.getFancyStyle().getUnderline().getColor());
            }

            x1 += fontRenderer.getStringWidth(c.getUnformattedTextForChat());
        }
        fontRenderer.drawString(chat.getFormattedText(), x, y, -1, shadow);
    }

}
