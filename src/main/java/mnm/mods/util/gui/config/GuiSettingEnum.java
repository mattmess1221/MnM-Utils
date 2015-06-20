package mnm.mods.util.gui.config;

import java.util.Arrays;

import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

/**
 * A setting for set values, such as enums. Values should either have a
 * toString() method or provide a names array during construction.
 *
 * @author Matthew
 * @param <T> The type
 */
public class GuiSettingEnum<T> extends GuiSetting<T> implements GuiMouseAdapter {

    private T value;

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
        super(setting);
        this.values = values;
        selected = getCurrentPosition();

        select(Arrays.binarySearch(values, setting.getValue()));
        setBackColor(0xff666666);
    }

    @Override
    public void accept(GuiMouseEvent event) {
        if (event.event == GuiMouseEvent.CLICKED) {
            if (event.button == 0) {
                // Left click, go forward
                select(selected + 1);
            } else if (event.button == 1) {
                // Right click, go backward
                select(selected - 1);
            }
        }
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
        drawBorders(0, -1, getBounds().width, getBounds().height + 1);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}