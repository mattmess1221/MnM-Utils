package mnm.mods.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public interface ILocation {

    int getXPos();

    int getYPos();

    int getWidth();

    int getHeight();

    default int getXWidth() {
        return getXPos() + getWidth();
    }

    default int getYHeight() {
        return getYPos() + getHeight();
    }

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

    default boolean contains(ILocation r) {

        if (this.getXPos() >= r.getXWidth())
            return false;
        if (this.getXWidth() <= r.getXPos())
            return false;
        if (this.getYPos() >= r.getYHeight())
            return false;
        if (this.getYHeight() <= r.getYPos())
            return false;
        return true;
    }
}
