package me.spaceman.psilocin.gui.components;

import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.utils.RenderUtils;
import net.minecraft.client.gui.FontRenderer;

import java.util.function.BiConsumer;

public class ModuleButton extends GUIButton{

    private Module mod;
    private final static int ENABLED_COLOR_ONE = 0xFF928DAB;
    private final static int ENABLED_COLOR_TWO = 0xFF00d2ff;

    public ModuleButton(IGUIElement parent, Module mod) {
        super(mod.getName(), parent, null);
        this.mod = mod;
    }

    @Override
    public void draw(FontRenderer fontRenderer, int mouseX, int mouseY, float partialTicks) {
        if(this.mod.isEnabled()) {
            RenderUtils.drawRect(this.getBounds().getX(), this.getBounds().getY(), this.getBounds().getX() + this.getBounds().getWidth(), this.getBounds().getY() + this.getBounds().getHeight(), 0x557e7e7e);
            RenderUtils.drawRect(this.getBounds().getX() + 1, this.getBounds().getY() + 1, this.getBounds().getX() + this.getBounds().getWidth() - 1, this.getBounds().getY() + this.getBounds().getHeight() - 1, getBounds().contains(mouseX, mouseY) ? 0xEE0091b8 : 0xCC0091b8);
        }else {
            RenderUtils.drawRect(this.getBounds().getX(), this.getBounds().getY(), this.getBounds().getX() + this.getBounds().getWidth(), this.getBounds().getY() + this.getBounds().getHeight(), 0x557e7e7e);
            RenderUtils.drawRect(this.getBounds().getX() + 1, this.getBounds().getY() + 1, this.getBounds().getX() + this.getBounds().getWidth() - 1, this.getBounds().getY() + this.getBounds().getHeight() - 1, getBounds().contains(mouseX, mouseY) ? 0xAA222222 : 0xAA000000);
        }

        fontRenderer.drawStringWithShadow(this.label, this.getBounds().getX() + (this.getBounds().getWidth() / 2) - (fontRenderer.getStringWidth(this.label) / 2), this.getBounds().getY() + (this.getBounds().getHeight() / 2) - (fontRenderer.FONT_HEIGHT / 2) + 1, 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(this.getBounds().contains(mouseX, mouseY) && mouseButton == 0)
        {
            this.mod.toggle();
        }
    }
}
