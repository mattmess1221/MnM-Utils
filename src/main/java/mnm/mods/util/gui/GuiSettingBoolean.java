package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;

public class GuiSettingBoolean extends GuiSetting<Boolean> {

    private boolean value;
    private GuiCheckbox checkbox;

    public GuiSettingBoolean(SettingValue<Boolean> setting) {
        super(setting, new GuiCheckbox());
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean b) {
        value = b;
    }

    public GuiCheckbox getCheckbox() {
        return checkbox;
    }
}
