package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;

/**
 * A Gui input that wraps a {@link GuiText}.
 *
 * @author Matthew
 */
public class GuiSettingString extends GuiSetting<String> implements IFocusable {

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
