package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.EnumMap;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

/**
 * A recreation of Border Layout.
 *
 * @author Matthew Matthew
 * @see java.awt.BorderLayout
 */
public class BorderLayout implements ILayout {

    private EnumMap<Position, GuiComponent> components = Maps.newEnumMap(Position.class);

    @Override
    public void addComponent(GuiComponent comp, Object constraints) {
        if (constraints == null || constraints instanceof Position) {
            addComponent((Position) constraints, comp);
        } else {
            throw new IllegalArgumentException(
                    "cannot add to layout: constraint must be a Position enum");
        }
    }

    private synchronized void addComponent(Position constraint, GuiComponent comp) {
        if (constraint == null) {
            constraint = Position.CENTER;
        }

        components.put(constraint, comp);
    }

    @Override
    public synchronized void removeComponent(GuiComponent comp) {
        for (Entry<Position, GuiComponent> component : components.entrySet()) {
            if (component.getValue().equals(comp)) {
                components.remove(component.getKey());
                break;
            }
        }
    }

    @Override
    public void layoutComponents(GuiPanel parent) {
        Rectangle pbounds = parent.getBounds();
        GuiComponent center = components.get(Position.CENTER);
        GuiComponent north = components.get(Position.NORTH);
        GuiComponent south = components.get(Position.SOUTH);
        GuiComponent west = components.get(Position.WEST);
        GuiComponent east = components.get(Position.EAST);

        if (north != null) {
            Rectangle bounds = north.getBounds();

            bounds.x = 0;
            bounds.y = 0;
            bounds.width = pbounds.width;
            bounds.height = north.getMinimumSize().height;
        }

        if (west != null) {
            Rectangle bounds = west.getBounds();
            bounds.x = 0;
            bounds.width = west.getMinimumSize().width;

            if (north == null) {
                bounds.y = 0;
            } else {
                bounds.y = north.getBounds().height;
            }

            if (south == null) {
                if (north == null) {
                    bounds.height = pbounds.height;
                } else {
                    bounds.height = pbounds.height - north.getBounds().height;
                }
            } else {
                if (north == null) {
                    bounds.height = pbounds.height - south.getBounds().height;
                } else {
                    bounds.height = pbounds.height - south.getBounds().height
                            - north.getBounds().height;
                }
            }
        }

        if (center != null) {
            Rectangle bounds = center.getBounds();

            if (north == null) {
                bounds.y = 0;
            } else {
                bounds.y = north.getBounds().height + 1;
            }

            if (west == null) {
                bounds.x = 0;
            } else {
                bounds.x = west.getBounds().width;
            }

            if (east == null) {
                if (west == null) {
                    bounds.width = pbounds.getBounds().width;
                } else {
                    bounds.width = pbounds.getBounds().width - west.getBounds().width;
                }
            } else {
                if (west == null) {
                    bounds.width = pbounds.getBounds().width - east.getBounds().width;
                } else {
                    bounds.width = pbounds.getBounds().width - east.getBounds().width
                            - west.getBounds().width;
                }
            }

            if (south == null) {
                if (north == null) {
                    bounds.height = pbounds.getBounds().height;
                } else {
                    bounds.height = pbounds.getBounds().height - north.getBounds().height;
                }
            } else {
                if (north == null) {
                    bounds.height = pbounds.getBounds().height - south.getBounds().height - 1;
                } else {
                    bounds.height = pbounds.getBounds().height - south.getBounds().height
                            - north.getBounds().height - 2;
                }
            }
        }

        if (east != null) {
            Rectangle bounds = east.getBounds();

            bounds.x = pbounds.width - east.getMinimumSize().width;
            bounds.width = east.getMinimumSize().width;
            if (north == null) {
                bounds.y = 0;
            } else {
                bounds.y = north.getBounds().height;
            }
            if (south == null) {
                if (north == null) {
                    bounds.height = pbounds.height;
                } else {
                    bounds.height = pbounds.height - north.getMinimumSize().height;
                }
            } else {
                if (north == null) {
                    bounds.height = pbounds.height - south.getMinimumSize().height;
                } else {
                    bounds.height = pbounds.height - south.getMinimumSize().height
                            - north.getMinimumSize().height;
                }
            }
        }

        if (south != null) {
            Rectangle bounds = south.getBounds();

            bounds.x = 0;
            bounds.width = pbounds.width;
            bounds.height = south.getMinimumSize().height;
            bounds.y = pbounds.height - bounds.height;
        }
    }

    @Override
    public Dimension getLayoutSize() {
        int width = 0;
        int height = 0;
        for (Entry<Position, GuiComponent> comp : components.entrySet()) {
            Dimension size = comp.getValue().getMinimumSize();
            switch (comp.getKey()) {
            case CENTER:
                width += size.width;
                height += size.height;
                break;
            case EAST:
            case WEST:
                width += size.width;
                break;
            case NORTH:
            case SOUTH:
                height += size.height;
                break;
            }
        }
        return new Dimension(width, height);
    }

    public static enum Position {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        CENTER;
    }
}
