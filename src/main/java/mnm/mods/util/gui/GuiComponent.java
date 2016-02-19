package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.apache.commons.lang3.StringEscapeUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.eventbus.EventBus;

import mnm.mods.util.Color;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/**
 * The base class for all gui components.
 *
 * @author Matthew
 */
public abstract class GuiComponent extends Gui {

    private boolean enabled = true;
    private boolean visible = true;
    private boolean hovered;
    private boolean entered;
    private boolean focused;
    private boolean buttonHeld;

    protected Minecraft mc = Minecraft.getMinecraft();

    private int backColor = 0;
    private int foreColor = -1;
    private GuiPanel parent;
    private Rectangle bounds;
    private Dimension minimumSize = new Dimension();
    private float scale = 1;
    private String caption;

    private EventBus bus = new EventBus(getClass().getName());

    private GuiComponent wrapper;

    public GuiComponent() {
        this.setBounds(new Rectangle());
        this.getBus().register(this);
    }

    /**
     * Draws this component on screen.
     *
     * @param mouseX The mouse x
     * @param mouseY The mouse y
     */
    public void drawComponent(int mouseX, int mouseY) {
        if (wrapper != null) {
            wrapper.drawComponent(mouseX, mouseY);
        }
        // draw the caption
        String caption = getCaption();
        if (isHovered() && caption != null && !caption.isEmpty()) {
            GlStateManager.pushMatrix();
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            drawCaptionAtCursor(caption, mouseX, mouseY);
            GlStateManager.popMatrix();
        }
    }

    protected void drawCaption(String caption, int x, int y) {
        caption = StringEscapeUtils.unescapeJava(caption);
        String[] list = caption.split("\n\r?");

        int w = 0;
        // find the largest width
        for (String s : list) {
            w = Math.max(w, (int) (mc.fontRendererObj.getStringWidth(s) * getActualScale()));
        }
        y -= mc.fontRendererObj.FONT_HEIGHT * list.length;

        Point point = getActualPosition();
        ScaledResolution sr = new ScaledResolution(mc);
        int w2 = w;
        int x2 = x;
        while (x2 - 8 + point.x + w2 + 20 > sr.getScaledWidth()) {
            x--;
            w2--;
        }
        // put it on top
        GlStateManager.translate(0, 0, 1);
        GlStateManager.pushMatrix();
        Gui.drawRect(x - 2, y - 2, x + w + 2, y + mc.fontRendererObj.FONT_HEIGHT * list.length + 1,
                0xcc333333);
        drawBorders(x - 2, y - 2, x + w + 2, y + mc.fontRendererObj.FONT_HEIGHT * list.length + 1,
                0xccaaaaaa);
        for (String s : list) {
            mc.fontRendererObj.drawStringWithShadow(s, x, y, getForeColor());
            y += mc.fontRendererObj.FONT_HEIGHT;
        }
        GlStateManager.popMatrix();
    }

    private void drawCaptionAtCursor(String msg, int mouseX, int mouseY) {
        Point point = getActualPosition();
        drawCaption(msg, mouseX - point.x + 8, mouseY - point.y);
    }

    protected void drawBorders(int x1, int y1, int x2, int y2, int color) {
        this.drawVerticalLine(x1 - 1, y1 - 1, y2 + 1, color); // left
        this.drawHorizontalLine(x1 - 1, x2, y1 - 1, color); // top
        this.drawVerticalLine(x2, y1 - 1, y2 + 1, color); // right
        this.drawHorizontalLine(x1, x2 - 1, y2, color); // bottom
    }

    /**
     * Draws borders around the provided points. Uses a brightened background
     * color with 0xaa transparency.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    protected void drawBorders(int x1, int y1, int x2, int y2) {
        Color color = Color.of(getBackColor());
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        double amt = .75;
        r += luminance(r, amt);
        g += luminance(g, amt);
        b += luminance(b, amt);
        color = Color.of(r, g, b, 0xaa);
        drawBorders(x1, y1, x2, y2, color.getColor());
    }

    private static int luminance(int o, double amt) {
        return (int) ((255 - o) * amt);
    }

    /**
     * Updates the component. Called when it is called on the {@link GuiScreen}.
     */
    public void updateComponent() {
        if (wrapper != null) {
            wrapper.updateComponent();
        }
    }

