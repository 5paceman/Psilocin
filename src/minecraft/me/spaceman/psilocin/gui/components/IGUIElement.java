package me.spaceman.psilocin.gui.components;

import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.util.Rectangle;

public interface IGUIElement {

    void draw(FontRenderer fontRenderer, int mouseX, int mouseY, float partialTicks);
    void keyType(char key, int keyCode);
    void update();
    void mouseClicked(int mouseX, int mouseY, int mouseButton);
    void mouseReleased(int mouseX, int mouseY, int mouseButton);
    void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClicked);
    GUIBounds getBounds();
}
