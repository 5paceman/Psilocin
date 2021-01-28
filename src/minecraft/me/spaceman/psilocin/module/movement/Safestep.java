package me.spaceman.psilocin.module.movement;

import me.spaceman.psilocin.module.Module;
import org.lwjgl.input.Keyboard;

public class Safestep extends Module {

    // Entity#moveEntity -> 629
    public Safestep()
    {
        super("Safestep", Keyboard.KEY_L, Category.MOVEMENT, 0xFF00FF06);
    }

}
