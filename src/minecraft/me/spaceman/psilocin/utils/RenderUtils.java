package me.spaceman.psilocin.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;

public class RenderUtils {

    public static void preRemoveViewBobbing(float partialTicks)
    {
        if (Minecraft.getMinecraft().gameSettings.viewBobbing) {
            // Store the Viewport settings
            GL11.glPushAttrib(GL_VIEWPORT_BIT);
            // Reset the view matrix
            GL11.glLoadIdentity();
            // Turn off Viewbobbing
            Minecraft.getMinecraft().gameSettings.viewBobbing = false;
            // Use Minecraft camera setup function with view bobbing turned off
            Minecraft.getMinecraft().entityRenderer.setupCameraTransform(partialTicks, 0);
            // Enable it again
            Minecraft.getMinecraft().gameSettings.viewBobbing = true;
        }
    }

    public static void postRemoveViewBobbing()
    {
        if (Minecraft.getMinecraft().gameSettings.viewBobbing) {
            // Put the stored viewport settings back to how they were
            GL11.glPopAttrib();
        }
    }

    public static void drawBox(double x, double y, double z, float w, float h, float r, float g, float b, float a)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x + w, y + h, z).endVertex();
        worldrenderer.pos(x, y + h, z).endVertex();
        worldrenderer.pos(x, y + h, z + w).endVertex();
        worldrenderer.pos(x + w, y + h, z + w).endVertex();

        worldrenderer.pos(x + w, y, z + w).endVertex();
        worldrenderer.pos(x, y, z + w).endVertex();
        worldrenderer.pos(x, y, z).endVertex();
        worldrenderer.pos(x + w, y, z).endVertex();

        worldrenderer.pos(x + w, y + h, z + w).endVertex();
        worldrenderer.pos(x, y + h, z + w).endVertex();
        worldrenderer.pos(x, y, z + w).endVertex();
        worldrenderer.pos(x + w, y,  z + w).endVertex();

        worldrenderer.pos(x + w, y, z).endVertex();
        worldrenderer.pos(x, y, z).endVertex();
        worldrenderer.pos(x, y + h, z).endVertex();
        worldrenderer.pos(x + w, y+h, z).endVertex();

        worldrenderer.pos(x, y + h, z + w).endVertex();
        worldrenderer.pos(x, y + h, z).endVertex();
        worldrenderer.pos(x, y, z).endVertex();
        worldrenderer.pos(x, y, z + w).endVertex();

        worldrenderer.pos(x + w, y + h, z).endVertex();
        worldrenderer.pos(x + w, y+h, z + w).endVertex();
        worldrenderer.pos(x + w, y,  z + w).endVertex();
        worldrenderer.pos(x + w, y, z).endVertex();
        tessellator.draw();

        GlStateManager.color(r, g, b, 0.8f);
        worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y, z).endVertex();
        worldrenderer.pos(x + w, y, z).endVertex();
        worldrenderer.pos(x + w, y, z+w).endVertex();
        worldrenderer.pos(x, y, z + w).endVertex();
        worldrenderer.pos(x, y, z).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y + h, z).endVertex();
        worldrenderer.pos(x + w, y + h, z).endVertex();
        worldrenderer.pos(x + w, y + h, z + w).endVertex();
        worldrenderer.pos(x, y + h, z + w).endVertex();
        worldrenderer.pos(x, y + h, z).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y, z).endVertex();
        worldrenderer.pos(x, y + h, z).endVertex();
        worldrenderer.pos(x + w, y, z).endVertex();
        worldrenderer.pos(x + w, y + h, z).endVertex();
        worldrenderer.pos(x + w, y, z + w).endVertex();
        worldrenderer.pos(x + w, y + h, z + w).endVertex();
        worldrenderer.pos(x, y, z + w).endVertex();
        worldrenderer.pos(x, y + h, z + w).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawHorizontalLine(int startX, int endX, int y, int color)
    {
        if (endX < startX)
        {
            int i = startX;
            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    /**
     * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
     */
    public static void drawVerticalLine(int x, int startY, int endY, int color)
    {
        if (endY < startY)
        {
            int i = startY;
            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }

    /**
     * Draws a gradient color rectangle with the specified coordinates and color (ARGB format). Args: x1, y1, x2, y2, color
     */
    public static void drawGradientRect(int left, int top, int right, int bottom, int color, int color2)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float alpha = (float)(color >> 24 & 255) / 255.0F;
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;

        float alpha2 = (float)(color2 >> 24 & 255) / 255.0F;
        float red2 = (float)(color2 >> 16 & 255) / 255.0F;
        float green2 = (float)(color2 >> 8 & 255) / 255.0F;
        float blue2 = (float)(color2 & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color (ARGB format). Args: x1, y1, x2, y2, color
     */
    public static void drawRect(int left, int top, int right, int bottom, int color)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     */
    public static void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    public static void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, (float)x, (float)y, color);
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used anywhere in vanilla code.
     */
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)vHeight) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)uWidth) * f), (double)(v * f1)).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }
}
