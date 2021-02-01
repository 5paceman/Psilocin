package me.spaceman.psilocin.module;

import me.spaceman.psilocin.Psilocin;

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
        Integer key = Psilocin.getInstance().getConfigHandler().<Integer>getValue(this.getName() + ".keybind");
        if(key != null)
            this.keyCode = key;
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

    public void setKeybind(int keyCode)
    {
        this.keyCode = keyCode;
        Psilocin.getInstance().getConfigHandler().putValue(this.getName() + ".keybind", this.getKeyCode());
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
