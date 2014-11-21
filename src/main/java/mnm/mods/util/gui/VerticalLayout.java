package mnm.mods.util.gui;

import java.awt.Dimension;
import java.util.List;

import com.google.common.collect.Lists;

public class VerticalLayout implements ILayout {

    private List<GuiComponent> list = Lists.newArrayList();

    @Override
    public void addComponent(GuiComponent comp, Object constraints) {
        if (constraints != null) {
            if (constraints instanceof Integer) {
                list.add((Integer) constraints, comp);
            } else {
                throw new IllegalArgumentException("Illegal constraint of type: "
                        + constraints.getClass().getName() + ". Only int accepted.");
            }
        } else {
            list.add(comp);
        }
    }

    @Override
    public void removeComponent(GuiComponent comp) {
        list.remove(comp);
    }

    @Override
    public void layoutComponents() {
        int y = 0;
        for (GuiComponent comp : list) {
            comp.getBounds().x = 0;
            comp.getBounds().y = y;
            y += comp.getPreferedSize().height;
        }
    }

    @Override
    public Dimension getLayoutSize() {
        int width = 0;
        int height = 0;
        for (GuiComponent comp : list) {
            width = Math.max(width, comp.getPreferedSize().width);
            height += comp.getPreferedSize().height;
        }
        return new Dimension(width, height);
    }
}
