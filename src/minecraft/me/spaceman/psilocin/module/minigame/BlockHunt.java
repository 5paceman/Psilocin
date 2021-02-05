package me.spaceman.psilocin.module.minigame;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.ReceivePacketEvent;
import me.spaceman.psilocin.eventsystem.events.RenderWorldEvent;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockHunt extends Module {

    private CopyOnWriteArrayList<BlockPos> hiddenBlocks = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Integer> hiddenEntities = new CopyOnWriteArrayList<>();
    private ArrayList<Block> blacklistedBlocks = new ArrayList<Block>();

    public BlockHunt() {
        super("BlockHunt", Keyboard.KEY_NUMPAD1, Category.MINIGAME, 0xFF00FF00);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
        setupBlacklistedBlocks();
    }

    @Override
    public void onDisable() {
        this.hiddenEntities.clear();
        this.hiddenBlocks.clear();
    }

    @EventSubscriber
    public void onPacketReceive(final ReceivePacketEvent event)
    {
        if(!this.isEnabled())
            return;

        if(event.getPacket() instanceof S13PacketDestroyEntities)
        {
            S13PacketDestroyEntities destroyEntities = (S13PacketDestroyEntities) event.getPacket();
            for(int i =0; i < destroyEntities.getEntityIDs().length; i++)
            {
                Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(destroyEntities.getEntityIDs()[i]);
                if(this.hiddenEntities.contains(destroyEntities.getEntityIDs()[i]))
                    this.hiddenEntities.remove(destroyEntities.getEntityIDs()[i]);

                if(entity instanceof EntityFallingBlock)
                {
                    hiddenBlocks.add(new BlockPos(Math.floor(entity.posX),  Math.floor(entity.posY), Math.floor(entity.posZ)));
                }
            }
        }

        if(event.getPacket() instanceof S0FPacketSpawnMob)
        {
            S0FPacketSpawnMob spawnMob = (S0FPacketSpawnMob) event.getPacket();
            Class entityType = EntityList.getClassFromID(spawnMob.getEntityType());
            if(!hiddenEntities.contains(spawnMob.getEntityID()) && entityType != EntityPlayer.class && EntityLivingBase.class.isAssignableFrom(entityType))
                hiddenEntities.add(spawnMob.getEntityID());
        }
    }

    @EventSubscriber
    public void onRenderWorld(final RenderWorldEvent event)
    {
        if(!this.isEnabled())
            return;

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

        for(BlockPos pos : this.hiddenBlocks)
        {
            if(this.blacklistedBlocks.contains(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()))
            {
                this.hiddenBlocks.remove(pos);
                continue;
            }
            GL11.glDisable(GL11.GL_LIGHTING);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.pushMatrix();
            RenderUtils.drawBox(pos.getX() - renderManager.renderPosX, pos.getY() - renderManager.renderPosY, pos.getZ() - renderManager.renderPosZ, 1f, 1f, 0f, 1f, 0f, 0.4f);
            GlStateManager.popMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
        }

        for(Integer entityID : this.hiddenEntities)
        {
            if(Minecraft.getMinecraft().theWorld.getEntityByID(entityID) == null)
            {
                continue;
            }
            Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
            double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) event.getPartialTicks() - renderManager.renderPosX;
            double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.getPartialTicks() - renderManager.renderPosY;
            double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) event.getPartialTicks() - renderManager.renderPosZ;

            GL11.glDisable(GL11.GL_LIGHTING);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.pushMatrix();
            GL11.glTranslated(posX, posY, posZ);
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * event.getPartialTicks() - 90.0F, 0.0F, 1.0F, 0.0F);
            RenderUtils.drawBox(0, 0, 0, entity.width, entity.height, 1f, 0f, 0f, 0.4f);
            GlStateManager.popMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
        }

        Entity thePlayer = Minecraft.getMinecraft().getRenderViewEntity();
        double posX = thePlayer.lastTickPosX + (thePlayer.posX - thePlayer.lastTickPosX) * (double) event.getPartialTicks() - renderManager.renderPosX;
        double posY = thePlayer.lastTickPosY + (thePlayer.posY - thePlayer.lastTickPosY) * (double) event.getPartialTicks() - renderManager.renderPosY;
        double posZ = thePlayer.lastTickPosZ + (thePlayer.posZ - thePlayer.lastTickPosZ) * (double) event.getPartialTicks() - renderManager.renderPosZ;
        RenderUtils.preRemoveViewBobbing(event.getPartialTicks());
        for (Entity player : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if(player instanceof EntityFallingBlock) {
                double entityPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.getPartialTicks() - renderManager.renderPosX;
                double entityPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks() - renderManager.renderPosY;
                double entityPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks() - renderManager.renderPosZ;

                GL11.glDisable(GL11.GL_LIGHTING);
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.pushMatrix();
                GL11.glLineWidth(2f);
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos(posX, posY + thePlayer.getEyeHeight(), posZ).color(0f,  1f,  0f, 1f).endVertex();
                worldRenderer.pos(entityPosX, entityPosY, entityPosZ).color(0f, 1f, 0f, 1f).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
                GL11.glEnable(GL11.GL_LIGHTING);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
            }
        }
        RenderUtils.postRemoveViewBobbing();
    }

    private void setupBlacklistedBlocks()
    {
        this.blacklistedBlocks.add(Blocks.grass);
        this.blacklistedBlocks.add(Blocks.water);
        this.blacklistedBlocks.add(Blocks.cobblestone);
        this.blacklistedBlocks.add(Blocks.tallgrass);
        this.blacklistedBlocks.add(Blocks.air);
    }

}
