package mnm.mods.util;

import mnm.mods.util.update.UpdateChecker;

public class MnmUtils {

    private static MnmUtils instance;

    private IChatProxy chatProxy = new DefaultChatProxy();

    private MnmUtils() {
        UpdateChecker.runUpdateChecks();
    }
    public static MnmUtils getInstance() {
        if (instance == null) {
            instance = new MnmUtils();
        }
        return instance;
    }

    public IChatProxy getChatProxy() {
        return chatProxy;
    }

    public void setChatProxy(IChatProxy chatProxy) {
        if (chatProxy == null) {
            chatProxy = new DefaultChatProxy();
        }
        this.chatProxy = chatProxy;
    }
}
