package mnm.mods.util.events;

import com.google.common.base.Optional;

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
    <T extends GuiScreen> void addScreen(Class<T> cl, IScreenRedirect<T> redirect);

    /**
     * Gets an {@link IScreenRedirect} from a class, which may or may not exist.
     *
     * @param cl The screen class to get the replacement of
     * @return The Optional of the redirect or absent if it doesn't exist
     */
    <T extends GuiScreen> Optional<IScreenRedirect<T>> getScreen(Class<T> cl);

}
