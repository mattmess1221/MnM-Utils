package mnm.mods.util.gui.config;

import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.config.GuiSetting.GuiSettingWrapped;

/**
 * A Gui input that wraps a {@link GuiText}.
 *
 * @author Matthew
 */
public class GuiSettingString extends GuiSettingWrapped<String, GuiText> {

    public GuiSettingString(SettingValue<String> setting) {
        super(setting, new GuiText());
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Deprecated
    public GuiText getTextField() {
        return getInput();
    }
}
