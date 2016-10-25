package mnm.mods.util.gui.config;

import javax.annotation.Nonnull;

import mnm.mods.util.config.Value;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiWrappedComponent;
import mnm.mods.util.gui.IGuiInput;

/**
 * A base setting gui wrapper.
 *
 * @author Matthew
 * @param <T> The setting type
 * @param <W> The gui this setting will wrap
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

    @Override
    public void updateComponent() {
        getSetting().set(getValue());
    }

    private abstract static class Wrapped<T, W extends GuiComponent> extends GuiWrappedComponent<W> implements IGuiInput<T> {

        private final Value<T> setting;

        public Wrapped(Value<T> setting, @Nonnull W wrap) {
            super(wrap);
            this.setting = setting;
        }

        public Value<T> getSetting() {
            return this.setting;
        }

        @Override
        public void updateComponent() {
            super.updateComponent();
            getSetting().set(getValue());
        }

    }

    public static class GuiSettingWrapped<T, Wrapper extends GuiComponent & IGuiInput<T>>
            extends Wrapped<T, Wrapper> {

        public GuiSettingWrapped(Value<T> setting, @Nonnull Wrapper input) {
            super(setting, input);
            input.setValue(setting.get());
        }

        /**
         * Gets the input object.
         *
         * @return The input object
         * @deprecated Use {@link #getComponent()}
         */
        @Deprecated
        public Wrapper getInput() {
            return this.getComponent();
        }

        @Override
        public T getValue() {
            return getComponent() == null ? null : getComponent().getValue();
        }

        @Override
        public void setValue(T value) {
            if (getComponent() != null) {
                getComponent().setValue(value);
            }
        }

    }
}
