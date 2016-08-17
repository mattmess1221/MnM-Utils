package mnm.mods.util.gui;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.eventbus.Subscribe;

import mnm.mods.util.ILocation;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.renderer.GlStateManager;

/**
 * A component that can contain multiple components.
 *
 * @author Matthew
 */
public class GuiPanel extends GuiComponent implements Iterable<GuiComponent> {

    private List<GuiComponent> components = Lists.newArrayList();
    private Optional<GuiComponent> overlay = Optional.empty();
    private Optional<ILayout> layout = Optional.empty();

    private Queue<Runnable> actionQueue = Queues.newLinkedBlockingDeque();

    public GuiPanel(@Nonnull ILayout layout) {
        setLayout(Optional.of(layout));
    }

    public GuiPanel() {}

    @Subscribe
    public void unfocus(GuiMouseEvent event) {
        // Unfocuses all focusable on click
        if (event.getType() == MouseEvent.CLICK) {
            unfocusAll();
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (this.overlay.isPresent()) {
            overlay.get().drawComponent(mouseX, mouseY);
            return;
        }
        getLayout().ifPresent(layout -> layout.layoutComponents(this));
        this.components.stream()
                .filter(GuiComponent::isVisible)
                .forEach(gc -> {
                    GlStateManager.pushMatrix();
                    ILocation location = gc.getLocation();
                    int xPos = location.getXPos();
                    int yPos = location.getYPos();
                    GlStateManager.translate(xPos, yPos, 0F);
                    GlStateManager.scale(gc.getScale(), gc.getScale(), 1F);

                    gc.drawComponent(mouseX, mouseY);

                    GlStateManager.popMatrix();
                });

        super.drawComponent(mouseX, mouseY);
    }

    @Override
    public void updateComponent() {
        // run the queue
        for (Runnable r = actionQueue.poll(); r != null; r = actionQueue.poll()) {
            r.run();
        }
        this.components.forEach(GuiComponent::updateComponent);
        getOverlay().ifPresent(GuiComponent::updateComponent);

    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (overlay.isPresent()) {
            overlay.get().handleMouseInput();
            return;
        }
        this.components.forEach(GuiComponent::handleMouseInput);
    }

    @Override
    public void handleKeyboardInput() {
        super.handleKeyboardInput();
        if (overlay.isPresent()) {
            overlay.get().handleKeyboardInput();
            return;
        }
        this.components.forEach(GuiComponent::handleKeyboardInput);
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
            this.actionQueue.offer(() -> {
                guiComponent.setParent(this);
                components.add(guiComponent);
                getLayout().ifPresent(layout -> layout.addComponent(guiComponent, constraints));
            });
        }
    }

    /**
     * Removes all components from this panel.
     */
    public void clearComponents() {
        this.actionQueue.offer(() -> {
            for (GuiComponent comp : components) {
                comp.setParent(null);
                getLayout().ifPresent(layout -> layout.removeComponent(comp));
            }
            components.clear();
            setOverlay(Optional.empty());
        });
    }

    /**
     * Removes a component from this panel.
     *
     * @param guiComp The component to remove
     */
    public void removeComponent(GuiComponent guiComp) {
        this.actionQueue.offer(() -> {
            components.remove(guiComp);
            getLayout().ifPresent(layout -> layout.removeComponent(guiComp));
        });
    }

    /**
     * Sets the layout for this panel.
     *
     * @param lmg The layout manager
     */
    public void setLayout(Optional<ILayout> lmg) {
        this.layout = lmg;
    }

    /**
     * Gets the layout for this panel.
     *
     * @return The layout
     */
    public Optional<ILayout> getLayout() {
        return layout;
    }

    /**
     * Sets the overlay for this component. An overlay temporarily replaces the
     * contents of the panel with itself. The contents are placed back when the
     * overlay is set to null.
     *
     * @param gui The component to overlay
     */
    public void setOverlay(Optional<GuiComponent> overlay) {
        if (overlay.isPresent()) {
            GuiComponent gui = overlay.get();
            gui.setParent(this);
            gui.setLocation(gui.getLocation().copy()
                    .setWidth(getLocation().getWidth())
                    .setHeight(getLocation().getHeight()));
        } else {
            this.unfocusAll();
        }
        this.overlay = overlay;
    }

    public Optional<GuiComponent> getOverlay() {
        return overlay;
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
        this.components.forEach(GuiComponent::onClosed);
        this.overlay.ifPresent(GuiComponent::onClosed);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension size = getLayout().map(ILayout::getLayoutSize).orElseGet(() -> {
            int width = 0;
            int height = 0;
            for (GuiComponent gc : components) {
                width = Math.max(width, gc.getLocation().getXPos() + gc.getLocation().getWidth());
                height = Math.max(height, gc.getLocation().getYPos() + gc.getLocation().getHeight());
            }
            return new Dimension(width, height);
        });

        return size;

    }

    @Deprecated
    @Override
    public Iterator<GuiComponent> iterator() {
        return components.iterator();
    }

}
