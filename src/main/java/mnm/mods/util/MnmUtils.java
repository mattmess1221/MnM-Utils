package mnm.mods.util;

public class MnmUtils {

    public static final MnmUtils INSTANCE = new MnmUtils();

    private IChatProxy chatProxy = new DefaultChatProxy();

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
