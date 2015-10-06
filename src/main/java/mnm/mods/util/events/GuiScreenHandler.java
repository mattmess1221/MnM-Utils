package mnm.mods.util.events;

import java.util.Map;

import org.apache.logging.log4j.LogManager;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.mumfrey.liteloader.transformers.event.EventInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenHandler implements ScreenHandler {

    private static GuiScreenHandler instance;
    private static boolean eventRunning;

    private Map<Class<? extends GuiScreen>, IScreenRedirect<?>> screens = Maps.newHashMap();

    public GuiScreenHandler() {
        if (instance != null) {
            throw new UnsupportedOperationException("GuiScreenHandler is already initialzied.");
        }
        instance = this;
    }

    public static void onDisplayGui(EventInfo<Minecraft> event, GuiScreen screen) {
        if (eventRunning)
            return;
        try {
            eventRunning = true;
            if (screen != null && instance.handleScreen(screen)) {
                event.cancel();
            }
        } catch (Throwable ex) {
            LogManager.getLogger().warn("Caught error while setting screen.", ex);
        } finally {
            eventRunning = false;
        }
    }

    private <T extends GuiScreen> boolean handleScreen(T screen) {
        @SuppressWarnings("unchecked")
        Optional<IScreenRedirect<T>> func = getScreen((Class<T>) screen.getClass());
        if (func.isPresent()) {
            GuiScreen screen2 = func.get().redirect(screen);
            if (screen != screen2) {
                Minecraft.getMinecraft().displayGuiScreen(screen2);
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends GuiScreen> void addScreen(Class<T> screen, IScreenRedirect<T> func) {
        screens.put(screen, func);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GuiScreen> Optional<IScreenRedirect<T>> getScreen(Class<T> cl) {
        return Optional.fromNullable((IScreenRedirect<T>) screens.get(cl));
    }
}
