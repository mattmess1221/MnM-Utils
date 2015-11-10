package mnm.mods.util.gui;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.renderer.GlStateManager;

import com.google.common.collect.Lists;

/**
 * A component that can contain multiple components.
 *
 * @author Matthew
 */
public class GuiPanel extends GuiComponent implements Iterable<GuiComponent> {

    private List<GuiComponent> components = Lists.newArrayList();
    private ILayout layout;
    @Nullable
    private GuiComponent overlay;

    public GuiPanel(ILayout layout) {
        this();
        setLayout(layout);
    }

    public GuiPanel() {
        // Unfocuses all focusable on click
        this.addMouseAdapter(new GuiMouseAdapter() {
            @Override
            public void accept(GuiMouseEvent event) {
                if (event.event == GuiMouseEvent.CLICKED) {
                    unfocusAll();
                }
            }
        });
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {

        if (overlay != null) {
            overlay.drawComponent(mouseX, mouseY);
            return;
        }
        for (GuiComponent gc : components) {
            if (gc.isVisible()) {
                GlStateManager.pushMatrix();

                GlStateManager.translate(gc.getBounds().x, gc.getBounds().y, 0F);
                GlStateManager.scale(gc.getScale(), gc.getScale(), 1F);

                gc.drawComponent(mouseX, mouseY);

                GlStateManager.popMatrix();
            }
        }
        super.drawComponent(mouseX, mouseY);
    }

    @Override
    public void updateComponent() {
        super.updateComponent();
        for (GuiComponent comp : this) {
            comp.updateComponent();
        }
        if (layout != null) {
            layout.layoutComponents(this);
        }
        if (this.overlay != null) {
            overlay.updateComponent();
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (overlay != null) {
            overlay.handleMouseInput();
            return;
        }
        for (int i = 0; i < this.components.size(); i++) {
            this.components.get(i).handleMouseInput();
        }
    }

    @Override
    public void handleKeyboardInput() {
        super.handleKeyboardInput();
        if (overlay != null) {
            overlay.handleKeyboardInput();
            return;
        }
        for (GuiComponent comp : this) {
            comp.handleKeyboardInput();
        }
    }

    /**
     * Gets the number of components in this panel.
     *
     * @return The count
     */
    public int getComponentCount() {
        return this.components.size();
    }

    /**
     * Adds a component to this panel.
     *
     * @param guiComponent The component
     */
    public void addComponent(GuiComponent guiComponent) {
        addComponent(guiComponent, (Object) null);
    }

    /**
     * Adds a component to this panel with constraints.
     *
     * @param guiComponent The component
     * @param constraints The constraints
     */
    public void addComponent(GuiComponent guiComponent, Object constraints) {
        if (guiComponent != null) {
            guiComponent.setParent(this);
            components.add(guiComponent);
            if (layout != null) {
                layout.addComponent(guiComponent, constraints);
            }
            if (getParent() != null) {
                updateComponent();
            }
        }
    }

    /**
     * Removes all components from this panel.
     */
    public void clearComponents() {
        for (GuiComponent comp : components) {
            comp.setParent(null);
            if (layout != null) {
                layout.removeComponent(comp);
            }
        }
        components.clear();
        setOverlay(null);
        updateComponent();
    }

    /**
     * Removes a component from this panel.
     *
     * @param guiComp The component to remove
     */
    public void removeComponent(GuiComponent guiComp) {
        components.remove(guiComp);
        if (layout != null) {
            layout.removeComponent(guiComp);
        }
        updateComponent();
    }

    /**
     * Sets the layout for this panel.
     *
     * @param lmg The layout manager
     */
    public void setLayout(ILayout lmg) {
        this.layout = lmg;
    }

    /**
     * Gets the layout for this panel.
     *
     * @return The layout
     */
    public ILayout getLayout() {
        return layout;
    }

    /**
     * Sets the overlay for this component. An overlay temporarily replaces the
     * contents of the panel with itself. The contents are placed back when the
     * overlay is set to null.
     *
     * @param gui The component to overlay
     */
    public void setOverlay(GuiComponent gui) {
        if (gui != null) {
            gui.setParent(this);
            gui.setSize(getBounds().width, getBounds().height);
        } else {
            this.unfocusAll();
        }
        this.overlay = gui;
    }

    /**
     * Unfocuses all components in this panel that are focusable.
     */
    public void unfocusAll() {
        for (GuiComponent comp : components) {
            if (comp.isFocusable()) {
                comp.setFocused(false);
            } else if (comp instanceof GuiPanel) {
                ((GuiPanel) comp).unfocusAll();
            }
        }
    }

    @Override
    public void onClosed() {
        for (GuiComponent comp : components) {
            comp.onClosed();
        }
        if (this.overlay != null) {
            this.overlay.onClosed();
        }
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension size = null;
        if (getLayout() != null) {
            size = getLayout().getLayoutSize();
        } else {
            int width = 0;
            int height = 0;
            for (GuiComponent gc : this) {
                width = Math.max(width, gc.getBounds().x + gc.getBounds().width);
                height = Math.max(height, gc.getBounds().y + gc.getBounds().height);
            }
            size = new Dimension(width, height);
        }
        return size;

    }

    @Override
    public Iterator<GuiComponent> iterator() {
        return components.iterator();
    }
}
