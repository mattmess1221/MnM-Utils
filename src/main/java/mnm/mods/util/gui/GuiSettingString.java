package mnm.mods.util.gui;

import java.awt.Rectangle;

import mnm.mods.util.SettingValue;
import mnm.mods.util.gui.events.GuiKeyboardAdapter;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiSettingString extends GuiSetting<String> implements Focusable, GuiMouseAdapter,
        GuiKeyboardAdapter {

    private GuiTextField textField;

    public GuiSettingString(SettingValue<String> setting) {
        this(setting, 0, 0, 1, 1);
    }

    public GuiSettingString(SettingValue<String> setting, int xPos, int yPos, int width, int height) {
        super(setting, xPos, yPos);
        textField = new GuiTextField(0, mc.fontRendererObj, 0, 0, width, height);
        textField.setText(getValue());
    }

    @Override
    public void accept(GuiMouseEvent event) {
        if (event.event == GuiMouseEvent.CLICKED) {
            setFocused(true);
        }
    }

    @Override
    public void accept(GuiKeyboardEvent event) {
        if (Keyboard.isKeyDown(event.key)) {
            textField.textboxKeyTyped(event.character, event.key);
            setValue(textField.getText());
        }
    }

    @Override
    public void setBounds(Rectangle bounds) {
        setTextboxSize(bounds.width, bounds.height);
        super.setBounds(bounds);
    }

    @Override
    public void setSize(int width, int height) {
        setTextboxSize(width, height);
        super.setSize(width, height);
    }

    private void setTextboxSize(int width, int height) {
        if (textField != null) {
            textField.width = width;
            textField.height = height;
        }
    }

    @Override
    public void setFocused(boolean focus) {
        textField.setFocused(focus);
        Keyboard.enableRepeatEvents(focus);
    }

    @Override
    public boolean isFocused() {
        return textField.isFocused();
    }

    @Override
    public void updateComponent() {
        textField.updateCursorCounter();
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        textField.drawTextBox();
    }

    public GuiTextField getTextField() {
        return textField;
    }
}
