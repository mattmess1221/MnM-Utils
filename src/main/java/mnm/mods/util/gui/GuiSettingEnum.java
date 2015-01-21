package mnm.mods.util.gui;

import java.util.Arrays;

import mnm.mods.util.SettingValue;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

public class GuiSettingEnum<T> extends GuiSetting<T> {

    private String text;
    private final T[] values;
    private String[] names;
    private int selected;

    public GuiSettingEnum(SettingValue<T> setting, T[] values, String[] names) {
        this(setting, values);
        this.names = names;
        select(Arrays.binarySearch(values, setting.getValue()));
    }

    public GuiSettingEnum(SettingValue<T> setting, T[] values) {
        this(setting, values, 0, 0, 0, 0);
    }

    public GuiSettingEnum(SettingValue<T> setting, T[] values, int xPos, int yPos, int width,
            int height) {
        super(setting, xPos, yPos);
        this.values = values;
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
        select(Arrays.binarySearch(values, setting.getValue()));
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
        String text = values[selected].toString();
        if (names != null) {
            text = names[selected];
        }
        this.text = I18n.format(text);
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
