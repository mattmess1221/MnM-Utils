package mnm.mods.util.gui.config;

import mnm.mods.util.config.Value;
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

    private final Value<T> setting;

    public GuiSetting(Value<T> setting) {
        this.setting = setting;
        this.setValue(setting.get());
    }

    /**
     * Gets the setting object.
     *
     * @return The setting object
     */
    public Value<T> getSetting() {
        return this.setting;
    }

    /**
     * Resets the value to setting value.
     */
    public void reset() {
        this.setValue(this.setting.get());
    }

    /**
     * Sets the setting value to the value.
     */
    public void saveValue() {
        this.setting.set(this.getValue());
    }

    public static class GuiSettingWrapped<T, Wrapper extends IGuiInput<T>> extends GuiSetting<T> {

        private final Wrapper input;

        public GuiSettingWrapped(Value<T> setting, Wrapper input) {
            super(setting);
            this.input = input;
            this.input.setValue(setting.get());
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
