package me.spaceman.psilocin.module.render;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.RenderNametagsEvent;
import me.spaceman.psilocin.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class Nametags extends Module {

    private RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
    private DecimalFormat decimalFormat = new DecimalFormat("#.#");
    public Nametags() {
        super("Nametags", Keyboard.KEY_M, Category.RENDER, 0xFF2233BB);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
    }

    @EventSubscriber
    public void onRenderNametags(final RenderNametagsEvent event)
    {
        if(!this.isEnabled() || !(event.getEntity() instanceof EntityPlayer))
            return;
        if(event.getEntity().getAlwaysRenderNameTagForRender()) {
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            event.setCancelled(true);
            double distance = event.getEntity().getDistance(this.renderManager.livingPlayer.posX, this.renderManager.livingPlayer.posY, this.renderManager.livingPlayer.posZ);
            double distanceMod = (2.5f / 30f) * distance;
            float scaleFactor = (float) Math.max(1f, Math.min(2.5f, distanceMod));
            double x = event.getX();
            double y = event.getY() + 0.25;
            double z = event.getZ();

            String s = event.getEntity().getDisplayName().getFormattedText() + " H:" + decimalFormat.format(event.getEntity().getHealth());
            float f1 = 0.02666667F;
            GlStateManager.alphaFunc(516, 0.1F);

            FontRenderer fontrenderer = renderManager.getFontRenderer();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + event.getEntity().height + 0.5F - (event.getEntity().isChild() ? event.getEntity().height / 2.0F : 0.0F), (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
            GlStateManager.translate(0.0F, 9.374999F, 0.0F);
            GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            int i = fontrenderer.getStringWidth(s) / 2;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double) (-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GL11.glDepthMask(true);
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 0xFFFFFFFF);
            GlStateManager.translate(-i - 7, 4, 0.0D);
            GlStateManager.scale(-10f, -10f, 10f);
            renderItem.renderItem(((EntityPlayer) event.getEntity()).inventory.getCurrentItem(), ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}
