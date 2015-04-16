package mnm.mods.util.gui;

import java.awt.Rectangle;

import mnm.mods.util.SettingValue;

public abstract class GuiSettingNumber<T extends Number> extends GuiSetting<T> {

    private GuiNumericUpDown nud;

    public GuiSettingNumber(SettingValue<T> setting) {
        super(setting);
        this.nud = new GuiNumericUpDown();
        this.nud.setValue(setting.getValue().doubleValue());
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (nud != null) {
            nud.drawComponent(mouseX, mouseY);
        }
    }

    @Override
    void setParent(GuiPanel guiPanel) {
        this.nud.setParent(guiPanel);
        super.setParent(guiPanel);
    }

    @Override
    public void setBounds(Rectangle bounds) {
        if (nud != null) {
            nud.setBounds(bounds);
        }
        super.setBounds(bounds);
    }

    @Override
    public void setSize(int width, int height) {
        if (nud != null) {
            nud.setSize(width, height);
        }
        super.setSize(width, height);
    }

    @Override
    public void setPosition(int xPos, int yPos) {
        if (nud != null) {
            nud.setPosition(xPos, yPos);
        }
        super.setPosition(xPos, yPos);
    }

    @Override
    public void updateComponent() {
        if (nud != null) {
            nud.updateComponent();
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (nud != null) {
            nud.handleMouseInput();
        }
    }

    public GuiNumericUpDown getNumUpDown() {
        return nud;
    }

    @Override
    public abstract T getValue();

    @Override
    public abstract void setValue(T value);
}
