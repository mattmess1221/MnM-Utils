package mnm.mods.util.gui;

import java.math.BigInteger;

import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.Subscribe;

import mnm.mods.util.Color;
import mnm.mods.util.Consumer;
import mnm.mods.util.config.Value;
import mnm.mods.util.gui.config.GuiSettingString;
import mnm.mods.util.gui.events.GuiEvent;
import mnm.mods.util.gui.events.GuiKeyboardEvent;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.text.ChatBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

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
        this.current.getBus().register(new Object() {
            @Subscribe
            public void accept(GuiMouseEvent event) {
                if (event.event == GuiMouseEvent.CLICKED) {
                    Color color = Color.of(current.getForeColor());
                    setColor(color);
                }
            }
        });
        this.setLayout(new GuiGridLayout(20, 20));

        this.addComponent(sliderRed = new GuiSliderColor(color.getRed() / 255D, true, GuiSliderColor.Model.RED, color), new int[] { 1, 1, 2, 10 });
        this.addComponent(sliderGreen = new GuiSliderColor(color.getGreen() / 255D, true, GuiSliderColor.Model.GREEN, color), new int[] { 4, 1, 2, 10 });
        this.addComponent(sliderBlue = new GuiSliderColor(color.getBlue() / 255D, true, GuiSliderColor.Model.BLUE, color), new int[] { 7, 1, 2, 10 });
        this.addComponent(sliderAlpha = new GuiSliderColor(color.getAlpha() / 255D, true, GuiSliderColor.Model.ALPHA, color), new int[] { 10, 1, 2, 10 });

        GuiLabel label;

        label = new GuiLabel();
        label.setText(new ChatBuilder().quickTranslate("colors.red").format(EnumChatFormatting.RED).build());
        label.setAngle(300);
        this.addComponent(label, new int[] { 1, 12 });

        label = new GuiLabel();
        label.setText(new ChatBuilder().quickTranslate("chat.green").format(EnumChatFormatting.GREEN).build());
        label.setAngle(300);
        this.addComponent(label, new int[] { 4, 12 });

        label = new GuiLabel();
        label.setText(new ChatBuilder().quickTranslate("colors.blue").format(EnumChatFormatting.BLUE).build());
        label.setAngle(300);
        this.addComponent(label, new int[] { 7, 12 });

        label = new GuiLabel();
        label.setText(new ChatBuilder().quickTranslate("colors.alpha").format(EnumChatFormatting.WHITE).build());
        this.addComponent(label, new int[] { 10, 12 });

        this.addComponent(current, new int[] { 14, 1, 6, 3 });
        this.addComponent(selected, new int[] { 14, 4, 6, 3 });

        string = new GuiSettingString(new Value<String>(""));
        string.getInput().getTextField().setMaxStringLength(8);
        string.getBus().register(new Object() {
            @Subscribe
            public void accept(GuiKeyboardEvent event) {
                if (string.isFocused() && event.key == Keyboard.KEY_RETURN) {
                    String hex = string.getValue();
                    if (hex.matches("^[0-9a-fA-F]{1,8}$")) { // valid hex
                        int c = new BigInteger(hex, 16).intValue();
                        setColor(Color.of(c));
                    }
                }
            }
        });
        this.addComponent(string, new int[] { 14, 8, 6, 2 });

        GuiButton random = new GuiButton(I18n.format("createWorld.customize.custom.randomize"));
        random.getBus().register(new Object() {
            @Subscribe
            public void action(GuiEvent event) {
                setColor(Color.random());
            }
        });
        this.addComponent(random, new int[] { 13, 11, 8, 2 });

        GuiButton cancel = new GuiButton(I18n.format("gui.cancel"));
        cancel.getBus().register(new Object() {
            @Subscribe
            public void action(GuiEvent event) {
                // Close
                getParent().setOverlay(null);
            }
        });
        this.addComponent(cancel, new int[] { 13, 13, 4, 2 });
        GuiButton apply = new GuiButton(I18n.format("gui.done"));
        apply.getBus().register(new Object() {
            @Subscribe
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

        Color color = Color.of(r, g, b, a);
        if (!color.equals(this.color)) {
            this.color = color;
            selected.setForeColor(color.getColor());

            sliderRed.setBase(color);
            sliderGreen.setBase(color);
            sliderBlue.setBase(color);
            sliderAlpha.setBase(color);

            string.setValue(Integer.toHexString(color.getColor()));
        }
    }
}
