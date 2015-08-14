package mnm.mods.util.gui.config;

import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.GuiCheckbox;
import mnm.mods.util.gui.config.GuiSetting.GuiSettingWrapped;

/**
 * A gui input for booleans as a checkbox.
 *
 * @author Matthew
 */
public class GuiSettingBoolean extends GuiSettingWrapped<Boolean, GuiCheckbox> {

    public GuiSettingBoolean(SettingValue<Boolean> setting) {
        super(setting, new GuiCheckbox());
    }

    @Deprecated
    public GuiCheckbox getCheckbox() {
        return getInput();
    }
}
