package mnm.mods.util.gui;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;

import com.google.common.eventbus.Subscribe;

import mnm.mods.util.TexturedModal;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

/**
 * A {@link net.minecraft.client.gui.GuiButton} for the GuiComponent system.
 */
public class GuiButton extends GuiComponent {

    private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    private static final TexturedModal MODAL_NORMAL = new TexturedModal(WIDGETS, 0, 66, 200, 20);
    private static final TexturedModal MODAL_HOVER = new TexturedModal(WIDGETS, 0, 86, 200, 20);
    private static final TexturedModal MODAL_DISABLE = new TexturedModal(WIDGETS, 0, 46, 200, 20);

    private String text = "";
    private ResourceLocation sound = new ResourceLocation("gui.button.press");
    public int packedFGColour;

    /**
     * Instantiates a new button with {@code text} as the display string.
     *
     * @param text The display string
     */
    public GuiButton(String text) {
        this.setText(text);
    }

    @Subscribe
    public void onClick(ActionPerformedEvent action) {
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(getSound(), 1.0F));
    }

    /**
     * Sets the display text for this button.
     *
     * @param text The new text
     */
    public void setText(String text) {
        if (text == null) {
            text = "";
        }
        this.text = text;
    }

    /**
     * Gets the display text for this button.
     *
     * @return The text
     */
    public String getText() {
        return this.text;
    }

    public void setSound(ResourceLocation sound) {
        this.sound = sound;
    }

    public ResourceLocation getSound() {
        return sound;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        FontRenderer fontrenderer = mc.fontRendererObj;
        Rectangle bounds = getBounds();

        mc.getTextureManager().bindTexture(WIDGETS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        TexturedModal modal = this.getHoverState(isHovered());
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
            if (p < 2) {
                p += 2;
            }
            if (p > 14) {
                p -= 2;
            }
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
        } else if (!this.isEnabled()) {
            textColor = 0xA0A0A0;
        } else if (this.isHovered()) {
            textColor = 0xFFFFA0;
        }

        this.drawCenteredString(fontrenderer, getText(), bounds.width / 2, (bounds.height - 8) / 2,
                textColor);

        super.drawComponent(mouseX, mouseY);
    }

    private TexturedModal getHoverState(boolean hovered) {
        TexturedModal modal = GuiButton.MODAL_NORMAL;

        if (!this.isEnabled()) {
            modal = GuiButton.MODAL_DISABLE;
        } else if (hovered) {
            modal = GuiButton.MODAL_HOVER;
        }

        return modal;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(mc.fontRendererObj.getStringWidth(this.getText()) + 8, 20);
    }

}
