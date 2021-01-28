package me.spaceman.psilocin.module.world;

import me.spaceman.psilocin.module.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Fullbright extends Module {

    private float oldGamma = 1F;
    // EntityRenderer#setupFog -> 1958
    public Fullbright() {
        super("Fullbright", Keyboard.KEY_F, Category.WORLD, 0xFF20B1B2);
    }

    @Override
    public void onEnable() {
        this.oldGamma = Minecraft.getMinecraft().gameSettings.saturation;
        Minecraft.getMinecraft().gameSettings.saturation = 1000F;
    }

    @Override
    public void onDisable() {
        Minecraft.getMinecraft().gameSettings.saturation = this.oldGamma;
    }
}
