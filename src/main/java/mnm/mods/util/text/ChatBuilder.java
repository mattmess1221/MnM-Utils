package mnm.mods.util.text;

import net.minecraft.util.IChatComponent;

public class ChatBuilder extends AbstractChatBuilder {

    private IChatComponent chat;

    /**
     * Used for builders that build multiple chats.
     *
     * @return This builder
     */
    @Override
    public IChatBuilder next() {
        throw new UnsupportedOperationException();
    }

    /**
     * Ends a translation so it can be appended to the chat.
     *
     * @return
     */
    @Override
    public IChatBuilder end() {
        throw new UnsupportedOperationException();
    }

    /**
     * Appends the current chat to the chat and makes the provided value the
     * current.
     *
     * @param chat The new current value
     * @return
     */
    @Override
    public ChatBuilder append(IChatComponent chat) {

        if (current != null) {
            if (this.chat == null)
                this.chat = current;
            else
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
    @Override
    public IChatComponent build() {
        return append(null).chat;
    }
}
