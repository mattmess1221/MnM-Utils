package mnm.mods.util.gui.events;

import java.awt.Point;

import mnm.mods.util.gui.GuiComponent;

/**
 * A event for the mouse adapter.
 *
 * @author Matthew
 */
public class GuiMouseEvent extends GuiEvent {

    /**
     * Denotes that this event has nothing special. Sent once at the beginning.
     */
    public static final int RAW = -1;
    /**
     * Denotes that the cursor has entered the {@link GuiComponent}.
     */
    public static final int ENTERED = 0;
    /**
     * Denotes that the cursor has exited the {@link GuiComponent}.
     */
    public static final int EXITED = 1;
    /**
     * Denotes that cursor is hovered over the {@link GuiComponent}.
     */
    public static final int HOVERED = 2;
    /**
     * Denotes that a mouse button has been pressed.
     */
    public static final int PRESSED = 3;
    /**
     * Denotes that a button has been pressed, then released.
     */
    public static final int CLICKED = 4;
    /**
     * Denotes that a button has been released.
     */
    public static final int RELEASED = 5;
    /**
     * Denotes that the scroll wheel has been moved. The
     * {@link GuiMouseEvent#scroll} field will not be 0.
     */
    public static final int SCROLLED = 6;
    /**
     * Denotes that a mouse button is held and the cursor is moving.
     */
    public static final int DRAGGED = 7;
    /**
     * Denotes that the mouse has moved.
     */
    public static final int MOVED = 8;

    /**
     * The event called
     */
    public int event;
    /**
     * The position of the cursor
     */
    public final Point position;
    /**
     * The mouse button clicked
     */
    public final int button;
    /**
     * How much has been scrolled
     */
    public final int scroll;

    public GuiMouseEvent(GuiComponent component, int event, Point point, int button) {
        this(component, event, point, button, 0);
    }

    public GuiMouseEvent(GuiComponent component, int event, Point point, int button, int scroll) {
        super(component);
        this.event = event;
        this.position = point;
        this.button = button;
        this.scroll = scroll;
    }
}
