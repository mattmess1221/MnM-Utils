package mnm.mods.util.gui;

import mnm.mods.util.SettingValue;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiSettingString extends GuiSetting<String> implements Focusable {

    private GuiTextField textField;

    public GuiSettingString(SettingValue<String> setting, int xPos, int yPos, int width, int height) {
        super(setting, xPos, yPos);
        textField = new GuiTextField(0, mc.fontRendererObj, 0, 0, width, height);
        textField.setText(getValue());
        setSize(100, 20);

        this.addMouseAdapter(event -> {
            if (event.event == GuiMouseEvent.CLICKED) {
                setFocused(true);
            }
        });

        this.addKeyboardAdapter(event -> {
            if (Keyboard.isKeyDown(event.key)) {
                textField.textboxKeyTyped(event.character, event.key);
                setValue(textField.getText());
            }
        });
    }

    @Override
    public void setFocused(boolean focus) {
        textField.setFocused(focus);
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
}
