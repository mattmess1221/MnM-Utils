package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

/**
 * A listener for keyboard events. Can be implemented as an anonymous inner
 * class or implemented in the top-level class. The {@link GuiComponent}
 * constructor automatically adds it if it is implemented.
 *
 * @author Matthew
 * @see ActionPerformed
 */
public interface GuiKeyboardAdapter {

    /**
     * Run when a keyboard event occurs
     *
     * @param event The event
     */
    void accept(GuiKeyboardEvent event);

}
