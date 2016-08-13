package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.Maps;

/**
 * A layout which places components along a grid. Add components with an int[]
 * array to define bounds. Array can be of length 2 or 4, the last 2 being
 * optional. If they are not included, they default to 1.
 * <p>
 * The format of the array is <code>{ xPos, yPos, width*, height* }</code>. <br>
 * * indicates optional
 * <p>
 * <b>Example Usage:</b>
 *
 * <pre>
 * GuiPanel panel = new GuiPanel(new GuiGridLayout(10, 10));
 *
 * // creates a label and places it at x:0 y:0 and size w:1 h:1.
 * GuiLabel label = new GuiLabel(&quot;Label&quot;);
 * panel.addComponent(label, new int[] { 0, 0 });
 *
 * // creates a button and places it at x:1 y:1 with size w:3, h:2.
 * GuiButton button = new GuiButton(&quot;button&quot;);
 * panel.addComponent(button, new int[] { 1, 1, 3, 2 });
 * </pre>
 *
 * @author Matthew
 */
public class GuiGridLayout implements ILayout {

    private int cols;
    private int rows;

    private Map<Rectangle, GuiComponent> grid = Maps.newHashMap();

    public GuiGridLayout(int columns, int rows) {
        this.cols = columns;
        this.rows = rows;
    }

    @Override
    public void addComponent(GuiComponent comp, Object constraints) {
        if (constraints == null) {
            throw new IllegalArgumentException("component requires constraints.");
        } else if (!(constraints instanceof int[])) {
            throw new IllegalArgumentException("Constraints must be an int array");
        }
        addComponent(comp, (int[]) constraints);
    }

    public void addComponent(GuiComponent comp, int[] constraints) {
        if (constraints.length != 2 && constraints.length != 4) {
            throw new IllegalArgumentException("Constraints must have either 2 or 4 elements.");
        }
        int x = constraints[0];
        int y = constraints[1];
        int w = 1;
        int h = 1;
        if (constraints.length == 4) {
            w = constraints[2];
            h = constraints[3];
        }
        Rectangle rect = new Rectangle(x, y, w, h);
        try {
            checkBoundsIfValid(rect);
            grid.put(rect, comp);
        } catch (Exception e) {
            LogManager.getLogger().catching(e);
        }
    }

    /**
     * Checks if the rectangle is valid. A rectangle is valid if x and y are
     * above 0 and x + width < cols and y + height < rows
     */
    private void checkBoundsIfValid(Rectangle b) {
        if (b.x + b.width - 1 > cols || b.y + b.height - 1 > rows || b.x < 0 || b.y < 0) {
            throw new IndexOutOfBoundsException(String.format(
                    "x:%s y:%s w:%s h:%s cols:%s rows:%s", b.x, b.y, b.width, b.height, cols, rows));
        }
        for (Rectangle r : grid.keySet()) {
            if (b.contains(r)) {
                throw new IllegalArgumentException("Area " + b + " already contains "
                        + grid.get(r).getClass().getName());
            }
        }
    }

    @Override
    public void removeComponent(GuiComponent comp) {
        Rectangle remove = null;
        for (Entry<Rectangle, GuiComponent> entry : grid.entrySet()) {
            if (entry.getValue() == comp) {
                remove = entry.getKey();
                break;
            }
        }
        grid.remove(remove);
    }

    @Override
    public void layoutComponents(GuiPanel parent) {
        Dimension size = parent.getLocation().getSize();
        int colW = size.width / cols;
        int rowH = size.height / rows;
        for (Map.Entry<Rectangle, GuiComponent> entry : grid.entrySet()) {
            Rectangle bounds = entry.getKey();
            int x = bounds.x * colW;
            int width = bounds.width * colW;
            int y = bounds.y * rowH;
            int height = bounds.height * rowH;
            entry.getValue().setLocation(new Location(x, y, width, height));
        }
    }

    @Override
    public Dimension getLayoutSize() {
        return new Dimension(cols, rows);
    }

}
