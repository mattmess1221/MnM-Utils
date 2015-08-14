package mnm.mods.util.gui.config;

import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.IGuiInput;

/**
 * A base setting gui wrapper.
 *
 * @author Matthew
 * @param <T> The setting type
 * @param <Wrapper> The gui this setting will wrap
 */
public abstract class GuiSetting<T> extends GuiComponent implements IGuiInput<T> {

    private final SettingValue<T> setting;

    public GuiSetting(SettingValue<T> setting) {
        this.setting = setting;
        this.setValue(setting.getValue());
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

    public static class GuiSettingWrapped<T, Wrapper extends IGuiInput<T>> extends GuiSetting<T> {

        private final Wrapper input;

        public GuiSettingWrapped(SettingValue<T> setting, Wrapper input) {
            super(setting);
            this.input = input;
            this.input.setValue(setting.getValue());
            if (input instanceof GuiComponent) {
                wrap((GuiComponent) input);
            }
        }

        /**
         * Gets the input object.
         *
         * @return The input object
         */
        public Wrapper getInput() {
            return input;
        }

        @Override
        public T getValue() {

            return getInput().getValue();
        }

        @Override
        public void setValue(T value) {
            if (getInput() == null) {
                return;
            }
            getInput().setValue(value);
        }

    }
}
