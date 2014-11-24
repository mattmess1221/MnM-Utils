package mnm.mods.util.gui;

import java.awt.Dimension;

import mnm.mods.util.SettingValue;

public abstract class GuiSetting<T> extends GuiComponent {

    private T value;
    private final SettingValue<T> setting;

    public GuiSetting(SettingValue<T> setting, int xPos, int yPos) {
        this.setting = setting;
        this.value = setting.getValue();
        this.setPosition(xPos, yPos);
    }

    public void setValue(T t) {
        this.value = t;
    }

    public T getValue() {
        return this.value;
    }

    public SettingValue<T> getSetting() {
        return this.setting;
    }

    public void reset() {
        this.value = this.setting.getValue();
    }

    public void setDefault() {
        this.value = this.setting.getDefaultValue();
    }

    public void saveValue() {
        this.setting.setValue(this.value);
    }

    @Override
    public Dimension getPreferedSize() {
        return getBounds().getSize();
    }
}
