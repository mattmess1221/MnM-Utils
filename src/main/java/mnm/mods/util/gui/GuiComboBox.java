package mnm.mods.util.gui;

import java.util.List;

import com.google.common.collect.Lists;

public class GuiComboBox<T> extends GuiComponent {

    private List<T> options = Lists.newArrayList();

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        // TODO Auto-generated method stub

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
