package me.spaceman.psilocin.module.minigame;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.PlayerLivingUpdateEvent;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import org.lwjgl.input.Keyboard;

public class FireballAura extends Module {

    private TimeHelper timeHelper = new TimeHelper();
    private long delay = 170;

    public FireballAura() {
        super("FireballAura", Keyboard.KEY_U, Category.MINIGAME, 0xFFF23005);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
        try {
            timeHelper.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    if(fireball.shootingEntity != event.getPlayer() && fireball.canAttackWithItem() && Minecraft.getMinecraft().playerController.getCurrentGameType() == WorldSettings.GameType.SURVIVAL)
                    {
                        double distance = fireball.getDistanceToEntity(event.getPlayer());
                        if(event.getPlayer().canEntityBeSeen(fireball) && distance <= 5 && timeHelper.getElapsedTimeInMilliseconds() >= delay) {
                            this.lookAt(fireball, event.getPlayer());
                            Minecraft.getMinecraft().playerController.attackEntity(event.getPlayer(), entity);
                            event.getPlayer().swingItem();

                            try {
                                timeHelper.clear();
                                timeHelper.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private void lookAt(EntityFireball entity, EntityPlayerSP thePlayer)
    {
        double xDiff = entity.posX - thePlayer.posX;
        double yDiff = entity.posY - (thePlayer.posY + (double)thePlayer.getEyeHeight());
        double zDiff = entity.posZ - thePlayer.posZ;
        double xzSqrtDiff = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float newYaw = (float)(MathHelper.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
        float newPitch = (float)(-(MathHelper.atan2(yDiff, xzSqrtDiff) * 180.0D / Math.PI));
        float rotationPitch = thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(newPitch - thePlayer.rotationPitch);
        float rotationYaw = thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(newYaw - thePlayer.rotationYaw);

        float f2 = MathHelper.wrapAngleTo180_float(thePlayer.rotationYaw - thePlayer.renderYawOffset);

        if (f2 < -75.0F)
        {
            rotationYaw = thePlayer.renderYawOffset - 75.0F;
        }

        if (f2 > 75.0F)
        {
            rotationYaw = thePlayer.renderYawOffset + 75.0F;
        }

        thePlayer.setLocationAndAngles(thePlayer.posX, thePlayer.posY, thePlayer.posZ, rotationYaw, rotationPitch);
    }
}
