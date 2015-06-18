package mnm.mods.util.gui.config;

import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.IFocusable;

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
