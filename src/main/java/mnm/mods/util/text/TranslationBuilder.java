package mnm.mods.util.text;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

class TranslationBuilder extends AbstractChatBuilder {

    private final IChatBuilder parent;
    private final String translationKey;
    private List<Object> translationArgs = Lists.newArrayList();

    private IChatComponent buffer;

    TranslationBuilder(IChatBuilder parent, String key) {
        this.parent = parent;
        this.translationKey = key;
    }

    @Override
    public IChatBuilder next() {
        translationArgs.add(append(null).buffer);
        buffer = null;
        return this;
    }

    @Override
    public IChatBuilder end() {
        if (buffer != null)
            translationArgs.add(append(null).buffer);
        return parent.append(new ChatComponentTranslation(translationKey, translationArgs));
    }

    @Override
    public IChatComponent build() {
        throw new IllegalStateException("Translation in progress.");
    }

    @Override
    public TranslationBuilder append(IChatComponent chat) {
        if (current != null) {
            if (this.buffer == null)
                buffer = current;
            else
                this.buffer.appendSibling(current);
        }
        current = chat;
        return this;
    }
}