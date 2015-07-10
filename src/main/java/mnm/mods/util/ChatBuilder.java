package mnm.mods.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentScore;
import net.minecraft.util.ChatComponentSelector;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public final class ChatBuilder {

    private IChatComponent chat;
    private IChatComponent current;

    private boolean isTranslation;
    private String translationKey;
    private List<Object> translationArgs;

    /**
     * Sets the formatting in the style of the current chat.
     *
     * @param f The format
     * @return This builder
     */
    public ChatBuilder format(EnumChatFormatting f) {
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

    public ChatBuilder click(ClickEvent event) {
        checkCreated();
        current.getChatStyle().setChatClickEvent(event);
        return this;
    }

    public ChatBuilder hover(HoverEvent event) {
        checkCreated();
        current.getChatStyle().setChatHoverEvent(event);
        return this;
    }

    public ChatBuilder insertion(String insertion) {
        checkCreated();
        current.getChatStyle().setInsertion(insertion);
        return this;
    }

    private void checkCreated() {
        if (current == null) {
            throw new IllegalStateException("A chat component has not been created yet.");
        }
    }

    public ChatBuilder score(String player, String objective) {
        return append(new ChatComponentScore(player, objective));
    }

    public ChatBuilder text(String text) {
        return append(new ChatComponentText(text));
    }

    public ChatBuilder selector(Selector selector) {
        return append(new ChatComponentSelector(selector.toString()));
    }

    /**
     * Starts the creation of a translation. After calling, any call to
     * {@link #append(IChatComponent)} won't be immediately appended to the
     * component. Instead, it will be added to a list of arguments for the
     * translation. To end the translation and allow it to be appended to the
     * chat, call {@link #endTranslation()}.
     *
     * @param key The translation key
     * @return This builder
     */
    public ChatBuilder startTranslation(String key) {
        isTranslation = true;
        this.translationKey = key;
        this.translationArgs = new ArrayList<Object>();
        return this;
    }

    /**
     * Ends a translation so it can be appended to the chat.
     *
     * @return
     */
    public ChatBuilder endTranslation() {
        isTranslation = false;
        append(new ChatComponentTranslation(translationKey, translationArgs.toArray()));
        translationKey = null;
        translationArgs = null;
        return this;
    }

    /**
     * Appends the current chat to the chat and makes the provided value the
     * current.
     *
     * @param chat The new current value
     * @return
     */
    public ChatBuilder append(IChatComponent chat) {
        if (isTranslation) {
            translationArgs.add(chat);
        } else if (this.chat == null) {
            this.chat = chat;
        } else if (this.chat != current && current != null) {
            this.chat.appendSibling(current);
        }
        current = chat;
        return this;
    }

    /**
     * Appends the current chat (if any) to the chat and returns the built chat.
     *
     * @return The chat
     */
    public IChatComponent build() {
        if (isTranslation) {
            throw new IllegalStateException("There is an unfinished translation in progress.");
        }
        return append(null).chat;
    }

    public static enum Selector {

        PLAYER('p'),
        ALL('a'),
        ENTITY('e'),
        RANDOM('r');

        private char id;

        private Selector(char c) {
            this.id = c;
        }

        @Override
        public String toString() {
            return "@" + id;
        }
    }
}
