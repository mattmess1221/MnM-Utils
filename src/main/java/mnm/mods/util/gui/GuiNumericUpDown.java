package mnm.mods.util.gui;

import java.awt.Rectangle;
import java.text.NumberFormat;

import mnm.mods.util.gui.events.GuiEvent;

public class GuiNumericUpDown extends GuiPanel {

    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;
    private double interval = 1;

    private double value;

    private NumberFormat format = NumberFormat.getNumberInstance();

    public GuiNumericUpDown() {
        setLayout(new BorderLayout());

        {
            GuiPanel text = new GuiPanel();
            GuiRectangle rect = new GuiRectangle() {
                @Override
                public Rectangle getBounds() {
                    return getParent().getBounds();
                }
            };
            rect.setForeColor(0xff000000);
            text.addComponent(rect);
            GuiLabel label = new GuiLabel("") {
                @Override
                public String getString() {
                    return format.format(getValue());
                }
            };
            label.setPosition(5, 7);
            text.addComponent(label);

            addComponent(text, BorderLayout.Position.CENTER);
        }
        {
            GuiPanel pnlButtons = new GuiPanel(new GuiGridLayout(1, 2));
            GuiButton up = new UpDown("\u2191", 1); // upsidedown v
            GuiButton down = new UpDown("\u2193", -1);
            pnlButtons.addComponent(up, new int[] { 0, 0 });
            pnlButtons.addComponent(down, new int[] { 0, 1 });

            addComponent(pnlButtons, BorderLayout.Position.EAST);
        }
    }

    public void increment(int n) {
        setValue(getValue() + n * getInterval());
    }

    public void setFormat(NumberFormat numberFormat) {
        this.format = numberFormat;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        value = Math.max(value, getMin());
        value = Math.min(value, getMax());
        this.value = value;
    }

    private class UpDown extends GuiButton {

        private int direction;

        public UpDown(String text, int direction) {
            super(text);
            this.direction = direction;
            setBackColor(0xff666666);
        }

        @Override
        public Rectangle getBounds() {
            Rectangle bounds = super.getBounds();
            bounds.width = 7;
            bounds.height = 6;
            return bounds;
        }

        @Override
        public void action(GuiEvent event) {
            increment(direction);
        }
    }
}
