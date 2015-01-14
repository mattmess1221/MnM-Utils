package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiKeyboardAdapter;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiListener;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseWheelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

public abstract class GuiComponent extends Gui {

    public boolean enabled = true;
    public boolean visible = true;
    public boolean hovered = false;
    private boolean entered = false;
    public boolean held;

    protected Minecraft mc = Minecraft.getMinecraft();

    private int backColor = 0;
    private int foreColor = -1;
    private GuiPanel parent;
    private Rectangle bounds;
    private Dimension minimumSize = new Dimension();
    private List<GuiListener> listeners = Lists.newArrayList();

    public GuiComponent() {
        this(new Rectangle());
    }

    public GuiComponent(int x, int y, int w, int h) {
        this(new Rectangle(x, y, w, h));
    }

    /* Everything will be assigned here */
    public GuiComponent(Rectangle bounds) {
        this.setBounds(bounds);
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
        if (mc.currentScreen != null) {
            int mouseX = Mouse.getX() * mc.currentScreen.width / mc.displayWidth;
            int mouseY = mc.currentScreen.height - Mouse.getY() * mc.currentScreen.height
                    / mc.displayHeight - 1;
            Point actual = getActualPosition();
            int scroll = Mouse.getEventDWheel();
            GuiMouseEvent event = new GuiMouseEvent(this, new Point(mouseX, mouseY),
                    Mouse.getEventButton());

            if (mouseX > actual.x && mouseX < actual.x + getBounds().width && mouseY > actual.y
                    && mouseY < actual.y + getBounds().height) {
                if (!hovered) {
                    this.entered = true;
                }
                this.hovered = true;
            } else {
                if (!hovered) {
                    this.entered = false;
                }
                this.hovered = false;
            }
            if (Mouse.getEventButtonState()) {
                this.held = false;
            }
            for (GuiListener listener : this.listeners) {
                if (listener instanceof GuiMouseAdapter) {
                    ((GuiMouseAdapter) listener).handleRaw();

                    if (hovered || held) {
                        if (Mouse.getEventDX() != 0 && Mouse.getEventDY() != 0) {
                            // mouse moved
                            ((GuiMouseAdapter) listener).mouseMoved(event);
                            if (held) {
                                // mouse dragged
                                ((GuiMouseAdapter) listener).mouseDragged(event);
                            }
                        }
                        if (event.getButton() != -1) {
                            if (Mouse.getEventButtonState()) {
                                // button pressed
                                this.held = true;
                                ((GuiMouseAdapter) listener).mousePressed(event);
                            } else if (!Mouse.getEventButtonState()) {
                                // button released
                                ((GuiMouseAdapter) listener).mouseReleased(event);
                                if (held) {
                                    // button clicked
                                    ((GuiMouseAdapter) listener).mouseClicked(event);
                                }
                                this.held = false;
                            }
                        }
                    }

                    if (scroll != 0) {
                        // wheel moved
                        GuiMouseWheelEvent wheelEvent = new GuiMouseWheelEvent(event, scroll);
                        ((GuiMouseAdapter) listener).mouseWheelMoved(wheelEvent);
                    }
                }
                if (hovered) {
                    if (listener instanceof ActionPerformed && event.getButton() == 0
                            && !Mouse.getEventButtonState()) {
                        // left button released
                        ((ActionPerformed) listener).actionPerformed(event);
                    }
                    if (listener instanceof GuiMouseAdapter) {

                        if (entered) {
                            // mouse entered
                            ((GuiMouseAdapter) listener).mouseEntered(event);
                        } else {
                            // mouse left
                            ((GuiMouseAdapter) listener).mouseHovered(event);
                        }
                    }
                } else {
                    if (entered && listener instanceof GuiMouseAdapter) {
                        // mouse exited
                        hovered = false;
                        ((GuiMouseAdapter) listener).mouseExited(event);
                    }
                }

            }
        }
    }

    public void handleKeyboardInput() {
        int key = Keyboard.getEventKey();
        char character = Keyboard.getEventCharacter();
        long time = Keyboard.getEventNanoseconds();
        GuiKeyboardEvent event = new GuiKeyboardEvent(this, key, character, time);
        for (GuiListener listener : this.listeners) {
            if (listener instanceof GuiKeyboardAdapter) {
                ((GuiKeyboardAdapter) listener).keyTyped(event);
            }
        }
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
        Point point = new Point(getBounds().x, getBounds().y);
        if (getParent() != null) {
            Point parent = getParent().getActualPosition();
            point.x += parent.x;
            point.y += parent.y;
        }
        return point;
    }

    public void setMinimumSize(Dimension size) {
        this.minimumSize = size;
    }

    public Dimension getMinimumSize() {
        return minimumSize;
    }

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
