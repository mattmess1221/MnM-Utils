package mnm.mods.util.text;

import java.util.Iterator;
import java.util.List;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class FancyChatComponent implements IChatComponent {

    private final IChatComponent chat;
    private FancyChatStyle style;

    public FancyChatComponent(IChatComponent parent) {
        if (parent instanceof FancyChatComponent)
            throw new IllegalArgumentException("Parent chat cannot be fancy");
        this.chat = parent;
    }

    public FancyChatComponent(String string) {
        this(new ChatComponentText(string));
    }

    @Override
    public String getUnformattedTextForChat() {
        return chat.getUnformattedTextForChat();
    }

    @Override
    public IChatComponent createCopy() {
        IChatComponent chat = this.chat.createCopy();
        FancyChatComponent fcc = new FancyChatComponent(chat);
        fcc.setFancyStyle(getFancyStyle().createCopy());
        return fcc;
    }

    @Override
    public IChatComponent appendSibling(IChatComponent component) {
        chat.appendSibling(component);
        return this;
    }

    @Override
    public IChatComponent appendText(String text) {
        chat.appendText(text);
        return this;
    }

    @Override
    public ChatStyle getChatStyle() {
        return chat.getChatStyle();
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle style) {
        chat.setChatStyle(style);
        return this;
    }

    @Override
    public String getFormattedText() {
        return chat.getFormattedText();
    }

    @Override
    public List<IChatComponent> getSiblings() {
        return chat.getSiblings();
    }

    @Override
    public String getUnformattedText() {
        return chat.getUnformattedText();
    }

    @Override
    public Iterator<IChatComponent> iterator() {
        return chat.iterator();
    }

    public IChatComponent getChat() {
        return chat;
    }

    public FancyChatStyle getFancyStyle() {
        if (style == null)
            style = new FancyChatStyle();
        return style;
    }

    public void setFancyStyle(FancyChatStyle style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return String.format("FancyChat{chat=%s, fancystyle=%s}", chat, style);
    }
}
