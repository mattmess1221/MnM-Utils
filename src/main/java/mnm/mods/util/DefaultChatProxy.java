package mnm.mods.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class DefaultChatProxy implements IChatProxy {

    @Override
    public void addToChat(String channel, String msg) {
        String text = String.format("[%s] %s", channel, msg);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
    }
}
