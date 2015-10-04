package mnm.mods.util.events;

import java.util.Map;

import org.apache.logging.log4j.LogManager;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mumfrey.liteloader.transformers.event.EventInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenHandler {

    private static Map<Class<? extends GuiScreen>, Function<? extends GuiScreen, GuiScreen>> screens = Maps.newHashMap();

    private static boolean eventRunning;

    public static void onDisplayGui(EventInfo<Minecraft> event, GuiScreen screen) {
        if (eventRunning)
            return;
        try {
            eventRunning = true;
            if (screen != null && handleScreen(screen)) {
                event.cancel();
            }
        } catch (Throwable ex) {
            LogManager.getLogger().warn("Caught error while setting screen.", ex);
        } finally {
            eventRunning = false;
        }
    }

    private static <T extends GuiScreen> boolean handleScreen(T screen) {
        @SuppressWarnings("unchecked")
        Function<T, GuiScreen> func = (Function<T, GuiScreen>) screens.get(screen.getClass());
        if (func != null) {
            GuiScreen screen2 = func.apply(screen);
            if (screen != screen2) {
                Minecraft.getMinecraft().displayGuiScreen(screen2);
                return true;
            }
        }
        return false;
    }

    public static <From extends GuiScreen> void addHandler(Class<From> screen, Function<From, GuiScreen> func) {
        screens.put(screen, func);
    }
}
