package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

/**
 * A event for the mouse adapter.
 *
 * @author Matthew
 */
public class GuiMouseEvent extends AbstractMouseEvent {

    private MouseEvent event;
    private int button;
    private int scroll;

    public GuiMouseEvent(GuiComponent component, MouseEvent event, int mouseX, int mouseY, int button) {
        this(component, event, mouseX, mouseY, button, 0);
    }

    public GuiMouseEvent(GuiComponent component, MouseEvent event, int mouseX, int mouseY, int button, int scroll) {
        super(component, mouseX, mouseY);
        this.event = event;
        this.button = button;
        this.scroll = scroll;
    }

    public MouseEvent getEvent() {
        return event;
    }

    public int getButton() {
        return button;
    }

    public int getScroll() {
        return scroll;
    }

    public static enum MouseEvent {
        RAW,
        ENTER,
        EXIT,
        HOVER,
        PRESS,
        CLICK,
        RELEASE,
        SCROLL,
        DRAG,
        MOVE;
    }
}
