package mnm.mods.util.gui;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;

import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.renderer.GlStateManager;

import com.google.common.collect.Lists;

public class GuiPanel extends GuiComponent implements Iterable<GuiComponent>, GuiMouseAdapter {

    private List<GuiComponent> components = Lists.newArrayList();
    private ILayout layout;

    public GuiPanel(ILayout layout) {
        this();
        setLayout(layout);
    }

    public GuiPanel() {}

    @Override
    public void accept(GuiMouseEvent event) {
        // Unfocuses all focusable on click
        if (event.event == GuiMouseEvent.CLICKED) {
            unfocusAll();
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {

        if (layout != null) {
            layout.layoutComponents(this);
        }
        for (GuiComponent gc : components) {
            if (gc.visible) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(gc.getBounds().x, gc.getBounds().y, 0F);
                gc.drawComponent(mouseX, mouseY);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public void updateComponent() {
        super.updateComponent();
        for (GuiComponent comp : this) {
            comp.updateComponent();
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        for (int i = 0; i < this.components.size(); i++) {
            this.components.get(i).handleMouseInput();
        }
    }

    @Override
    public void handleKeyboardInput() {
        super.handleKeyboardInput();
        for (GuiComponent comp : this) {
            comp.handleKeyboardInput();
        }
    }

    public int getComponentCount() {
        return this.components.size();
    }

    public void addComponent(GuiComponent guiComponent) {
        addComponent(guiComponent, (Object) null);
    }

    public void addComponent(GuiComponent guiComponent, Object constraints) {
        if (guiComponent != null) {
            guiComponent.setParent(this);
            components.add(guiComponent);
            if (layout != null) {
                layout.addComponent(guiComponent, constraints);
            }
        }
    }

    public void clearComponents() {
        if (layout != null) {
            for (GuiComponent comp : components) {
                layout.removeComponent(comp);
            }
        }
        components.clear();
    }

    public void removeComponent(GuiComponent guiComp) {
        components.remove(guiComp);
        if (layout != null) {
            layout.removeComponent(guiComp);
        }
    }

    public void setLayout(ILayout lmg) {
        this.layout = lmg;
    }

    public ILayout getLayout() {
        return layout;
    }

    public void unfocusAll() {
        for (GuiComponent comp : components) {
            if (comp instanceof Focusable) {
                ((Focusable) comp).setFocused(false);
            } else if (comp instanceof GuiPanel) {
                ((GuiPanel) comp).unfocusAll();
            }
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
