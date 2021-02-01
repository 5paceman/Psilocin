package me.spaceman.psilocin.gui.components;

import me.spaceman.psilocin.utils.RenderUtils;
import net.minecraft.client.gui.FontRenderer;

import java.util.function.BiConsumer;

public class GUIButton implements IGUIElement {

    private BiConsumer<Integer, Integer> onClick;
    protected String label;
    protected GUIBounds bounds;
    protected final static int GRADIENT_ONE = 0xFF414345;
    protected final static int GRADIENT_TWO = 0xFF232526;

    public GUIButton(String label, IGUIElement parent, BiConsumer<Integer, Integer> onClick)
    {
        this.label = label;
        this.onClick = onClick;
        this.bounds = new GUIBounds(parent, 90, 15);
    }

    @Override
    public void draw(FontRenderer fontRenderer, int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawRect(this.getBounds().getX(), this.getBounds().getY(), this.getBounds().getX() + this.getBounds().getWidth(), this.getBounds().getY() + this.getBounds().getHeight(), 0x557e7e7e);
        RenderUtils.drawRect(this.getBounds().getX() + 1, this.getBounds().getY() + 1, this.getBounds().getX() + this.getBounds().getWidth() - 1, this.getBounds().getY() + this.getBounds().getHeight() - 1, this.bounds.contains(mouseX, mouseY) ? 0xAA222222 : 0xAA000000);
        fontRenderer.drawStringWithShadow(this.label, this.getBounds().getX() + (this.getBounds().getWidth() / 2) - (fontRenderer.getStringWidth(this.label) / 2), this.getBounds().getY() + (this.getBounds().getHeight() / 2) - (fontRenderer.FONT_HEIGHT / 2) + 1, 0xFFFFFFFF);
    }

    @Override
    public void keyType(char key, int keyCode) {

    }

    @Override
    public void update() {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0 && this.getBounds().contains(mouseX, mouseY))
        {
            this.onClick.accept(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClicked) {

    }

    @Override
    public GUIBounds getBounds() {
        return this.bounds;
    }
}
