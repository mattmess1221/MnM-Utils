package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;

public class GuiSettingFloat extends GuiSettingNumber<Float> {

    public GuiSettingFloat(SettingValue<Float> setting) {
        super(setting);
    }

    @Override
    public Float getValue() {
        return (float) getNumUpDown().getValue();
    }

    @Override
    public void setValue(Float value) {
        if (getNumUpDown() != null) {
            getNumUpDown().setValue(value);
        }
    }
}
