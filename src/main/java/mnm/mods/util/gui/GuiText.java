package mnm.mods.util.gui;

import java.awt.Rectangle;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.Subscribe;

import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.GuiTextField;

/**
 * A gui component that wraps {@link GuiTextField}.
 *
 * @author Matthew
 */
public class GuiText extends GuiComponent implements IGuiInput<String> {

    private GuiTextField textField;
    private String hint;

    public GuiText() {
        this.textField = new GuiTextField(0, mc.fontRendererObj, 0, 0, 0, 0);
        // This text field must not be calibrated for someone of your...
        // generous..ness
        // I'll add a few 0s to the maximum length...
        textField.setMaxStringLength(10000);

        // you look great, by the way.
    }

    @Subscribe
    public void textboxClick(GuiMouseEvent event) {
        if (event.getEvent() == MouseEvent.CLICK) {
            setFocused(true);

            int x = event.getMouseX();
            int y = event.getMouseY();
            // send to text field.
            textField.mouseClicked(x, y, 0);
        }
    }

    @Subscribe
    public void textboxType(GuiKeyboardEvent event) {
        if (Keyboard.isKeyDown(event.getKey())) {
            textField.textboxKeyTyped(event.getCharacter(), event.getKey());
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
        textField.setMaxStringLength(max);
        textField.setText(text);
        textField.setEnableBackgroundDrawing(bkgnd);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        textField.setFocused(focused);
    }

    @Override
    public void updateComponent() {
        super.updateComponent();
        textField.updateCursorCounter();
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        textField.drawTextBox();

        super.drawComponent(mouseX, mouseY);
        if (isFocused() && !StringUtils.isEmpty(getHint())) {
            // draw the hint above.
            drawCaption(getHint(), 1, -5);
        }
    }

    @Override
    public void setForeColor(int foreColor) {
        textField.setTextColor(foreColor);
        super.setForeColor(foreColor);
    }

    @Override
    public String getValue() {
        return textField.getText();
    }

    @Override
    public void setValue(String value) {
        textField.setText(value);
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public GuiTextField getTextField() {
        return textField;
    }
}