    /**
     * Handles the mouse input and sends it to the mouse and action listeners.
     */
    public void handleMouseInput() {
        if (wrapper != null) {
            wrapper.handleMouseInput();
            return;
        }
        if (!isEnabled()) {
            this.hovered = false;
            return;
        }
        if (mc.currentScreen != null) {
            float scale = getActualScale();
            Point point = scalePoint(new Point(Mouse.getX(), Mouse.getY()));
            Rectangle actual = getActualBounds();
            // adjust for position and scale
            int x = (int) ((point.x - actual.x) / scale);
            int y = (int) ((point.y - actual.y) / scale);

            int button = Mouse.getEventButton();
            int scroll = Mouse.getEventDWheel();

            if (x >= actual.x && x <= actual.x + actual.width
                    && y >= actual.y && y <= actual.y + actual.height) {
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

            getBus().post(new GuiMouseEvent(this, MouseEvent.RAW, x, y, button, scroll));

            if (isHovered() || isButtonHeld()) {
                if (Mouse.getEventDX() != 0 && Mouse.getEventDY() != 0) {
                    // mouse moved
                    getBus().post(new GuiMouseEvent(this, MouseEvent.MOVE, x, y, button, scroll));
                    if (isButtonHeld()) {
                        // mouse dragged
                        getBus().post(new GuiMouseEvent(this, MouseEvent.DRAG, x, y, button, scroll));
                    }
                }
                if (button != -1) {
                    if (Mouse.getEventButtonState()) {
                        // button pressed
                        getBus().post(new GuiMouseEvent(this, MouseEvent.PRESS, x, y, button, scroll));
                    } else if (!Mouse.getEventButtonState()) {
                        // button released
                        getBus().post(new GuiMouseEvent(this, MouseEvent.RELEASE, x, y, button, scroll));
                        if (isButtonHeld() && isHovered()) {
                            // button clicked
                            getBus().post(new GuiMouseEvent(this, MouseEvent.CLICK, x, y, button, scroll));
                        }
                        this.buttonHeld = false;
                    }
                }

                if (isHovered()) {
                    if (entered) {
                        // mouse entered
                        getBus().post(new GuiMouseEvent(this, MouseEvent.ENTER, x, y, button, scroll));
                    } else {
                        // mouse left
                        getBus().post(new GuiMouseEvent(this, MouseEvent.HOVER, x, y, button, scroll));
                    }
                } else {
                    if (entered) {
                        getBus().post(new GuiMouseEvent(this, MouseEvent.EXIT, x, y, button, scroll));
                    }
                }
                if (scroll != 0) {
                    // wheel moved
                    getBus().post(new GuiMouseEvent(this, MouseEvent.SCROLL, x, y, button, scroll));
                }
            }

            if (isHovered() && button == 0 && !Mouse.getEventButtonState()) {
                // left button released
                getBus().post(new ActionPerformedEvent(this));
            }
        }
    }

    /**
     * Handles the keyboard input and sends it to the keyboard listeners.
     */
    public void handleKeyboardInput() {
        if (wrapper != null) {
            wrapper.handleKeyboardInput();
            return;
        }
        int key = Keyboard.getEventKey();
        char character = Keyboard.getEventCharacter();
        long time = Keyboard.getEventNanoseconds();
        GuiKeyboardEvent event = new GuiKeyboardEvent(this, key, character, time);
        getBus().post(event);
    }

    /**
     * Called when the screen is closed.
     */
    public void onClosed() {
        if (wrapper != null) {
            wrapper.onClosed();
        }
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
        if (wrapper != null) {
            wrapper.setBounds(bounds);
            return;
        }
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
        if (wrapper != null) {
            return this.wrapper.getBounds();
        }
        return this.bounds;
    }

    /**
     * Gets the event bus used for the gui events for this component. This
     * replaces the listener interfaces previously used. They should easily port
     * over by adding a {@link com.google.common.eventbus.Subscribe} annotation
     * to the method.
     *
     * @return The event bus
     */
    public EventBus getBus() {
        return bus;
    }

    public void wrap(GuiComponent wrap) {
        if (wrap == this) {
            return;
        }
        this.wrapper = wrap;
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
        if (wrapper != null) {
            return wrapper.getParent();
        }
        return this.parent;
    }

    public GuiPanel getRootPanel() {
        GuiPanel panel = getParent();
        if (panel == null) {
            return this instanceof GuiPanel ? (GuiPanel) this : null;
        }
        for (;;) {
            if (panel.getParent() == null)
                return panel;
            panel = panel.getParent();
        }
    }

    /**
     * Sets the parent of this component. Should only be used by
     * {@link GuiPanel}.
     *
     * @param guiPanel The parent
     */
    void setParent(GuiPanel guiPanel) {
        if (wrapper != null) {
            wrapper.setParent(guiPanel);
            return;
        }
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

    public Dimension getActualSize() {
        float scale = getActualScale();
        Dimension d = new Dimension(getBounds().getSize());
        d.height *= scale;
        d.width *= scale;
        return d;
    }

    public Rectangle getActualBounds() {
        Rectangle b = new Rectangle(getBounds());
        float scale = getActualScale();
        b.x *= scale;
        b.y *= scale;
        b.width *= scale;
        b.height *= scale;
        if (getParent() != null) {
            Rectangle b1 = getParent().getActualBounds();
            b.x += b1.x;
            b.y += b1.y;
        }
        return b;
    }

    public void setMinimumSize(Dimension size) {
        if (wrapper != null) {
            wrapper.setMinimumSize(size);
            return;
        }
        this.minimumSize = size;
    }

    public Dimension getMinimumSize() {
        if (wrapper != null) {
            return wrapper.getMinimumSize();
        }
        return minimumSize;
    }

    /**
     * Sets the scale for this component.
     *
     * @param scale The scale
     */
    public void setScale(float scale) {
        if (wrapper != null) {
            wrapper.setScale(scale);
            return;
        }
        this.scale = scale;
    }

    /**
     * Gets the scale for this component.
     *
     * @return The scale
     */
    public float getScale() {
        if (wrapper != null) {
            return wrapper.getScale();
        }
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
        if (wrapper != null) {
            return wrapper.getBackColor();
        }
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
        if (wrapper != null) {
            wrapper.setBackColor(backColor);
            return;
        }
        this.backColor = backColor;
    }

    /**
     * Gets the foreground color. If it is -1, it returns the parent's.
     *
     * @return The foreground color
     */
    public int getForeColor() {
        if (wrapper != null) {
            return wrapper.getForeColor();
        }
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
        if (wrapper != null) {
            wrapper.setForeColor(foreColor);
            return;
        }
        this.foreColor = foreColor;
    }

    /**
     * Gets if this is enabled. Disabled components will not handle mouse or
     * keyboard events.
     *
     * @return True if enabled, false if disabled.
     */
    public boolean isEnabled() {
        if (wrapper != null) {
            return wrapper.isEnabled();
        }
        return enabled;
    }

    /**
     * Sets if this is enabled or not. Disabled components will not handle mouse
     * or keyboard events.
     *
     * @param enabled True for enabled, false for disabled
     */
    public void setEnabled(boolean enabled) {
        if (wrapper != null) {
            wrapper.setEnabled(enabled);
            return;
        }
        this.enabled = enabled;
    }

    /**
     * Gets if this component is visible. Non-visible components are not
     * rendered.
     *
     * @return The visibility state
     */
    public boolean isVisible() {
        if (wrapper != null) {
            return wrapper.isVisible();
        }
        return visible;
    }

    /**
     * Sets this component's visibility. Non-visible components are not
     * rendered.
     *
     * @param visible The visibility state
     */
    public void setVisible(boolean visible) {
        if (wrapper != null) {
            wrapper.setVisible(visible);
            return;
        }
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
        if (wrapper != null) {
            return wrapper.isHovered();
        }
        return hovered && (parent == null ? true : parent.isHovered());
    }

    /**
     * Returns if the cursor is hovered over this component and a button is
     * held.
     */
    public boolean isButtonHeld() {
        if (wrapper != null) {
            return wrapper.isButtonHeld();
        }
        return buttonHeld;
    }

    /**
     * Sets the caption which is shown when the mouse is hovering over this
     * component.
     *
     * @param caption The new caption
     */
    public void setCaption(String caption) {
        if (wrapper != null) {
            wrapper.setCaption(caption);
            return;
        }
        this.caption = caption;
    }

    /**
     * Gets the caption which is shown when the mouse is hovering over this
     * component.
     *
     * @return The caption
     */
    public String getCaption() {
        if (wrapper != null) {
            return wrapper.getCaption();
        }
        return caption;
    }

    /**
     * Gets the current focus of this component.
     *
     * @return The focus
     */
    public boolean isFocused() {
        return focused;
    }

    /**
     * Sets the component's focus
     *
     * @param focus The focus value
     */
    public void setFocused(boolean focused) {
        // check if is focusable
        if (isFocusable()) {
            if (focused) {
                // unfocus everything before focusing this one
                GuiPanel root = getRootPanel();
                if (root != null) {
                    root.unfocusAll();
                }
            }
            this.focused = focused;
        }
        // otherwise ignore
    }

    /**
     * Denotes if this is focusable. Override to change.
     *
     * @return Whether this is focusable
     */
    @SuppressWarnings("deprecation")
    public boolean isFocusable() {
        // return false; check for IFocusable for compatibility
        return this instanceof IFocusable;
    }

    protected static Point scalePoint(Point point) {
        Minecraft mc = Minecraft.getMinecraft();
        int x = point.x * mc.currentScreen.width / mc.displayWidth;
        int y = mc.currentScreen.height - point.y * mc.currentScreen.height / mc.displayHeight - 1;
        return new Point(x, y);
    }

}
