package mnm.mods.util.gui;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;

import com.google.common.collect.Lists;

public class GuiPanel extends GuiComponent implements Iterable<GuiComponent> {

    private List<GuiComponent> components = Lists.newArrayList();
    private ILayout layout;

    public GuiPanel(ILayout layout) {
        setLayout(layout);
    }

    public GuiPanel() {
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {

        GlStateManager.pushMatrix();
        // Make the upper left corner of the panel (0,0).
        GlStateManager.translate(this.getBounds().x, this.getBounds().y, 0.0F);
        if (layout != null) {
            layout.layoutComponents();
        }
        for (GuiComponent gc : components) {
            if (gc.visible) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(gc.getBounds().x, gc.getBounds().y, 0F);
                gc.drawComponent(mouseX, mouseY);
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
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

    @Override
    public Dimension getPreferedSize() {
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
