package mnm.mods.util.gui.events;

import java.awt.Point;

import mnm.mods.util.gui.GuiComponent;

public class GuiMouseWheelEvent extends GuiMouseEvent {

    private final int wheelDirection;

    public GuiMouseWheelEvent(GuiComponent component, Point point, int click, int wheel) {
        super(component, point, click);
        this.wheelDirection = wheel;
    }

    public GuiMouseWheelEvent(GuiMouseEvent event, int scroll) {
        this(event.getComponent(), event.getPosition(), event.getButton(), scroll);
    }

    public int getWheelDirection() {
        return wheelDirection;
    }
}
