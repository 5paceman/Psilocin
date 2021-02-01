package me.spaceman.psilocin.gui.components;

import me.spaceman.psilocin.utils.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import org.lwjgl.Sys;
import org.lwjgl.util.Rectangle;

import java.util.ArrayList;

public class GUIPanel implements IGUIElement{

    private String panelName;
    private GUIBounds bounds;
    private boolean isOpen, isDragging;
    private GUIBounds openButton;
    private GUIBounds topBar;
    private int dragX, dragY;


    private ArrayList<IGUIElement> panelElements = new ArrayList<>();

    public GUIPanel(String panelName, int x, int y, int width, int height)
    {
        this.panelName = panelName;
        this.bounds = new GUIBounds(x, y, width, height);
        this.isOpen = true;
        this.openButton = new GUIBounds(this, 10, 10, bounds.getWidth() - 14, 4);
        this.topBar = new GUIBounds(this, getBounds().getWidth(), 18);
    }

    @Override
    public void draw(FontRenderer fontRenderer, int mouseX, int mouseY, float partialTicks) {
        if(this.isDragging)
        {
            this.bounds.setX(mouseX + dragX);
            this.bounds.setY(mouseY + dragY);
        }

        RenderUtils.drawRect(topBar.getX(), topBar.getY(), topBar.getX() + topBar.getWidth(), topBar.getY() + topBar.getHeight(), 0x557e7e7e);
        RenderUtils.drawRect(topBar.getX() + 1, topBar.getY() + 1, topBar.getX() + topBar.getWidth() - 1, topBar.getY() + topBar.getHeight() - 1, 0xAA000000);
        fontRenderer.drawStringWithShadow(this.panelName, bounds.getX() + 4, bounds.getY() + (18 /2) - (fontRenderer.FONT_HEIGHT / 2), 0xFFFFFFFF);
        RenderUtils.drawRect(openButton.getX(), openButton.getY(), openButton.getX() + openButton.getWidth(),openButton.getY()+ openButton.getHeight(), this.isOpen ? 0xCC0091b8 :  0xCC7e7e7e);
        if(this.isOpen) {
            RenderUtils.drawRect(bounds.getX() + 2, bounds.getY() + topBar.getHeight(), bounds.getX() + bounds.getWidth() - 2, bounds.getY() + topBar.getHeight() + bounds.getHeight(), 0x557e7e7e);
            RenderUtils.drawRect(bounds.getX() + 3, bounds.getY() + topBar.getHeight() + 1, bounds.getX() + bounds.getWidth() - 3, bounds.getY() + topBar.getHeight() + bounds.getHeight() - 1, 0xAA000000);
            for(IGUIElement elements : this.panelElements)
            {
                elements.draw(fontRenderer, mouseX, mouseY, partialTicks);
            }
        }
    }

    public void addElement(IGUIElement element)
    {
        this.panelElements.add(element);
        resize();
    }

    public void resize()
    {
        int height = 0;
        int width = bounds.getWidth();
        for(IGUIElement element : this.panelElements)
        {
            element.getBounds().setYOffset(topBar.getHeight() + 3 + height);
            element.getBounds().setXOffset((bounds.getWidth() - element.getBounds().getWidth()) / 2);
            height += element.getBounds().getHeight() + 3;
            if(element.getBounds().getWidth() > width)
            {
                width = element.getBounds().getWidth();
            }
        }

        this.bounds.setWidth(width);
        this.bounds.setHeight(height + 3);
    }

    @Override
    public void keyType(char key, int keyCode) {
        for(IGUIElement elements : this.panelElements)
        {
            elements.keyType(key, keyCode);
        }
    }

    @Override
    public void update() {
        for(IGUIElement elements : this.panelElements)
        {
            elements.update();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(openButton.contains(mouseX, mouseY)){
            this.isOpen = !isOpen;
        }else if(mouseButton == 0 && topBar.contains(mouseX, mouseY))
        {
            this.dragX = this.getBounds().getX() - mouseX;
            this.dragY = this.getBounds().getY() - mouseY;
            this.isDragging = true;
        }
        if(this.isOpen) {
            for (IGUIElement element : this.panelElements) {
                element.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0)
            this.isDragging = false;

        for(IGUIElement elements : this.panelElements)
        {
            elements.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClicked) {
        for(IGUIElement elements : this.panelElements)
        {
            elements.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClicked);
        }
    }

    @Override
    public GUIBounds getBounds() {
        return this.bounds;
    }
}
