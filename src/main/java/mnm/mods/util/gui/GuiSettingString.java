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
        super(setting);
        this.textField = new GuiTextField(0, mc.fontRendererObj, 0, 0, 1, 1);
        this.textField.setText(setting.getValue());
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
        if (getBounds() != null
                && (bounds.width != getBounds().width || bounds.height != getBounds().height)) {
            updateTextbox(bounds.width, bounds.height);
        }
        super.setBounds(bounds);
    }

    @Override
    public void setSize(int width, int height) {
        if (width != getBounds().width || height != getBounds().height) {
            updateTextbox(width, height);
        }
        super.setSize(width, height);
    }

    private void updateTextbox(int width, int height) {
        // Create a new instance so it works without forge
        String text = "";
        int max = 32;
        boolean bkgnd = true;
        if (textField != null) {
            text = textField.getText();
            max = textField.getMaxStringLength();
            bkgnd = textField.getEnableBackgroundDrawing();
        }
        textField = new GuiTextField(0, mc.fontRendererObj, 0, 0, width, height);
        textField.setText(text);
        textField.setMaxStringLength(max);
        textField.setEnableBackgroundDrawing(bkgnd);
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

    @Override
    public void setForeColor(int foreColor) {
        textField.setTextColor(foreColor);
        super.setForeColor(foreColor);
    }

    public GuiTextField getTextField() {
        return textField;
    }
}
