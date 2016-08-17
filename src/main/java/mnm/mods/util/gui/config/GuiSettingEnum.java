package mnm.mods.util.gui.config;

import java.util.Arrays;

import com.google.common.eventbus.Subscribe;

import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.config.Value;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

/**
 * A setting for set values, such as enums. Values should either have a
 * toString() method or provide a names array during construction.
 *
 * @author Matthew
 * @param <T> The type
 */
public class GuiSettingEnum<T> extends GuiSetting<T> {

    private final T[] values;
    private final String[] names;

    private T value;

    private String text;
    private int selected;

    public GuiSettingEnum(Value<T> setting, T[] values) {
        this(setting, values, null);
    }

    public GuiSettingEnum(Value<T> setting, T[] values, String[] names) {
        super(setting);
        this.names = names;
        this.values = values;
        selected = getCurrentPosition();

        select(Arrays.binarySearch(values, setting.get()));
        setSecondaryColor(Color.DARK_GRAY);
    }

    @Subscribe
    public void activate(GuiMouseEvent event) {
        if (event.getType() == MouseEvent.CLICK) {
            if (event.getButton() == 0) {
                // Left click, go forward
                select(selected + 1);
            } else if (event.getButton() == 1) {
                // Right click, go backward
                select(selected - 1);
            }
        }
    }

    private int getCurrentPosition() {
        int pos = -1;
        T value = getSetting().get();
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
        ILocation loc = this.getLocation();
        Gui.drawRect(0, 0, loc.getWidth(), loc.getHeight(), 0xff000000);
        String string = mc.fontRendererObj.trimStringToWidth(text, loc.getWidth());
        int xPos = loc.getWidth() / 2 - mc.fontRendererObj.getStringWidth(string) / 2;
        int yPos = loc.getHeight() / 2 - 4;
        mc.fontRendererObj.drawString(string, xPos, yPos, getPrimaryColorProperty().getHex());
        drawBorders(0, -1, loc.getWidth(), loc.getHeight() + 1);
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
