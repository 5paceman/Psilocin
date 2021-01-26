package me.spaceman.psilocin.module.render;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.RenderWorldEvent;
import me.spaceman.psilocin.module.Module;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.security.Key;

import static net.minecraft.util.MathHelper.sin;

public class Trajectory extends Module {

    private Minecraft mc;

    public Trajectory() {
        super("Trajectory", Keyboard.KEY_T, Category.RENDER, 0xFF922B21);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
        mc = Minecraft.getMinecraft();
    }

    @EventSubscriber
    public void onRenderWorld(final RenderWorldEvent event)
    {
        if(Mouse.isButtonDown(1))
        {
            if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)
            {
                double arrowX = mc.thePlayer.posX;
                double arrowY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
                double arrowZ = mc.thePlayer.posZ;
                double motionX = getBowStrength() * Math.cos(mc.thePlayer.rotationPitch) * sin(mc.thePlayer.rotationYaw);
                double motionZ = getBowStrength() * Math.cos(mc.thePlayer.rotationPitch) * Math.cos(mc.thePlayer.rotationYaw);
                double motionY = getBowStrength() * sin(mc.thePlayer.rotationPitch);

                for(double i = 0; i < 1000D; i += 0.5D)
                {
                    motionY -= 0.01 * i;
                    arrowX += motionX * i;
                    arrowZ += motionZ * i;
                    arrowY += motionY * i;

                    if(mc.theWorld.getBlockState(new BlockPos(arrowX, arrowY, arrowZ)).getBlock().isCollidable())
                    {
                        
                    }
                }
            }
        }
    }

    private double getBowStrength()
    {
        int useTime = mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration() - mc.thePlayer.getItemInUseCount();
        float bowStrength = (float)useTime / 20.0F;
        bowStrength = (bowStrength * bowStrength + bowStrength * 2.0F) / 3.0F;

        if ((double)bowStrength < 0.1D)
        {
            return 0D;
        }

        if (bowStrength > 1.0F)
        {
            bowStrength = 1.0F;
        }

        return bowStrength;
    }
}
