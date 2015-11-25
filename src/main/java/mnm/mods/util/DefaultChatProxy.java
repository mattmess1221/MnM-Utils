package mnm.mods.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class DefaultChatProxy implements IChatProxy {

    @Override
    public void addToChat(String channel, IChatComponent msg) {
        IChatComponent text = new ChatComponentTranslation("[%s] %s", channel, msg);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
    }
}
