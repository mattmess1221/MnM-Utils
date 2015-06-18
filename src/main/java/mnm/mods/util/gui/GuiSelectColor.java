package mnm.mods.util.gui;

import java.math.BigInteger;

import mnm.mods.util.Color;
import mnm.mods.util.Consumer;
import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.config.GuiSettingString;
import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import mnm.mods.util.gui.events.GuiKeyboardAdapter;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

/**
 * A gui used to select a color.
 *
 * @author Matthew
 */
public class GuiSelectColor extends GuiPanel {

    private Consumer<Color> callback;
    private Color color;

    private GuiSliderColor sliderRed;
    private GuiSliderColor sliderGreen;
    private GuiSliderColor sliderBlue;
    private GuiSliderColor sliderAlpha;

    private GuiSettingString string;

    private GuiRectangle current = new GuiRectangle();
    private GuiRectangle selected = new GuiRectangle();

    /**
     * Creates a new instance with a color and callback.
     *
     * @param callback_ Called when apply is clicked
     * @param color The starting color
     */
    public GuiSelectColor(Consumer<Color> callback_, Color color) {
        this.callback = callback_;
        this.current.setForeColor(color.getColor());
        this.selected.setForeColor(color.getColor());
        this.current.addMouseAdapter(new GuiMouseAdapter() {
            @Override
            public void accept(GuiMouseEvent event) {
                if (event.event == GuiMouseEvent.CLICKED) {
                    Color color = new Color(current.getForeColor());
                    setColor(color);
                }
            }
        });
        this.setLayout(new GuiGridLayout(20, 20));

        this.addComponent(sliderRed = new GuiSliderColor(color.getRed() / 255D, true,
                GuiSliderColor.Model.RED, color), new int[] { 1, 1, 2, 10 });
        this.addComponent(sliderGreen = new GuiSliderColor(color.getGreen() / 255D, true,
                GuiSliderColor.Model.GREEN, color), new int[] { 4, 1, 2, 10 });
        this.addComponent(sliderBlue = new GuiSliderColor(color.getBlue() / 255D, true,
                GuiSliderColor.Model.BLUE, color), new int[] { 7, 1, 2, 10 });
        this.addComponent(sliderAlpha = new GuiSliderColor(color.getAlpha() / 255D, true,
                GuiSliderColor.Model.ALPHA, color), new int[] { 10, 1, 2, 10 });

        this.addComponent(new GuiLabel(EnumChatFormatting.RED + I18n.format("colors.red"), 300),
                new int[] { 1, 12 });
        this.addComponent(
                new GuiLabel(EnumChatFormatting.GREEN + I18n.format("colors.green"), 300),
                new int[] { 4, 12 });
        this.addComponent(new GuiLabel(EnumChatFormatting.BLUE + I18n.format("colors.blue"), 300),
                new int[] { 7, 12 });
        this.addComponent(
                new GuiLabel(EnumChatFormatting.WHITE + I18n.format("colors.alpha"), 300),
                new int[] { 10, 12 });

        this.addComponent(current, new int[] { 14, 1, 6, 3 });
        this.addComponent(selected, new int[] { 14, 4, 6, 3 });

        string = new GuiSettingString(new SettingValue<String>(""));
        string.getTextField().getTextField().setMaxStringLength(8);
        string.addKeyboardAdapter(new GuiKeyboardAdapter() {
            @Override
            public void accept(GuiKeyboardEvent event) {
                if (string.isFocused() && event.key == Keyboard.KEY_RETURN) {
                    String hex = string.getTextField().getTextField().getText();
                    if (hex.matches("^[0-9a-fA-F]{1,8}$")) { // valid hex
                        int c = new BigInteger(hex, 16).intValue();
                        setColor(new Color(c));
                    }
                }
            }
        });
        this.addComponent(string, new int[] { 14, 8, 6, 2 });

        GuiButton random = new GuiButton(I18n.format("createWorld.customize.custom.randomize"));
        random.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                setColor(Color.random());
            }
        });
        this.addComponent(random, new int[] { 13, 11, 8, 2 });

        GuiButton cancel = new GuiButton(I18n.format("gui.cancel"));
        cancel.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                // Close
                getParent().setOverlay(null);
            }
        });
        this.addComponent(cancel, new int[] { 13, 13, 4, 2 });
        GuiButton apply = new GuiButton(I18n.format("gui.done"));
        apply.addActionListener(new ActionPerformed() {
            @Override
            public void action(GuiEvent event) {
                callback.apply(GuiSelectColor.this.color);
            }
        });
        this.addComponent(apply, new int[] { 17, 13, 4, 2 });

        setColor(color);
    }

    public void setColor(Color color) {
        sliderRed.setValue(color.getRed() / 255D);
        sliderGreen.setValue(color.getGreen() / 255D);
        sliderBlue.setValue(color.getBlue() / 255D);
        sliderAlpha.setValue(color.getAlpha() / 255D);
    }

    @Override
    public void updateComponent() {
        super.updateComponent();

        int r = (int) (sliderRed.getValue() * 255);
        int g = (int) (sliderGreen.getValue() * 255);
        int b = (int) (sliderBlue.getValue() * 255);
        int a = (int) (sliderAlpha.getValue() * 255);

        Color color = new Color(r, g, b, a);
        if (!color.equals(this.color)) {
            this.color = color;
            selected.setForeColor(color.getColor());

            sliderRed.setBase(color);
            sliderGreen.setBase(color);
            sliderBlue.setBase(color);
            sliderAlpha.setBase(color);

            string.getTextField().getTextField().setText(Integer.toHexString(color.getColor()));
        }
    }
}
