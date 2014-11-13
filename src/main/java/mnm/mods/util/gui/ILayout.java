package mnm.mods.util.gui;

import java.awt.Dimension;

public interface ILayout {

    void addComponent(GuiComponent comp, Object constraints);

    void removeComponent(GuiComponent comp);

    void layoutComponents();

    Dimension getLayoutSize();

}
