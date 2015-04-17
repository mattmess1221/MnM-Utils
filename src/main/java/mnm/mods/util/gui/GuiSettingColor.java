package mnm.mods.util.gui;

import java.awt.Rectangle;

import mnm.mods.util.Color;
import mnm.mods.util.Consumer;
import mnm.mods.util.SettingValue;
import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiSettingColor extends GuiSetting<Color> implements Consumer<Color>, ActionPerformed {

    private static final ResourceLocation TRANSPARENCY = new ResourceLocation("mnmutils",
            "textures/transparency.png");

    public GuiSettingColor(SettingValue<Color> setting) {
        super(setting);
    }

    @Override
    public void action(GuiEvent event) {
        getParent().setOverlay(new GuiSelectColor(GuiSettingColor.this, getValue()));
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        Rectangle rect = getBounds();
        mc.getTextureManager().bindTexture(TRANSPARENCY);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, rect.width, rect.height, 6, 6);
        Gui.drawRect(0, 0, rect.width, rect.height, getValue().getColor());
        Gui.drawRect(0, 0, rect.width, 1, 0xffaabbcc);
        Gui.drawRect(1, 0, 0, rect.height, 0xffaabbcc);
        Gui.drawRect(rect.width - 1, 0, rect.width, rect.height, 0xffaabbcc);
        Gui.drawRect(0, rect.height, rect.width, rect.height - 1, 0xffaabbcc);
    }

    @Override
    public void apply(Color input) {
        setValue(input);
        getParent().setOverlay(null);
    }

    @Override
    public Color getValue() {
        return getSetting().getValue();
    }

    @Override
    public void setValue(Color color) {
        getSetting().setValue(color);
    }

}
