package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;

public class GuiSettingString extends GuiSetting<String> implements Focusable {

    public GuiSettingString(SettingValue<String> setting) {
        super(setting, new GuiText());
    }

    @Override
    public boolean isFocused() {
        return getTextField().isFocused();
    }

    @Override
    public void setFocused(boolean focus) {
        getTextField().setFocused(focus);
    }

    @Override
    public String getValue() {
        return getSetting().getValue();
    }

    @Override
    public void setValue(String value) {
        getSetting().setValue(value);
    }

    public GuiText getTextField() {
        return (GuiText) getInput();
    }
}
