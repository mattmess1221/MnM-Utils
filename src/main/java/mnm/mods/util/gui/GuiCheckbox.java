package mnm.mods.util.gui;

import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import net.minecraft.client.gui.Gui;

/**
 * A checkbox, representing a boolean input.
 *
 * @author Matthew
 */
public class GuiCheckbox extends GuiComponent implements ActionPerformed, IGuiInput<Boolean> {

    private boolean value;

    public GuiCheckbox() {
        this.setSize(9, 9);
        setBackColor(0x99ffffa0);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        int centerX = 4;
        int centerY = 4;
        int width = 9;
        int height = 9;

        // Background
        Gui.drawRect(1, 0, width - 1, 1, getBackColor()); // top
        Gui.drawRect(1, height - 1, width - 1, height, getBackColor()); // bottom
        Gui.drawRect(0, 1, 1, height - 1, getBackColor()); // left
        Gui.drawRect(width - 1, 1, width, height - 1, getBackColor()); // right
        Gui.drawRect(1, 1, width - 1, height - 1, 0xff000000); // background

        if (getValue()) {
            // draw check
            Gui.drawRect(centerX - 2, centerY, centerX - 1, centerY + 1, getForeColor());
            Gui.drawRect(centerX - 1, centerY + 1, centerX, centerY + 2, getForeColor());
            Gui.drawRect(centerX, centerY + 2, centerX + 1, centerY + 3, getForeColor());
            Gui.drawRect(centerX + 1, centerY + 2, centerX + 2, centerY, getForeColor());
            Gui.drawRect(centerX + 2, centerY, centerX + 3, centerY - 2, getForeColor());
            Gui.drawRect(centerX + 3, centerY - 2, centerX + 4, centerY - 4, getForeColor());
        }
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public void action(GuiEvent event) {
        setValue(!getValue());
    }
}
