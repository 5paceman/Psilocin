package me.spaceman.psilocin.module.render;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.RenderWorldEvent;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFireball;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;

public class Trajectory extends Module {

    private Minecraft mc;
    private Random rand = new Random();

    public Trajectory() {
        super("Trajectory", Keyboard.KEY_T, Category.RENDER, 0xFF00C4FF);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
        mc = Minecraft.getMinecraft();
    }

    // See the event class declaration to see where this event gets called from
    @EventSubscriber
    public void onRenderWorld(final RenderWorldEvent event)
    {
        if(!this.isEnabled())
            return;

        // Make sure we're holding an item
        if(mc.thePlayer.getHeldItem() != null)
        {
            Item heldItem = mc.thePlayer.getHeldItem().getItem();
            // Make sure the item we're holding is an item we want to predict for (ItemFireball is for Bed Wars)
            if(heldItem instanceof ItemEnderPearl || heldItem instanceof ItemBow || heldItem instanceof ItemFireball)
            {
                // In the case of the bow if we're not pulling the string back we dont care to render the projection
                if(heldItem instanceof ItemBow && mc.thePlayer.getItemInUse() == null)
                    return;

                // This is so our rendering doesnt end up bobbing all over the screen if view bobbing is turned on
                RenderUtils.preRemoveViewBobbing(event.getPartialTicks());

                // Intial position of the arrow moved slightly so it lines up with the bow
                double projX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * event.getPartialTicks() - Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.08F;
                double projY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * event.getPartialTicks() + mc.thePlayer.getEyeHeight() - 0.04;
                double projZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * event.getPartialTicks() - Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.08F;
                // The generic speed of the arrow (Will change if its an enderpearl etc)
                float speed = (heldItem instanceof ItemBow) ? 1F : (heldItem instanceof ItemEnderPearl) ? 0.4F : 1F;
                // Convert pitch and yaw to radians
                float yaw = (float)Math.toRadians(mc.thePlayer.rotationYaw);
                float pitch = (float)Math.toRadians(mc.thePlayer.rotationPitch);
                // Basic Trajectory motion (Same calculation as on StackOverflow)
                double motionX = -Math.sin(yaw) * Math.cos(pitch) * speed;
                double motionY = -Math.sin(pitch) * speed;
                double motionZ = Math.cos(yaw) *  Math.cos(pitch) * speed;
                double motionFactor = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                //  setThrowableMotion for EntityThrowable and EntityArrow
                motionX /= motionFactor;
                motionY /= motionFactor;
                motionZ /= motionFactor;
                // Same math as used in ItemBow#onPlayerStoppedUsing for calculating bow power
                if(heldItem instanceof ItemBow) {
                    float bowPower = getBowStrength();
                    motionX *= bowPower;
                    motionY *= bowPower;
                    motionZ *= bowPower;
                } else if(heldItem instanceof ItemEnderPearl)
                {
                    // See EntityThrowable getVelocity()
                    motionX *= 1.5D;
                    motionY *= 1.5D;
                    motionZ *= 1.5D;
                }

                // Disable lighting so no lighting gets applied to our lines
                GL11.glDisable(GL11.GL_LIGHTING);
                GlStateManager.disableLighting();
                // Disable texturing
                GlStateManager.disableTexture2D();
                GlStateManager.pushMatrix();
                GL11.glLineWidth(2f);
                // Tesselator and world renderer are Minecraft classes used for rendering
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                // GL_LINE_STRIP lets us plot points for a continous line GL_LINE would plot a line for every two points
                // DefaultVertexFormats.POSITION_COLOR just sets it so each vertex has a position and color vertex
                worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
                // Loop to simulate time of the arrow flying
                boolean hit = false;
                for(double i = 0; i < 1000D; i++)
                {
                    // Plot our point for rendering renderPosX/Y/Z are Minecraft render engine offsets so stuff renders where we need it
                    worldRenderer.pos(projX - mc.getRenderManager().renderPosX, projY - mc.getRenderManager().renderPosY, projZ - mc.getRenderManager().renderPosZ).color(1f, 1f, 1f, 1f).endVertex();
                    MovingObjectPosition rayTrace = mc.theWorld.rayTraceBlocks(new Vec3(projX, projY, projZ), new Vec3(projX + (motionX * 0.1), projY + (motionY * 0.1), projZ + (motionZ * 0.1)), false, true, false);
                    if(rayTrace != null)
                    {
                        hit = true;
                        tessellator.draw();
                        switch(rayTrace.typeOfHit)
                        {
                            case ENTITY:
                                break;
                            case BLOCK:
                                double x = rayTrace.getBlockPos().getX() - mc.getRenderManager().renderPosX;
                                double y = rayTrace.getBlockPos().getY() - mc.getRenderManager().renderPosY;
                                double z = rayTrace.getBlockPos().getZ() - mc.getRenderManager().renderPosZ;
                                GL11.glDisable(GL11.GL_DEPTH_TEST);
                                GlStateManager.disableDepth();
                                RenderUtils.drawBox(x, y, z, 1, 1, 0.8f, 1f, 0.8f, 0.25f);
                                GlStateManager.enableTexture2D();
                                GL11.glEnable(GL11.GL_TEXTURE_2D);
                                break;
                        }
                        break;
                    }

                    for(Entity entity : mc.theWorld.loadedEntityList)
                    {
                        if(entity == mc.getRenderViewEntity())
                            continue;
                        if(entity instanceof EntityLivingBase && entity.getEntityBoundingBox().isVecInside(new Vec3(projX, projY, projZ)))
                        {

                            hit = true;
                            tessellator.draw();
                            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().renderPosX;
                            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY;
                            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().renderPosZ;
                            GL11.glPushMatrix();
                            GL11.glTranslated(x, y, z);
                            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * event.getPartialTicks() - 90.0F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.disableDepth();
                            RenderUtils.drawBox(0  - (entity.width * 1.5f) / 2, 0, 0 - (entity.width * 1.5f) / 2, (entity.width * 1.5f), entity.height, 0.2f, 1f, 0.2f, 0.25f);
                            GlStateManager.enableTexture2D();
                            GlStateManager.disableDepth();
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glPopMatrix();
                            break;
                        }
                    }
                    if(!hit) {
                        for (Entity entity : mc.theWorld.playerEntities) {
                            if (entity == mc.getRenderViewEntity())
                                continue;
                            if (entity.getEntityBoundingBox().isVecInside(new Vec3(projX, projY, projZ))) {
                                hit = true;
                                tessellator.draw();
                                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().renderPosX;
                                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY;
                                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().renderPosZ;
                                GL11.glPushMatrix();
                                GL11.glTranslated(x, y, z);
                                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * event.getPartialTicks() - 90.0F, 0.0F, 1.0F, 0.0F);
                                GL11.glDisable(GL11.GL_DEPTH_TEST);
                                GlStateManager.disableDepth();
                                RenderUtils.drawBox(0 - entity.width / 2, 0, 0 - entity.width / 2, entity.width, entity.height, 0.2f, 1f, 0f, 0.25f);
                                GlStateManager.enableTexture2D();
                                GL11.glEnable(GL11.GL_TEXTURE_2D);
                                GL11.glEnable(GL11.GL_DEPTH_TEST);
                                GL11.glPopMatrix();
                                break;
                            }
                        }
                    }
                    if(hit)
                        break;
                    // Slowly increment our simulated arrows coords using our motion values
                    projX += motionX * 0.1;
                    projZ += motionZ * 0.1;
                    projY += motionY * 0.1;
                    // Slowly decrement our motions so they fall off over time

                    // Decrement Y motion by gravity
                    if(heldItem instanceof ItemBow) {
                        motionX *= 0.999;
                        motionY *= 0.999;
                        motionZ *= 0.999;
                        motionY -= 0.005F;
                    }else if(heldItem instanceof ItemEnderPearl)
                    {
                        motionX *= 0.999;
                        motionY *= 0.999;
                        motionZ *= 0.999;
                        motionY -= 0.003F;
                    } else if(heldItem instanceof ItemFireball)
                    {
                        motionY -= 0f;
                    }


                }
                // After the loop we draw our line
                if(!hit)
                    tessellator.draw();

                GlStateManager.popMatrix();
                // Re-enable depth and texture 2D. If we re-enable lighting we'll end up with stuff in the
                // GUI like the hotbar having lighting when it shouldnt
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();

                // This is so our rendering doesnt end up bobbing all over the screen if view bobbing is turned on
                RenderUtils.postRemoveViewBobbing();
            }
        }
    }

    private float getBowStrength()
    {
        float bowPower = (mc.thePlayer.getItemInUse().getMaxItemUseDuration() - mc.thePlayer.getItemInUseCount()) / 20f;
        bowPower = (bowPower * bowPower + bowPower * 2f) / 3f;

        if (bowPower > 1f || bowPower <= 0.1f)
            bowPower = 1f;

        bowPower *= 3f;

        return bowPower;
    }
}
