package mnm.mods.util.gui;

import org.lwjgl.input.Mouse;

import com.google.common.eventbus.Subscribe;

import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * A slider for double values. Click and drag or scroll to change the value.
 *
 * @author Matthew
 */
public class GuiSlider extends GuiComponent implements IGuiInput<Double> {

    private static final ResourceLocation TRANSPARENCY = new ResourceLocation("mnmutils",
            "textures/transparency.png");

    private boolean vertical;
    private double value;

    public GuiSlider(double value, boolean vertical) {
        this.vertical = vertical;
        this.setValue(value);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        GlStateManager.enableBlend();
        Gui.drawRect(0, 0, getBounds().width, getBounds().height, -1);
        mc.getTextureManager().bindTexture(TRANSPARENCY);
        Gui.drawModalRectWithCustomSizedTexture(1, 1, 0, 0, getBounds().width - 2,
                getBounds().height - 2, 6, 6);
        drawMid();
        if (vertical) {
            int nook = Math.abs((int) (getBounds().height * getValue()) - getBounds().height);
            Gui.drawRect(-1, nook - 1, getBounds().width + 1, nook + 2, 0xffffffff);
            Gui.drawRect(0, nook, getBounds().width, nook + 1, 0xff000000);
        } else {
            int nook = (int) (getBounds().width * getValue());
            Gui.drawRect(nook, 0, nook + 1, getBounds().height, 0xff000000);
        }
        int midX = getBounds().width / 2;
        int midY = getBounds().height / 2;
        drawCenteredString(mc.fontRendererObj, getFormattedValue(), midX, midY, -1);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        super.drawComponent(mouseX, mouseY);
    }

    protected void drawMid() {
        Gui.drawRect(1, 1, getBounds().width - 1, getBounds().height - 1, getForeColor());
    }

    public String getFormattedValue() {
        return String.format("%%%.0f", getValue() * 100);
    }

    @Subscribe
    public void moveSlider(GuiMouseEvent event) {
        if (event.getMouseX() < 0
                || event.getMouseY() < 0
                || event.getMouseX() > getBounds().width
                || event.getMouseY() > getBounds().height) {
            return;
        }
        if ((event.getEvent() == MouseEvent.CLICK || event.getEvent() == MouseEvent.DRAG)
                && Mouse.isButtonDown(0)) {
            double val;
            if (vertical) {
                int y = event.getMouseY();
                val = Math.abs((double) y / (double) getBounds().height - 1);
            } else {
                int x = event.getMouseX();
                val = (double) x / (double) getBounds().width;
            }
            setValue(val);
        }
        if (event.getEvent() == MouseEvent.SCROLL) {
            setValue(getValue() + event.getScroll() / 7360D);
        }
    }

    @Override
    public void setValue(Double value) {
        if (value < 0) {
            value = 0D;
        }
        if (value > 1) {
            value = 1D;
        }
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    public boolean isVertical() {
        return vertical;
    }
}
