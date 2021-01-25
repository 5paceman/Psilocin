package me.spaceman.psilocin.module.world;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.PlayerLivingUpdateEvent;
import me.spaceman.psilocin.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import org.lwjgl.input.Keyboard;

public class FireballAura extends Module {
    public FireballAura() {
        super("FireballAura", Keyboard.KEY_U, Category.WORLD, 0xFF21B3E4);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
    }

    @EventSubscriber
    public void onPlayerUpdate(final PlayerLivingUpdateEvent event)
    {
        if(this.isEnabled())
        {
            for(Entity entity : event.getPlayer().worldObj.loadedEntityList)
            {
                if(entity instanceof EntityFireball)
                {
                    EntityFireball fireball = (EntityFireball) entity;
                    if(fireball.canAttackWithItem() && Minecraft.getMinecraft().playerController.getCurrentGameType() == WorldSettings.GameType.SURVIVAL)
                    {
                        if(event.getPlayer().canEntityBeSeen(fireball)) {
                            this.lookAt(fireball, event.getPlayer());
                            event.getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPlayer().posX, event.getPlayer().getEntityBoundingBox().minY, event.getPlayer().posZ, event.getPlayer().rotationYaw, event.getPlayer().rotationPitch, event.getPlayer().onGround));
                            event.getPlayer().attackTargetEntityWithCurrentItem(fireball);
                        }
                    }
                }
            }
        }
    }

    private void lookAt(EntityFireball entity, EntityPlayerSP thePlayer)
    {
        double d0 = entity.posX - thePlayer.posX;
        double d1 = entity.posY - (thePlayer.posY + (double)thePlayer.getEyeHeight());
        double d2 = entity.posZ - thePlayer.posZ;
        double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float)(-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI));
        thePlayer.rotationPitch = this.updateRotation(thePlayer.rotationPitch, f1, 30F);
        thePlayer.rotationYawHead = this.updateRotation(thePlayer.rotationYawHead, f, 30F);
    }

    private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_)
    {
        float f = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);

        if (f > p_75652_3_)
        {
            f = p_75652_3_;
        }

        if (f < -p_75652_3_)
        {
            f = -p_75652_3_;
        }

        return p_75652_1_ + f;
    }
}
