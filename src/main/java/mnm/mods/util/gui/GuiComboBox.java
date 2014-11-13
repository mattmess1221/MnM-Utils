package mnm.mods.util.gui;

import java.awt.Dimension;
import java.util.List;

import com.google.common.collect.Lists;

public class GuiComboBox<T> extends GuiComponent {

    private List<T> options = Lists.newArrayList();

    public GuiComboBox(GuiPanel parent) {
        super(parent);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        // TODO Auto-generated method stub

    }

    @Override
    public Dimension getPreferedSize() {
        return null;
    }

    public void addOption(T t) {
        this.options.add(t);
    }

    public void addAllOptions(T[] options) {
        for (T t : options) {
            this.options.add(t);
        }
    }
}
