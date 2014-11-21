package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiListener;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseWheelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

public abstract class GuiComponent extends Gui {

    public boolean enabled = true;
    public boolean visible = true;
    public boolean hovered = false;

    protected Minecraft mc = Minecraft.getMinecraft();

    private int backColor = 0;
    private int foreColor = -1;
    private GuiPanel parent;
    private Rectangle bounds;
    private List<GuiListener> listeners = Lists.newArrayList();
    private boolean held;

    public GuiComponent() {
        this(new Rectangle());
    }

    public GuiComponent(int x, int y, int w, int h) {
        this(new Rectangle(x, y, w, h));
    }

    /* Everything will be assigned here */
    public GuiComponent(Rectangle bounds) {
        this.bounds = bounds;
    }

    public abstract void drawComponent(int mouseX, int mouseY);

    protected void drawBorders(int x1, int y1, int x2, int y2) {
        int val = 0xaa;
        Gui.drawRect(x1 - 1, y1 - 1, x1, y2, getBackColor() + val << 24); // left
        Gui.drawRect(x1 - 1, y1 - 1, x2 + 1, y1, getBackColor() + val << 24); // top
        Gui.drawRect(x2, y1 - 1, x2 + 1, y2 + 1, getBackColor() + val << 24); // right
        Gui.drawRect(x1 - 1, y2, x2 + 1, y2 + 1, getBackColor() + val << 24); // bottom
    }

    public void updateComponent() {
    }

    public void handleMouseInput() {
        int mouseX = Mouse.getEventX() * mc.currentScreen.width / mc.displayWidth;
        int mouseY = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height
                / mc.displayHeight - 1;
        Point actual = getActualPosition();
        int scroll = Mouse.getEventDWheel();
        GuiMouseEvent event = new GuiMouseEvent(this, new Point(mouseX, mouseY),
                Mouse.getEventButton());
        for (GuiListener listener : this.listeners) {
            if (mouseX != 0 && mouseY != 0 && listener instanceof GuiMouseAdapter) {
                // mouse moved
                ((GuiMouseAdapter) listener).mouseMoved(event);
                if (event.getButton() != -1) {
                    // mouse dragged
                    ((GuiMouseAdapter) listener).mouseDragged(event);
                }
            }
            if (scroll != 0 && listener instanceof GuiMouseAdapter) {
                // wheel moved
                GuiMouseWheelEvent wheelEvent = new GuiMouseWheelEvent(event, scroll);
                ((GuiMouseAdapter) listener).mouseWheelMoved(wheelEvent);
            }
            if (mouseX > actual.x && mouseX < actual.x + bounds.width && mouseY > actual.y
                    && mouseY < actual.y + bounds.height) {
                if (listener instanceof ActionPerformed && event.getButton() == 0
                        && !Mouse.getEventButtonState()) {
                    // left button released
                    ((ActionPerformed) listener).actionPerformed(event);
                }
                if (listener instanceof GuiMouseAdapter) {
                    if (event.getButton() != -1) {
                        if (Mouse.getEventButtonState()) {
                            // button pressed
                            this.held = true;
                            ((GuiMouseAdapter) listener).mousePressed(event);
                        } else {
                            // button released
                            ((GuiMouseAdapter) listener).mouseReleased(event);
                            if (held) {
                                // button clicked
                                ((GuiMouseAdapter) listener).mouseClicked(event);
                            }
                        }
                    }
                    if (!hovered) {
                        // mouse entered
                        hovered = true;
                        ((GuiMouseAdapter) listener).mouseEntered(event);
                    } else {
                        // mouse left
                        ((GuiMouseAdapter) listener).mouseHovered(event);
                    }
                }
            } else {
                if (hovered && listener instanceof GuiMouseAdapter) {
                    // mouse exited
                    hovered = false;
                    this.held = false;
                    ((GuiMouseAdapter) listener).mouseExited(event);
                }
            }

        }

    }

    public void handleKeyboardInput() {
        // TODO Auto-generated method stub

    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setBounds(int x, int y, int width, int height) {
        setBounds(new Rectangle(x, y, width, height));
    }

    public void setPosition(int xPos, int yPos) {
        getBounds().x = xPos;
        getBounds().y = yPos;
    }

    public void setSize(int width, int height) {
        getBounds().width = width;
        getBounds().height = height;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public void addEventListener(GuiListener listener) {
        this.listeners.add(listener);
    }

    public GuiPanel getParent() {
        return this.parent;
    }

    void setParent(GuiPanel guiPanel) {
        this.parent = guiPanel;
    }

    public Point getActualPosition() {
        Point point = new Point(bounds.x, bounds.y);
        if (getParent() != null) {
            Point parent = getParent().getActualPosition();
            point.x += parent.x;
            point.y += parent.y;
        }
        return point;
    }

    public abstract Dimension getPreferedSize();

    public int getBackColor() {
        int result = backColor;
        if (getParent() != null && result == 0) {
            result = getParent().getBackColor();
        }
        return result;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public int getForeColor() {
        int result = foreColor;
        if (getParent() != null && result == -1) {
            result = getParent().getForeColor();
        }
        return result;
    }

    public void setForeColor(int foreColor) {
        this.foreColor = foreColor;
    }

}
