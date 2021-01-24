package me.spaceman.psilocin.eventsystem.events;

public class KeyPressEvent extends Event{

    private int keyCode;
    private boolean keyState;

    public KeyPressEvent(int keycode, boolean keystate) {
        super(KEY_PRESS_EVENT);
        this.keyCode = keycode;
        this.keyState = keystate;
    }

    public int getKeyCode()
    {
        return this.keyCode;
    }

    public boolean getKeyState()
    {
        return this.keyState;
    }
}
