package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;

public abstract class GuiSettingNumber<T extends Number> extends GuiSetting<T> {

    public GuiSettingNumber(SettingValue<T> setting, IGuiInput<T> input) {
        super(setting, input);
    }

    public GuiNumericUpDown<T> getNumUpDown() {
        return (GuiNumericUpDown<T>) getInput();
    }

    public class GuiSettingInt extends GuiSettingNumber<Integer> {

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
