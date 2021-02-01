package me.spaceman.psilocin.gui.components;

import org.lwjgl.util.Rectangle;

public class GUIBounds {

    private IGUIElement parent;
    private int x, y, width, height;
    private int xOffset, yOffset;

    public GUIBounds(IGUIElement parent, int width, int height, int xOffset, int yOffset)
    {
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public GUIBounds(IGUIElement parent, int width, int height)
    {
        this.parent = parent;
        this.width = width;
        this.height = height;
    }

    public GUIBounds(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX()
    {
        if(parent != null)
        {
            return this.parent.getBounds().getX() + this.xOffset;
        } else {
            return this.x;
        }
    }

    public int getY()
    {
        if(parent != null)
        {
            return this.parent.getBounds().getY() + this.yOffset;
        } else {
            return this.y;
        }
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean contains(int x, int y)
    {
        Rectangle bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        return bounds.contains(x, y);
    }

}
