package mnm.mods.util.gui;

import java.awt.Point;
import java.awt.Rectangle;

import mnm.mods.util.gui.events.GuiKeyboardAdapter;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.GuiTextField;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

/**
 * A gui component that wraps {@link GuiTextField}.
 *
 * @author Matthew
 */
public class GuiText extends GuiComponent implements IGuiInput<String>, IFocusable,
        GuiMouseAdapter, GuiKeyboardAdapter {

    private GuiTextField textField;
    private String hint;

    public GuiText() {
        this.textField = new GuiTextField(0, mc.fontRendererObj, 0, 0, 0, 0);
    }

    @Override
    public void accept(GuiMouseEvent event) {
        if (event.event == GuiMouseEvent.CLICKED) {
            setFocused(true);
            // it's vanilla at (0,0) so subtract the current pos.
            Point pos = getActualPosition();
            int x = event.position.x - pos.x;
            int y = event.position.y - pos.y;
            // send to text field.
            textField.mouseClicked(x, y, 0);
        }
    }

    @Override
    public void accept(GuiKeyboardEvent event) {
        if (Keyboard.isKeyDown(event.key)) {
            textField.textboxKeyTyped(event.character, event.key);
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
