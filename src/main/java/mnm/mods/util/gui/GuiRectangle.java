package mnm.mods.util.gui;

import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a colored area. Transparency is represented using a transparency
 * grid.
 *
 * @author Matthew
 */
public class GuiRectangle extends GuiComponent {

    private static final ResourceLocation TRANSPARENCY = new ResourceLocation("mnmutils",
            "textures/transparency.png");

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        GlStateManager.enableAlpha();
        mc.getTextureManager().bindTexture(TRANSPARENCY);
        Rectangle r = getBounds();
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, r.width, r.height, 5, 5);
        Gui.drawRect(0, 0, getBounds().width, getBounds().height, getForeColor());
        GlStateManager.disableBlend();
    }
}
