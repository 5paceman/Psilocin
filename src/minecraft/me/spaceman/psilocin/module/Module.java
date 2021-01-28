package me.spaceman.psilocin.module;

public abstract class Module {

    public enum Category {
        WORLD,
        RENDER,
        MOVEMENT,
        MISC,
        MINIGAME
    }

    private String name;
    private int keyCode;
    private Category category;
    private int color;
    private boolean isEnabled;

    protected Module(String name, int keycode, Category category, int color)
    {
        this.name = name;
        this.keyCode = keycode;
        this.category = category;
        this.color = color;
    }

    public void toggle()
    {
        this.isEnabled = !this.isEnabled;

        if(this.isEnabled)
            this.onEnable();
        else
            this.onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}

    public String getName() {
        return name;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public Category getCategory() {
        return category;
    }

    public int getColor() {
        return color;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
