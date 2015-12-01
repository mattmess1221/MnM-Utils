package mnm.mods.util.text;

import mnm.mods.util.Color;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentScore;
import net.minecraft.util.ChatComponentSelector;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public abstract class AbstractChatBuilder implements IChatBuilder {

    protected IChatComponent current;

    @Override
    public IChatBuilder format(EnumChatFormatting f) {
        checkCreated();
        if (f.isColor()) {
            current.getChatStyle().setColor(f);
        } else if (f.isFancyStyling()) {
            if (f == EnumChatFormatting.BOLD) {
                current.getChatStyle().setBold(true);
            } else if (f == EnumChatFormatting.ITALIC) {
                current.getChatStyle().setItalic(true);
            } else if (f == EnumChatFormatting.UNDERLINE) {
                current.getChatStyle().setUnderlined(true);
            } else if (f == EnumChatFormatting.STRIKETHROUGH) {
                current.getChatStyle().setStrikethrough(true);
            } else if (f == EnumChatFormatting.OBFUSCATED) {
                current.getChatStyle().setObfuscated(true);
            }
        } else if (f == EnumChatFormatting.RESET) {
            current.getChatStyle().setColor(null);
            current.getChatStyle().setBold(false);
            current.getChatStyle().setItalic(false);
            current.getChatStyle().setUnderlined(false);
            current.getChatStyle().setStrikethrough(false);
            current.getChatStyle().setObfuscated(false);
        }
        return this;
    }

    @Override
    public IChatBuilder color(Color color) {
        asFancy().getFancyStyle().setColor(color);
        return this;
    }

    @Override
    public IChatBuilder underline(Color color) {
        asFancy().getFancyStyle().setUnderline(color);
        return this;
    }

    @Override
    public IChatBuilder highlight(Color color) {
        asFancy().getFancyStyle().setHighlight(color);
        return this;
    }

    private FancyChatComponent asFancy() {
        if (!(current instanceof FancyChatComponent)) {
            current = new FancyChatComponent(current);
        }
        return (FancyChatComponent) current;
    }

    @Override
    public IChatBuilder click(ClickEvent event) {
        checkCreated();
        current.getChatStyle().setChatClickEvent(event);
        return this;
    }

    @Override
    public IChatBuilder hover(HoverEvent event) {
        checkCreated();
        current.getChatStyle().setChatHoverEvent(event);
        return this;
    }

    @Override
    public IChatBuilder insertion(String insertion) {
        checkCreated();
        current.getChatStyle().setInsertion(insertion);
        return this;
    }

    private void checkCreated() {
        if (current == null) {
            throw new IllegalStateException("A chat component has not been created yet.");
        }
    }

    @Override
    public IChatBuilder score(String player, String objective) {
        return append(new ChatComponentScore(player, objective));
    }

    @Override
    public IChatBuilder text(String text) {
        return append(new ChatComponentText(text));
    }

    @Override
    public IChatBuilder selector(Selector selector) {
        return append(new ChatComponentSelector(selector.toString()));
    }

    @Override
    public IChatBuilder translation(String key) {
        return new TranslationBuilder(this, key);
    }

    @Override
    public IChatBuilder quickTranslate(String key) {
        return translation(key).end();
    }

}
