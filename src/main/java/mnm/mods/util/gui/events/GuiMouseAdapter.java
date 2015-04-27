package mnm.mods.util.gui.events;

/**
 * Interface for handling mouse events. Multiple events get called to this, so
 * be sure to check the event that was called with the constants in
 * {@link GuiMouseEvent}.
 *
 * @author Matthew
 */
public interface GuiMouseAdapter {

    /**
     * Called for mouse events. Can be called up to 10 times for each event.
     *
     * @param event
     */
    void accept(GuiMouseEvent event);

}
