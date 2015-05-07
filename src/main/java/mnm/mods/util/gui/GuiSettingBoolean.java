package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;

/**
 * A gui input for booleans as a checkbox.
 *
 * @author Matthew
 */
public class GuiSettingBoolean extends GuiSetting<Boolean> {

    public GuiSettingBoolean(SettingValue<Boolean> setting) {
        super(setting, new GuiCheckbox());
    }

    @Override
    public Boolean getValue() {
        return getCheckbox().getValue();
    }

    @Override
    public void setValue(Boolean b) {
        getCheckbox().setValue(b);
    }

    public GuiCheckbox getCheckbox() {
        return (GuiCheckbox) getInput();
    }
}
