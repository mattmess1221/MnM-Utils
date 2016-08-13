package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public interface ILocation {

    int getXPos();

    int getYPos();

    int getWidth();

    int getHeight();

    default Location copy() {
        return Location.copyOf(this);
    }

    default Point getPoint() {
        return new Point(getXPos(), getYPos());
    }

    default Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    default Rectangle asRectangle() {
        return new Rectangle(getXPos(), getYPos(), getWidth(), getHeight());
    }

}
