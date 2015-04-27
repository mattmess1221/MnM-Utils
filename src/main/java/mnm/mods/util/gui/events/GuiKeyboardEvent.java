package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

import org.lwjgl.input.Keyboard;

/**
 * An event for a keyboard adapter
 *
 * @author Matthew
 */
public class GuiKeyboardEvent extends GuiEvent {

    /**
     * The character typed
     */
    public final char character;
    /**
     * The key code typed
     *
     * @see Keyboard
     */
    public final int key;
    /**
     * The time in miliseconds the key was held for.
     */
    public final long time;

    public GuiKeyboardEvent(GuiComponent component, int key, char character, long time) {
        super(component);
        this.key = key;
        this.character = character;
        this.time = time;
    }
}
