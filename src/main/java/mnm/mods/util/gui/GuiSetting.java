package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;

/**
 * A base setting gui wrapper.
 *
 * @author Matthew
 * @param <T> The setting type
 */
public abstract class GuiSetting<T> extends GuiComponent implements IGuiInput<T> {

    private final SettingValue<T> setting;
    private final IGuiInput<T> input;

    public GuiSetting(SettingValue<T> setting2) {
        this(setting2, null);
    }

    public GuiSetting(SettingValue<T> setting, IGuiInput<T> input) {
        this.setting = setting;
        this.input = input;
        if (input instanceof GuiComponent) {
            wrap((GuiComponent) input);
        }
        if (input != null) {
            this.setValue(setting.getValue());
        }
    }

    /**
     * Gets the input object.
     *
     * @return The input object
     */
    public IGuiInput<T> getInput() {
        return input;
    }

    /**
     * Gets the setting object.
     *
     * @return The setting object
     */
    public SettingValue<T> getSetting() {
        return this.setting;
    }

    /**
     * Resets the value to setting value.
     */
    public void reset() {
        this.setValue(this.setting.getValue());
    }

    /**
     * Sets the value to the setting default.
     */
    public void setDefault() {
        this.setValue(this.setting.getDefaultValue());
    }

    /**
     * Sets the setting value to the value.
     */
    public void saveValue() {
        this.setting.setValue(this.getValue());
    }
}
