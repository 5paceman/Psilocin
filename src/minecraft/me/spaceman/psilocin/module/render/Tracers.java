package me.spaceman.psilocin.module.render;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.RenderWorldEvent;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;

public class Tracers extends Module{

    public Tracers()
    {
        super("Tracers", Keyboard.KEY_Y, Category.RENDER, 0xFF6D9F9F);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
    }

    @EventSubscriber
    public void onRenderWorld(final RenderWorldEvent event) {
        if (!this.isEnabled())
            return;

        RenderUtils.preRemoveViewBobbing(event.getPartialTicks());


        Entity thePlayer = Minecraft.getMinecraft().getRenderViewEntity();
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        double posX = thePlayer.lastTickPosX + (thePlayer.posX - thePlayer.lastTickPosX) * (double) event.getPartialTicks() - renderManager.renderPosX;
        double posY = thePlayer.lastTickPosY + (thePlayer.posY - thePlayer.lastTickPosY) * (double) event.getPartialTicks() - renderManager.renderPosY;
        double posZ = thePlayer.lastTickPosZ + (thePlayer.posZ - thePlayer.lastTickPosZ) * (double) event.getPartialTicks() - renderManager.renderPosZ;

        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
            if(player == Minecraft.getMinecraft().thePlayer)
                continue;

            boolean isFriend = Psilocin.getInstance().getFriendHandler().isFriend(player);
            double playerPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.getPartialTicks() - renderManager.renderPosX;
            double playerPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks() - renderManager.renderPosY;
            double playerPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks() - renderManager.renderPosZ;

            GL11.glDisable(GL11.GL_LIGHTING);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.pushMatrix();
            GL11.glLineWidth(2f);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            worldRenderer.pos(posX, posY + thePlayer.getEyeHeight(), posZ).color(0f, isFriend ? 0f : 1f, isFriend ? 1f : 0f, 1f).endVertex();
            worldRenderer.pos(playerPosX, playerPosY, playerPosZ).color(0f, isFriend ? 0f :  1f, isFriend ? 1f : 0f, 1f).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
        }

        RenderUtils.postRemoveViewBobbing();
    }

}
