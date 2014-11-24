package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;
import mnm.mods.util.Translatable;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

public class GuiSettingEnum<T extends Translatable> extends GuiSetting<T> {

    private String text;
    private final T[] values;
    private int selected;

    @SuppressWarnings("unchecked")
    public GuiSettingEnum(SettingValue<T> setting, int xPos, int yPos, int width, int height) {
        super(setting, xPos, yPos);
        values = (T[]) setting.getValue().getClass().getEnumConstants();
        selected = getCurrentPosition();
        setBounds(xPos, yPos, width, height);

        this.addEventListener(new GuiMouseAdapter() {
            @Override
            public void mousePressed(GuiMouseEvent event) {
                if (event.getButton() == 0) {
                    // Left click, go forward
                    select(selected + 1);
                } else if (event.getButton() == 1) {
                    // Right click, go backward
                    select(selected - 1);
                }
            }
        });
        select(0);
        setBackColor(0xff666666);
    }

    private int getCurrentPosition() {
        int pos = -1;
        T value = getSetting().getValue();
        for (int i = 0; i < this.values.length && pos < 0; i++) {
            if (values[i].equals(value)) {
                pos = i;
            }
        }
        return pos;
    }

    private void select(int id) {
        if (id < 0) {
            id = values.length - 1;
        } else if (id > values.length - 1) {
            id = 0;
        }
        selected = id;
        setValue(values[selected]);
        text = I18n.format(getValue().getUnlocalized());
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        Gui.drawRect(0, 0, getBounds().width, getBounds().height, 0xff000000);
        String string = mc.fontRendererObj.trimStringToWidth(text, getBounds().width);
        int xPos = getBounds().width / 2 - mc.fontRendererObj.getStringWidth(string) / 2;
        int yPos = getBounds().height / 2 - 4;
        mc.fontRendererObj.drawString(string, xPos, yPos, getForeColor());
    }
}
