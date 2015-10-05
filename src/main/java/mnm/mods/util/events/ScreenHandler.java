package mnm.mods.util.events;

import net.minecraft.client.gui.GuiScreen;

/**
 * Handler used to redirect and replace GuiScreens.
 */
public interface ScreenHandler {

    /**
     * Adds a redirect to the given class.
     *
     * @param cl The owning class to redirect
     * @param redirect The object that handles this redirect
     */
    <T extends GuiScreen> void addHandler(Class<T> cl, IScreenRedirect<T> redirect);
}
