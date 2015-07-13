package mnm.mods.util.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import mnm.mods.util.gui.BorderLayout.Position;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.Gui;

/**
 * TODO: Horizontal scrolling
 *
 * @author Matthew
 */
public class GuiScrollingPanel extends GuiPanel implements GuiMouseAdapter {

    private GuiPanel panel;

    public GuiScrollingPanel() {
        super(new BorderLayout());
        this.panel = new GuiPanel();
        this.panel.setBounds(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        GuiPanel panel = new GuiPanel();
        panel.addComponent(this.panel);
        this.addComponent(panel, Position.CENTER);
        this.addComponent(new Scrollbar(), Position.EAST);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        Point actual = getActualPosition();
        Rectangle rect = getBounds();

        glEnable(GL_SCISSOR_TEST);
        glScissor(actual.x * 2, mc.displayHeight - rect.height * 2 - actual.y * 2,
                rect.width * 2, rect.height * 2);

        super.drawComponent(mouseX, mouseY);

        glDisable(GL_SCISSOR_TEST);
    }

    @Override
    public void accept(GuiMouseEvent event) {
        if (event.event == GuiMouseEvent.SCROLLED) {
            Rectangle rect = panel.getBounds();
            rect.y += event.scroll / 12;

            Rectangle prect = panel.getParent().getBounds();
            Dimension dim = panel.getMinimumSize();
            if (rect.y + dim.height < prect.height) {
                rect.y = prect.height - dim.height;
            }
            if (rect.y > 0)
                rect.y = 0;
        }
    }

    public GuiPanel getContentPanel() {
        return panel;
    }

    @Override
    public Dimension getMinimumSize() {
        return getBounds().getSize();
    }

    // TODO Make draggable
    private class Scrollbar extends GuiComponent {

        @Override
        public void drawComponent(int mouseX, int mouseY) {
            int scroll = panel.getBounds().y;
            int max = GuiScrollingPanel.this.getBounds().height;
            int total = panel.getMinimumSize().height;
            if (total <= max) {
                return;
            }
            total -= max;
            Gui.drawRect(0, 20, 10, 10, -1);
            int size = Math.max(max / 2, 10);
            float perc = ((float) scroll / (float) total) * ((float) size / (float) max);
            int pos = (int) (-perc * max);

            Gui.drawRect(-1, pos, 0, pos + size - 1, -1);
            super.drawComponent(mouseX, mouseY);
        }
    }
}
