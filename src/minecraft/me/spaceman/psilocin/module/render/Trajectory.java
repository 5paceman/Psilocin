package me.spaceman.psilocin.module.render;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.RenderWorldEvent;
import me.spaceman.psilocin.module.Module;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemBow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;

public class Trajectory extends Module {

    private Minecraft mc;
    private Random rand = new Random();

    public Trajectory() {
        super("Trajectory", Keyboard.KEY_T, Category.RENDER, 0xFF922B21);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
        mc = Minecraft.getMinecraft();
    }

    // See the event class declaration to see where this event gets called from
    @EventSubscriber
    public void onRenderWorld(final RenderWorldEvent event)
    {
        if(Mouse.isButtonDown(1) && mc.thePlayer.getItemInUse() != null)
        {
            if(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)
            {
                // This is so our rendering doesnt end up bobbing all over the screen if view bobbing is turned on
                if (Minecraft.getMinecraft().gameSettings.viewBobbing) {
                    GL11.glPushAttrib(GL_VIEWPORT_BIT);
                    GL11.glLoadIdentity();
                    Minecraft.getMinecraft().gameSettings.viewBobbing = false;
                    event.getEntityRenderer().setupCameraTransform(event.getPartialTicks(), 0);
                    Minecraft.getMinecraft().gameSettings.viewBobbing = true;
                }

                // Intial position of the arrow moved slightly so it lines up with the bow
                double arrowX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * event.getPartialTicks() - Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.08F;
                double arrowY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * event.getPartialTicks() + mc.thePlayer.getEyeHeight() - 0.04;
                double arrowZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * event.getPartialTicks() - Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.08F;
                // The generic speed of the arrow (Will change if its an enderpearl etc)
                float speed = 1F;
                // Convert pitch and yaw to radians
                float yaw = (float)Math.toRadians(mc.thePlayer.rotationYaw);
                float pitch = (float)Math.toRadians(mc.thePlayer.rotationPitch);
                // Basic Trajectory motion (Same calculation as on StackOverflow)
                double motionX = -Math.sin(yaw) * Math.cos(pitch) * speed;
                double motionY = -Math.sin(pitch) * speed;
                double motionZ = Math.cos(yaw) *  Math.cos(pitch) * speed;
                // Same math as used in ItemBow#onPlayerStoppedUsing for calculating bow power
                float bowPower = (mc.thePlayer.getItemInUse().getMaxItemUseDuration() - mc.thePlayer.getItemInUseCount()) / 20f;
                bowPower = (bowPower * bowPower + bowPower * 2f) / 3f;

                if (bowPower > 1f || bowPower <= 0.1f)
                    bowPower = 1f;

                bowPower *= 3f;
                motionX *= bowPower;
                motionY *= bowPower;
                motionZ *= bowPower;

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
                for(double i = 0; i < 1000D; i++)
                {
                    // Plot our point for rendering renderPosX/Y/Z are Minecraft render engine offsets so stuff renders where we need it
                    worldRenderer.pos(arrowX - mc.getRenderManager().renderPosX, arrowY - mc.getRenderManager().renderPosY, arrowZ - mc.getRenderManager().renderPosZ).color(1f, 1f, 1f, 1f).endVertex();
                    // Slowly increment our simulated arrows coords using our motion values
                    arrowX += motionX * 0.1;
                    arrowZ += motionZ * 0.1;
                    arrowY += motionY * 0.1;
                    // Slowly decrement our motions so they fall off over time
                    motionX *= 0.999;
                    motionY *= 0.999;
                    motionZ *= 0.999;
                    // Decrement Y motion by gravity
                    motionY -= 0.005;


                }
                // After the loop we draw our line
                tessellator.draw();
                GlStateManager.popMatrix();
                // Re-enable depth and texture 2D. If we re-enable lighting we'll end up with stuff in the
                // GUI like the hotbar having lighting when it shouldnt
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();

                // This is so our rendering doesnt end up bobbing all over the screen if view bobbing is turned on
                if (Minecraft.getMinecraft().gameSettings.viewBobbing) {
                    GL11.glPopAttrib();
                    Minecraft.getMinecraft().gameSettings.viewBobbing = true;

                }
            }
        }
    }

    private double[] getHeading(double x, double y, double z)
    {
        float velocity = 1F * 1.5F;
        float inaccuracy = 1F;
        float f = MathHelper.sqrt_double(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        return new double[]{x, y ,z};
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
