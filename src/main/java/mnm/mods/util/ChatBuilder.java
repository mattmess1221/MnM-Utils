package mnm.mods.util;

import java.util.ArrayList;
import java.util.List;

import mnm.mods.util.text.FancyChatComponent;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentScore;
import net.minecraft.util.ChatComponentSelector;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

//TODO Move to text package
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

    public ChatBuilder underline(Color color) {
        asFancy().getFancyStyle().setUnderline(color);
        return this;
    }

    public ChatBuilder highlight(Color color) {
        asFancy().getFancyStyle().setHighlight(color);
        return this;
    }

    private FancyChatComponent asFancy() {
        if (!(current instanceof FancyChatComponent)) {
            current = new FancyChatComponent(current);
        }
        return (FancyChatComponent) current;
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
        } else if (this.chat != current && current != null && !(this.chat instanceof FancyChatComponent && ((FancyChatComponent) this.chat).getChat() == current
                || this.current instanceof FancyChatComponent && ((FancyChatComponent) current).getChat() == this.chat
                || this.current instanceof FancyChatComponent && this.chat instanceof FancyChatComponent
                        && ((FancyChatComponent) this.chat).getChat() == ((FancyChatComponent) this.current).getChat())) {
            // XXX: logic is complex
            this.chat.appendSibling(current);
        }
        current = chat;
        return this;
    }

    public int size() {
        if (chat == null)
            return 0;
        int size = chat.getSiblings().size() + 1;
        if (current != null) {
            size++;
        }
        return size;
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
        IChatComponent chat = append(null).chat;
        if (chat == null)
            chat = new ChatComponentText("");
        return chat;
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
