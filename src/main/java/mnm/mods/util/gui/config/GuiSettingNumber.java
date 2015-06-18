package mnm.mods.util.gui.config;

import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.GuiNumericUpDown;

/**
 * A base gui setting for numbers. It wraps a {@link GuiNumericUpDown}
 *
 * @author Matthew
 * @param <T>
 * @see GuiSettingDouble
 * @see GuiSettingInt
 */
public abstract class GuiSettingNumber<T extends Number> extends GuiSetting<T> {

    private GuiSettingNumber(SettingValue<T> setting, GuiNumericUpDown<T> input) {
        super(setting, input);
    }

    public GuiNumericUpDown<T> getNumUpDown() {
        return (GuiNumericUpDown<T>) getInput();
    }

    /**
     * Gui setting for integers
     *
     * @author Matthew
     */
    public static class GuiSettingInt extends GuiSettingNumber<Integer> {

        public GuiSettingInt(SettingValue<Integer> setting) {
            super(setting, new GuiNumericUpDown.IntUpDown());
        }

        @Override
        public Integer getValue() {
            return getNumUpDown().getValue();
        }

        @Override
        public void setValue(Integer value) {
            getNumUpDown().setValue(value);
        }
    }

    /**
     * Gui setting for doubles
     *
     * @author Matthew
     */
    public static class GuiSettingDouble extends GuiSettingNumber<Double> {

        public GuiSettingDouble(SettingValue<Double> setting) {
            super(setting, new GuiNumericUpDown.DoubleUpDown());
        }

        @Override
        public Double getValue() {
            return getNumUpDown().getValue();
        }

        @Override
        public void setValue(Double value) {
            getNumUpDown().setValue(value);
        }
    }
}
