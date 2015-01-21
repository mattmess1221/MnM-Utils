package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;
import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import net.minecraft.client.gui.Gui;

public class GuiSettingBoolean extends GuiSetting<Boolean> {

    private String title;

    public GuiSettingBoolean(SettingValue<Boolean> setting, String title) {
        this(setting, 0, 0, title);
    }

    public GuiSettingBoolean(SettingValue<Boolean> setting, int xPos, int yPos, String title) {
        super(setting, xPos, yPos);
        this.setSize(9, 9);
        this.addEventListener(new ActionPerformed() {
            @Override
            public void actionPerformed(GuiEvent event) {
                setValue(!getValue());
            }
        });
        this.title = title;
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

        mc.fontRendererObj.drawString(title, 15, 1, -1);
    }
}
