package mnm.mods.util.gui.events;

import java.awt.Point;

import mnm.mods.util.gui.GuiComponent;

public class GuiMouseEvent extends GuiEvent {

    private final Point position;
    private final int button;

    public GuiMouseEvent(GuiComponent component, Point point, int click) {
        super(component);
        this.position = point;
        this.button = click;
    }

    public Point getPosition() {
        return position;
    }

    public int getButton() {
        return button;
    }

}
