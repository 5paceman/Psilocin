package me.spaceman.psilocin.module.movement;

import me.spaceman.psilocin.module.Module;
import org.lwjgl.input.Keyboard;

public class Safestep extends Module {

    public Safestep()
    {
        super("Safestep", Keyboard.KEY_L, Category.MOVEMENT, 0xFF69D6A9);
    }

}
