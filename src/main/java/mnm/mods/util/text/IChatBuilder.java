package mnm.mods.util.text;

import mnm.mods.util.Color;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public interface IChatBuilder {

    /**
     * Sets the formatting in the style of the current chat.
     *
     * @param f The format
     * @return This builder
     */
    IChatBuilder format(EnumChatFormatting f);

    IChatBuilder color(Color color);

    IChatBuilder underline(Color color);

    IChatBuilder highlight(Color color);

    IChatBuilder click(ClickEvent event);

    IChatBuilder hover(HoverEvent event);

    IChatBuilder insertion(String insertion);

    IChatBuilder score(String player, String objective);

    IChatBuilder text(String text);

    IChatBuilder selector(Selector selector);

    /**
     * Starts the creation of a translation. After calling, any call to
     * {@link #append(IChatComponent)} won't be immediately appended to the
     * component. Instead, it will be added to a list of arguments for the
     * translation. To end the translation and allow it to be appended to the
     * chat, call {@link #endTranslation()}.
     *
     * @param key The translation key
     * @return A translation builder
     */
    IChatBuilder translation(String key);

    /**
     * Quickly translates the given key with zero arguments or formatting.
     * Immidiently ends the translation. Same as calling
     * {@code translation(key).end()}
     *
     * @param key
     * @return
     */
    IChatBuilder quickTranslate(String key);

    /**
     * Used for builders that build multiple chats.
     *
     * @return This builder
     */
    IChatBuilder next();

    /**
     * Ends a translation so it can be appended to the chat.
     *
     * @return
     */
    IChatBuilder end();

    /**
     * Appends the current chat to the chat and makes the provided value the
     * current.
     *
     * @param chat The new current value
     * @return
     */
    IChatBuilder append(IChatComponent chat);

    /**
     * Appends the current chat (if any) to the chat and returns the built chat.
     *
     * @return The chat
     */
    IChatComponent build();

}