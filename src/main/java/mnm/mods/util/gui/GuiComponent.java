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
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

/**
 * The base class for all gui components.
 *
 * @author Matthew
 */
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
    private float scale = 1;

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

    /**
     * Draws this component on screen.
     *
     * @param mouseX The mouse x
     * @param mouseY The mouse y
     */
    public abstract void drawComponent(int mouseX, int mouseY);

    /**
     * Draws borders around the provided points.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    protected void drawBorders(int x1, int y1, int x2, int y2) {
        int val = 0xaa;
        Gui.drawRect(x1 - 1, y1 - 1, x1, y2, getBackColor() + val << 24); // left
        Gui.drawRect(x1 - 1, y1 - 1, x2 + 1, y1, getBackColor() + val << 24); // top
        Gui.drawRect(x2, y1 - 1, x2 + 1, y2 + 1, getBackColor() + val << 24); // right
        Gui.drawRect(x1 - 1, y2, x2 + 1, y2 + 1, getBackColor() + val << 24); // bottom
    }

    /**
     * Updates the component. Called when it is called on the {@link GuiScreen}.
     */
    public void updateComponent() {}

    /**
     * Handles the mouse input and sends it to the mouse and action listeners.
     */
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

            float scale = getActualScale();

            if (mouseX > actual.x && mouseX < actual.x + getBounds().width * scale
                    && mouseY > actual.y && mouseY < actual.y + getBounds().height * scale) {
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

    /**
     * Handles the keyboard input and sends it to the keyboard listeners.
     */
    public void handleKeyboardInput() {
        int key = Keyboard.getEventKey();
        char character = Keyboard.getEventCharacter();
        long time = Keyboard.getEventNanoseconds();
        GuiKeyboardEvent event = new GuiKeyboardEvent(this, key, character, time);
        for (GuiKeyboardAdapter adapter : keyboardAdapters) {
            adapter.accept(event);
        }
    }

    /**
     * Called when the screen is closed.
     */
    public void onClosed() {
        this.hovered = false;
        this.entered = false;
        this.buttonHeld = false;
    }

    /**
     * Sets the bounds of this component.
     *
     * @param bounds The new bounds
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Sets the new bounds of this component.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void setBounds(int x, int y, int width, int height) {
        setBounds(new Rectangle(x, y, width, height));
    }

    /**
     * Sets the position of this component.
     *
     * @param xPos The X position
     * @param yPos The Y position
     */
    public void setPosition(int xPos, int yPos) {
        getBounds().x = xPos;
        getBounds().y = yPos;
    }

    /**
     * Sets the size of this component.
     *
     * @param width The width
     * @param height The height
     */
    public void setSize(int width, int height) {
        getBounds().width = width;
        getBounds().height = height;
    }

    /**
     * Gets the current bounds of this component. The returned object is
     * mutable.
     *
     * @return The current bounds
     */
    public Rectangle getBounds() {
        return this.bounds;
    }

    /**
     * Adds an action listener to the list.
     *
     * @param actionPerformed The listener
     */
    public void addActionListener(ActionPerformed actionPerformed) {
        this.actionListeners.add(actionPerformed);
    }

    /**
     * Adds a mouse adapter to the list.
     *
     * @param mouse The adapter
     */
    public void addMouseAdapter(GuiMouseAdapter mouse) {
        this.mouseAdapters.add(mouse);
    }

    /**
     * Adds a keyboard adapter to the list.
     *
     * @param keyboard The adapter
     */
    public void addKeyboardAdapter(GuiKeyboardAdapter keyboard) {
        this.keyboardAdapters.add(keyboard);
    }

    /**
     * Gets the parent of this component. Will return {@code null} until it is
     * added to a panel by being used as the parameter to
     * {@link GuiPanel#addComponent(GuiComponent)} or
     * {@link GuiPanel#addComponent(GuiComponent, Object)}.
     *
     * @return The parent or null if there is none
     */
    public GuiPanel getParent() {
        return this.parent;
    }

    /**
     * Sets the parent of this component. Should only be used by
     * {@link GuiPanel}.
     *
     * @param guiPanel The parent
     */
    void setParent(GuiPanel guiPanel) {
        this.parent = guiPanel;
    }

    /**
     * Gets the position of this component when drawn on the screen. Includes
     * all parent's positions and scales.
     *
     * @return The position
     */
    public Point getActualPosition() {
        Point point = new Point(getBounds().x, getBounds().y);
        if (getParent() != null) {
            Point parent = getParent().getActualPosition();
            point.x += parent.x;
            point.y += parent.y;
        }
        point.x *= getScale();
        point.y *= getScale();
        return point;
    }

    public void setMinimumSize(Dimension size) {
        this.minimumSize = size;
    }

    public Dimension getMinimumSize() {
        return minimumSize;
    }

    /**
     * Sets the scale for this component.
     *
     * @param scale The scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Gets the scale for this component.
     *
     * @return The scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Gets the total scale of this component. Takes into account all the
     * parents.
     *
     * @return The scale
     */
    public float getActualScale() {
        float scale = getScale();
        if (getParent() != null) {
            scale *= getParent().getActualScale();
        }
        return scale;
    }

    /**
     * Gets the background color. If it is 0, it returns the parent's.
     *
     * @return The background color
     */
    public int getBackColor() {
        int result = backColor;
        if (getParent() != null && result == 0) {
            result = getParent().getBackColor();
        }
        return result;
    }

    /**
     * Sets the background color.
     *
     * @param backColor The new color
     */
    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    /**
     * Gets the foreground color. If it is 0, it returns the parent's.
     *
     * @return The foreground color
     */
    public int getForeColor() {
        int result = foreColor;
        if (getParent() != null && result == -1) {
            result = getParent().getForeColor();
        }
        return result;
    }

    /**
     * Sets the foreground color.
     *
     * @param foreColor The new color
     */
    public void setForeColor(int foreColor) {
        this.foreColor = foreColor;
    }

    /**
     * Gets if this is enabled. Disabled components will not handle mouse or
     * keyboard events.
     *
     * @return True if enabled, false if disabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets if this is enabled or not. Disabled components will not handle mouse
     * or keyboard events.
     *
     * @param enabled True for enabled, false for disabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets if this component is visible. Non-visible components are not
     * rendered.
     *
     * @return The visibility state
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets this component's visibility. Non-visible components are not
     * rendered.
     *
     * @param visible The visibility state
     */
    public void setVisible(boolean visible) {
        if (!visible) {
            this.onClosed();
        }
        this.visible = visible;
    }

    /**
     * Returns if the cursor is hovered over this component.
     *
     * @return THe hover state
     */
    public boolean isHovered() {
        return hovered;
    }

    /**
     * Returns if the cursor is hovered over this component and a button is
     * held.
     */
    public boolean isButtonHeld() {
        return buttonHeld;
    }

}
