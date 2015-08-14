package mnm.mods.util.gui.config;

import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.IFocusable;
import mnm.mods.util.gui.config.GuiSetting.GuiSettingWrapped;

/**
 * A Gui input that wraps a {@link GuiText}.
 *
 * @author Matthew
 */
public class GuiSettingString extends GuiSettingWrapped<String, GuiText> implements IFocusable {

    public GuiSettingString(SettingValue<String> setting) {
        super(setting, new GuiText());
    }

    @Override
    public boolean isFocused() {
        return getInput().isFocused();
    }

    @Override
    public void setFocused(boolean focus) {
        getInput().setFocused(focus);
    }

    @Deprecated
    public GuiText getTextField() {
        return getInput();
    }
}
