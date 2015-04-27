package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

/**
 * A class for the gui events.
 *
 * @author Matthew
 */
public abstract class GuiEvent {

    /**
     * The component clicked.
     */
    public final GuiComponent component;

    public GuiEvent(GuiComponent component) {
        this.component = component;
    }
}
