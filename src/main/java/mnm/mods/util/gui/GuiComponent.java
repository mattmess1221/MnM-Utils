package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import mnm.mods.util.gui.events.GuiEvent;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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

    private List<Consumer<GuiEvent>> actionListeners = new ArrayList<>();
    private List<Consumer<GuiMouseEvent>> mouseAdapters = new ArrayList<>();
    private List<Consumer<GuiKeyboardEvent>> keyboardAdapters = new ArrayList<>();

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
            Point point = new Point(mouseX, mouseY);
            int button = Mouse.getEventButton();
            int scroll = Mouse.getEventDWheel();
            Point actual = getActualPosition();

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
            if (button != -1 && Mouse.getEventButtonState()) {
                this.held = true;

            }
            GuiMouseEvent event = new GuiMouseEvent(this, GuiMouseEvent.RAW, point, button, scroll);
            mouseAdapters.forEach(adapter -> {
                event.event = GuiMouseEvent.RAW;
                adapter.accept(event);

                if (hovered || held) {
                    if (Mouse.getEventDX() != 0 && Mouse.getEventDY() != 0) {
                        // mouse moved
                        event.event = GuiMouseEvent.MOVED;
                        adapter.accept(event);
                        if (held) {
                            // mouse dragged
                            event.event = GuiMouseEvent.DRAGGED;
                            adapter.accept(event);
                        }
                        if (button != -1) {
                            if (Mouse.getEventButtonState()) {
                                // button pressed
                                event.event = GuiMouseEvent.PRESSED;
                                adapter.accept(event);
                            } else if (!Mouse.getEventButtonState()) {
                                // button released
                                event.event = GuiMouseEvent.RELEASED;
                                adapter.accept(event);
                                if (held) {
                                    // button clicked
                                    event.event = GuiMouseEvent.CLICKED;
                                    adapter.accept(event);
                                }
                                this.held = false;
                            }
                        }
                    }
                    if (hovered) {
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
            })  ;
            if (hovered && button == 0 && !Mouse.getEventButtonState()) {
                // left button released
                actionListeners.forEach(it -> it.accept(event));
            }
        }
    }

    public void handleKeyboardInput() {
        int key = Keyboard.getEventKey();
        char character = Keyboard.getEventCharacter();
        long time = Keyboard.getEventNanoseconds();
        GuiKeyboardEvent event = new GuiKeyboardEvent(this, key, character, time);
        keyboardAdapters.forEach(it -> it.accept(event));
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

    public void addActionListener(Consumer<GuiEvent> action) {
        this.actionListeners.add(action);
    }

    public void addMouseAdapter(Consumer<GuiMouseEvent> mouse) {
        this.mouseAdapters.add(mouse);
    }

    public void addKeyboardAdapter(Consumer<GuiKeyboardEvent> keyboard) {
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

}
