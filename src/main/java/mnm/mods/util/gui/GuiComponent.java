package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiKeyboardAdapter;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

public abstract class GuiComponent extends Gui {

    private boolean enabled = true;
    private boolean visible = true;
    private boolean hovered = false;
    private boolean entered = false;
    private boolean buttonHeld;

    protected Minecraft mc = Minecraft.getMinecraft();

    private int backColor = 0;
    private int foreColor = -1;
    private GuiPanel parent;
    private Rectangle bounds;
    private Dimension minimumSize = new Dimension();

    private List<ActionPerformed> actionListeners = Lists.newArrayList();
    private List<GuiMouseAdapter> mouseAdapters = Lists.newArrayList();
    private List<GuiKeyboardAdapter> keyboardAdapters = Lists.newArrayList();

    public GuiComponent() {
        this.setBounds(new Rectangle());
        if (this instanceof ActionPerformed) {
            actionListeners.add((ActionPerformed) this);
        }
        if (this instanceof GuiMouseAdapter) {
            mouseAdapters.add((GuiMouseAdapter) this);
        }
        if (this instanceof GuiKeyboardAdapter) {
            keyboardAdapters.add((GuiKeyboardAdapter) this);
        }
    }

    public abstract void drawComponent(int mouseX, int mouseY);

    protected void drawBorders(int x1, int y1, int x2, int y2) {
        int val = 0xaa;
        Gui.drawRect(x1 - 1, y1 - 1, x1, y2, getBackColor() + val << 24); // left
        Gui.drawRect(x1 - 1, y1 - 1, x2 + 1, y1, getBackColor() + val << 24); // top
        Gui.drawRect(x2, y1 - 1, x2 + 1, y2 + 1, getBackColor() + val << 24); // right
        Gui.drawRect(x1 - 1, y2, x2 + 1, y2 + 1, getBackColor() + val << 24); // bottom
    }

    public void updateComponent() {}

    public void handleMouseInput() {
        if (!isEnabled()) {
            this.hovered = false;
            return;
        }
        if (mc.currentScreen != null) {
            int mouseX = Mouse.getX() * mc.currentScreen.width / mc.displayWidth;
            int mouseY = mc.currentScreen.height - Mouse.getY() * mc.currentScreen.height
                    / mc.displayHeight - 1;
            Point point = new Point(mouseX, mouseY);
            int button = Mouse.getEventButton();
            int scroll = Mouse.getEventDWheel();
            Point actual = getActualPosition();

            if (mouseX > actual.x && mouseX < actual.x + getBounds().width && mouseY > actual.y
                    && mouseY < actual.y + getBounds().height) {
                if (!isHovered()) {
                    this.entered = true;
                }
                this.hovered = true;
            } else {
                if (!isHovered()) {
                    this.entered = false;
                }
                this.hovered = false;
            }
            if (button != -1 && Mouse.getEventButtonState()) {
                this.buttonHeld = (true);

            }
            GuiMouseEvent event = new GuiMouseEvent(this, GuiMouseEvent.RAW, point, button, scroll);
            for (GuiMouseAdapter adapter : mouseAdapters) {
                event.event = GuiMouseEvent.RAW;
                adapter.accept(event);

                if (isHovered() || isButtonHeld()) {
                    if (Mouse.getEventDX() != 0 && Mouse.getEventDY() != 0) {
                        // mouse moved
                        event.event = GuiMouseEvent.MOVED;
                        adapter.accept(event);
                        if (isButtonHeld()) {
                            // mouse dragged
                            event.event = GuiMouseEvent.DRAGGED;
                            adapter.accept(event);
                        }
                    }
                    if (isHovered() && button != -1) {
                        if (Mouse.getEventButtonState()) {
                            // button pressed
                            event.event = GuiMouseEvent.PRESSED;
                            adapter.accept(event);
                        } else if (!Mouse.getEventButtonState()) {
                            // button released
                            event.event = GuiMouseEvent.RELEASED;
                            adapter.accept(event);
                            if (isButtonHeld()) {
                                // button clicked
                                event.event = GuiMouseEvent.CLICKED;
                                adapter.accept(event);
                            }
                            this.buttonHeld = false;
                        }
                    }

                    if (isHovered()) {
                        if (entered) {
                            // mouse entered
                            event.event = GuiMouseEvent.ENTERED;
                            adapter.accept(event);
                        } else {
                            // mouse left
                            event.event = GuiMouseEvent.HOVERED;
                            adapter.accept(event);
                        }
                    } else {
                        if (entered) {
                            event.event = GuiMouseEvent.EXITED;
                            adapter.accept(event);
                        }
                    }
                    if (scroll != 0) {
                        // wheel moved
                        event.event = GuiMouseEvent.SCROLLED;
                        adapter.accept(event);
                    }
                }
            }
            if (isHovered() && button == 0 && !Mouse.getEventButtonState()) {
                // left button released
                for (ActionPerformed action : actionListeners) {
                    action.action(event);
                }
            }
        }
    }

    public void handleKeyboardInput() {
        int key = Keyboard.getEventKey();
        char character = Keyboard.getEventCharacter();
        long time = Keyboard.getEventNanoseconds();
        GuiKeyboardEvent event = new GuiKeyboardEvent(this, key, character, time);
        for (GuiKeyboardAdapter adapter : keyboardAdapters) {
            adapter.accept(event);
        }
    }

    public void onClosed() {
        this.hovered = false;
        this.entered = false;
        this.buttonHeld = false;
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

    public void addActionListener(ActionPerformed actionPerformed) {
        this.actionListeners.add(actionPerformed);
    }

    public void addMouseAdapter(GuiMouseAdapter mouse) {
        this.mouseAdapters.add(mouse);
    }

    public void addKeyboardAdapter(GuiKeyboardAdapter keyboard) {
        this.keyboardAdapters.add(keyboard);
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (!visible) {
            this.onClosed();
        }
        this.visible = visible;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isButtonHeld() {
        return buttonHeld;
    }

}
