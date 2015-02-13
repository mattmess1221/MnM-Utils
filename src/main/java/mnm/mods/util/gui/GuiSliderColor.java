package mnm.mods.util.gui;

import mnm.mods.util.Color;
import net.minecraft.client.gui.Gui;

public class GuiSliderColor extends GuiSlider {

    private Model model;
    private Color base;

    public GuiSliderColor(double value, boolean vertical, Model model, Color base) {
        super(value, vertical);
        this.model = model;
        this.base = base;
    }

    @Override
    protected void drawMid() {
        if (isVertical()) {
            for (int i = 0; i < getBounds().height; i++) {
                int color = (int) ((double) i / (double) getBounds().height * 255D);
                color = Math.abs(color - 256);
                color = getColor(color);
                Gui.drawRect(0, i, getBounds().width, i + 1, color);
            }
        } else {
            for (int i = 0; i < getBounds().width; i++) {
                int color = (int) ((double) i / (double) getBounds().width * 255D);
                // color = Math.abs(color - 256);
                color = getColor(color);
                Gui.drawRect(i, 0, i + 1, getBounds().height, color);
            }
        }
    }

    @Override
    public String getFormattedValue() {
        return Integer.toString((int) (getValue() * 255D));
    }

    private int getColor(int i) {
        i %= 256;
        switch (model) {
        case RED:
            return Color.getColor(i, base.getGreen(), base.getBlue(), base.getAlpha());
        case GREEN:
            return Color.getColor(base.getRed(), i, base.getBlue(), base.getAlpha());
        case BLUE:
            return Color.getColor(base.getRed(), base.getGreen(), i, base.getAlpha());
        case ALPHA:
            return Color.getColor(base.getRed(), base.getGreen(), base.getBlue(), i);
        }
        return -1;
    }

    public void setBase(Color color) {
        this.base = color;
    }

    public static enum Model {
        RED,
        GREEN,
        BLUE,
        ALPHA;
    }
}
