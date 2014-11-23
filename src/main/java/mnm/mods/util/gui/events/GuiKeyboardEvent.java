package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

public class GuiKeyboardEvent extends GuiEvent {

    public final char character;
    public final int key;
    public final long time;

    public GuiKeyboardEvent(GuiComponent component, int key, char character, long time) {
        super(component);
        this.key = key;
        this.character = character;
        this.time = time;
    }
}
