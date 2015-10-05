package mnm.mods.util.events;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;

/**
 * Handles screen replacements.
 *
 * @param <Screen> The type of screen to redirect
 */
public interface IScreenRedirect<Screen extends GuiScreen> {

    /**
     * Called to try to redirect a {@link GuiScreen}. Returning {@code null}
     * will display no screen (or the main menu if not in-game). Returning the
     * {@code input} will have no effect on the current screen.
     *
     * @param input The original screen
     * @return The screen to display
     */
    @Nullable
    GuiScreen redirect(Screen input);
}
