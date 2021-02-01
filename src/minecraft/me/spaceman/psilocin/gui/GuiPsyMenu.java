package me.spaceman.psilocin.gui;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.gui.components.GUIButton;
import me.spaceman.psilocin.gui.components.GUIPanel;
import me.spaceman.psilocin.gui.components.IGUIElement;
import me.spaceman.psilocin.gui.components.ModuleButton;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.module.world.Fullbright;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class GuiPsyMenu extends GuiScreen {

    private ArrayList<IGUIElement> guiElements = new ArrayList<>();

    public GuiPsyMenu() {
        GUIPanel worldPanel = new GUIPanel("World", 50, 50, 100, 22);
        GUIPanel renderPanel = new GUIPanel("Render", 200, 50, 100, 22);
        GUIPanel movementPanel = new GUIPanel("Movement", 50, 150, 100, 22);
        GUIPanel minigamePanel = new GUIPanel("Minigames", 200, 150, 100, 22);

        for(Module mod : Psilocin.getInstance().getModuleHandler().getLoadedModules())
        {
            switch(mod.getCategory())
            {
                case WORLD:
                case MISC:
                    worldPanel.addElement(new ModuleButton(worldPanel, mod));
                    break;
                case RENDER:
                    renderPanel.addElement(new ModuleButton(renderPanel, mod));
                    break;
                case MOVEMENT:
                    movementPanel.addElement(new ModuleButton(movementPanel, mod));
                    break;
                case MINIGAME:
                    minigamePanel.addElement(new ModuleButton(minigamePanel, mod));
                    break;
            }
        }

        guiElements.add(worldPanel);
        guiElements.add(renderPanel);
        guiElements.add(movementPanel);
        guiElements.add(minigamePanel);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for(IGUIElement element : this.guiElements)
        {
            element.draw(this.fontRendererObj, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for(IGUIElement element : this.guiElements)
        {
            element.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for(IGUIElement element : this.guiElements)
        {
            element.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for(IGUIElement element : this.guiElements)
        {
            element.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        for(IGUIElement element : this.guiElements)
        {
            element.update();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
