package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Rectangle;

import mnm.mods.util.TexturedModal;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiButton extends GuiComponent {

    private final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    protected TexturedModal MODAL_NORMAL = new TexturedModal(WIDGETS, 0, 46, 200, 20);
    protected TexturedModal MODAL_HOVER = new TexturedModal(WIDGETS, 0, 66, 200, 20);
    protected TexturedModal MODAL_DISABLE = new TexturedModal(WIDGETS, 0, 86, 200, 20);
    protected String text = "";
    public int packedFGColour;

    public GuiButton(GuiPanel parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
    }

    public GuiButton(GuiPanel parent, String text) {
        this(parent, 0, 0, 100, 20);
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        FontRenderer fontrenderer = mc.fontRendererObj;
        Rectangle bounds = getBounds();

        mc.getTextureManager().bindTexture(WIDGETS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        TexturedModal modal = this.getHoverState(hovered);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // draw top left
        this.drawTexturedModalRect(0, 0, modal.getXPos(), modal.getYPos(), bounds.width / 2, 2);
        // draw top right
        this.drawTexturedModalRect(bounds.width / 2, 0, modal.getXPos() + modal.getWidth()
                - bounds.width / 2, modal.getYPos(), bounds.width / 2, 2);
        int pos = 2;
        // draw middles
        while (pos < bounds.height) {
            int p = pos % 16;
            if (p < 2)
                p += 2;
            if (p > 14)
                p -= 2;
            // draw left
            this.drawTexturedModalRect(0, pos, modal.getXPos(), modal.getYPos() + p,
                    bounds.width / 2, 1);
            // draw right
            this.drawTexturedModalRect(bounds.width / 2, pos, modal.getXPos() + modal.getWidth()
                    - bounds.width / 2, modal.getYPos() + p, bounds.width / 2, 1);
            pos++;
        }
        // draw bottom left
        this.drawTexturedModalRect(0, bounds.height - 2, 0, modal.getYPos() + (18),
                bounds.width / 2, 2);
        // draw bottom right
        this.drawTexturedModalRect(bounds.width / 2, bounds.height - 2, 200 - bounds.width / 2,
                modal.getYPos() + 18, bounds.width / 2, 2);

        int textColor = 0xE0E0E0;

        if (packedFGColour != 0) {
            textColor = packedFGColour;
        } else if (!this.enabled) {
            textColor = 0xA0A0A0;
        } else if (this.hovered) {
            textColor = 0xFFFFA0;
        }

        this.drawCenteredString(fontrenderer, text, bounds.width / 2, (bounds.height - 8) / 2,
                textColor);

    }

    private TexturedModal getHoverState(boolean hovered) {
        TexturedModal modal = this.MODAL_NORMAL;

        if (!this.enabled) {
            modal = this.MODAL_DISABLE;
        } else if (hovered) {
            modal = this.MODAL_HOVER;
        }

        return modal;
    }

    @Override
    public Dimension getPreferedSize() {
        return new Dimension(mc.fontRendererObj.getStringWidth(this.text) + 8, 20);
    }

}
