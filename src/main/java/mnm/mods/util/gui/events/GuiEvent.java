package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

public abstract class GuiEvent {

    private final GuiComponent component;

    public GuiEvent(GuiComponent component) {
        this.component = component;
    }

    public GuiComponent getComponent() {
        return component;
    }
}
