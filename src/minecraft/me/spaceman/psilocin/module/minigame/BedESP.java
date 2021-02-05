package me.spaceman.psilocin.module.minigame;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.BlockUpdateEvent;
import me.spaceman.psilocin.eventsystem.events.ReceivePacketEvent;
import me.spaceman.psilocin.eventsystem.events.RenderWorldEvent;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class BedESP extends Module {

    private CopyOnWriteArrayList<StoredBed> storedBeds = new CopyOnWriteArrayList<StoredBed>();

    public BedESP() {
        super("BedESP", Keyboard.KEY_Z, Category.MINIGAME, 0xFFBF4136);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
    }

    @EventSubscriber
    public void onBlockUpdate(final BlockUpdateEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        for(StoredBed bed : this.storedBeds)
        {
            bed.getSurroundingBlocks().clear();
            int headX = bed.head.getX();
            int headY = bed.head.getY();
            int headZ = bed.head.getZ();

            int footX = bed.foot.getX();
            int footY = bed.foot.getY();
            int footZ = bed.foot.getZ();

            for(int x = -1; x < 2; x++)
            {
                BlockPos headOffset = new BlockPos(headX + x, headY, headZ);
                BlockPos footOffset = new BlockPos(footX + x, footY, footZ);
                Block headBlock = mc.theWorld.getBlockState(headOffset).getBlock();
                Block footBlock = mc.theWorld.getBlockState(footOffset).getBlock();
                if(headBlock != Blocks.air && headBlock != Blocks.bed)
                {
                    bed.addSurroundingBlock(headBlock, 1);
                }

                if(footBlock != Blocks.air && footBlock != Blocks.bed)
                {
                    bed.addSurroundingBlock(footBlock, 1);
                }
            }

            for(int z = -1;  z < 2; z++)
            {
                BlockPos headOffset = new BlockPos(headX, headY, headZ + z);
                BlockPos footOffset = new BlockPos(footX, footY, footZ + z);
                Block headBlock = mc.theWorld.getBlockState(headOffset).getBlock();
                Block footBlock = mc.theWorld.getBlockState(footOffset).getBlock();
                if(headBlock != Blocks.air && headBlock != Blocks.bed)
                {
                    bed.addSurroundingBlock(headBlock, 1);
                }

                if(footBlock != Blocks.air && footBlock != Blocks.bed)
                {
                    bed.addSurroundingBlock(footBlock, 1);
                }
            }

            BlockPos headOffset = new BlockPos(headX, headY + 1, headZ);
            BlockPos footOffset = new BlockPos(footX, footY + 1, footZ);
            Block headBlock = mc.theWorld.getBlockState(headOffset).getBlock();
            Block footBlock = mc.theWorld.getBlockState(footOffset).getBlock();
            if(headBlock != Blocks.air && headBlock != Blocks.bed)
            {
                bed.addSurroundingBlock(headBlock, 1);
            }

            if(footBlock != Blocks.air && footBlock != Blocks.bed)
            {
                bed.addSurroundingBlock(footBlock, 1);
            }
        }

    }

    @EventSubscriber
    public void onPacketReceive(final ReceivePacketEvent event)
    {
        if(event.getPacket() instanceof S23PacketBlockChange)
        {
            S23PacketBlockChange blockChange = (S23PacketBlockChange) event.getPacket();
            Block block = blockChange.getBlockState().getBlock();
            if(block instanceof BlockBed)
            {
                addBed(blockChange.getBlockState(), blockChange.getBlockPosition());
            }
        }

        if(event.getPacket() instanceof S22PacketMultiBlockChange)
        {
            S22PacketMultiBlockChange blockChange = (S22PacketMultiBlockChange) event.getPacket();
            for(S22PacketMultiBlockChange.BlockUpdateData blockUpdate : blockChange.getChangedBlocks())
            {
                Block block = blockUpdate.getBlockState().getBlock();
                if(block instanceof BlockBed)
                {
                    addBed(blockUpdate.getBlockState(), blockUpdate.getPos());
                }
            }
        }
    }

    private void addBed(IBlockState state, BlockPos pos)
    {
        BlockBed bed = (BlockBed) state.getBlock();
        BlockPos footPos = null;
        if(state.getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD)
        {
            footPos = pos.offset((EnumFacing)state.getValue(BlockBed.FACING).getOpposite());
        } else if(state.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT)
        {
            footPos = pos;
            pos = pos.offset((EnumFacing)state.getValue(BlockBed.FACING));
        }

        if(footPos != null && Minecraft.getMinecraft().theWorld.getBlockState(footPos).getBlock() instanceof BlockBed)
        {
            StoredBed storedBed = new StoredBed(pos, footPos);
            if(!this.storedBeds.contains(storedBed))
                this.storedBeds.add(storedBed);
        }
    }

    @EventSubscriber
    public void onRenderWorld(final RenderWorldEvent event)
    {
        if(!this.isEnabled())
            return;

        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1, -1000000F);
        for(StoredBed beds : this.storedBeds)
        {
            if(mc.theWorld.getBlockState(beds.head).getBlock() != Blocks.bed)
            {
                this.storedBeds.remove(beds);
                continue;
            }
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            event.setCancelled(true);
            double distance = mc.getRenderViewEntity().getDistance(beds.head.getX(), beds.head.getY(), beds.head.getZ());
            double distanceMod = (2.5f / 30f) * distance;
            float scaleFactor = (float) Math.max(1f, Math.min(2.5f, distanceMod));

            double x = beds.head.getX() + 1 - mc.getRenderManager().renderPosX;
            double y = beds.head.getY() + 0.5 - mc.getRenderManager().renderPosY;
            double z = beds.head.getZ() + 0.5 - mc.getRenderManager().renderPosZ;

            String s = "Bed";
            float f1 = 0.02666667F;
            GlStateManager.alphaFunc(516, 0.1F);

            FontRenderer fontrenderer = mc.getRenderManager().getFontRenderer();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 1, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
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
            int renderX = 0;
            for(Map.Entry<Block, Integer> surroundingBlocks : beds.getSurroundingBlocks().entrySet())
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(-i + renderX, -10f, 0f);
                GL11.glScalef(10f, 10f, 10f);
                renderItem.renderItem(new ItemStack(surroundingBlocks.getKey()), ItemCameraTransforms.TransformType.GUI);
                GL11.glScalef(0.05f, 0.05f, 0.05f);
                GL11.glTranslatef(-5, 8, 0);
                fontrenderer.drawString(surroundingBlocks.getValue().toString(), 0, 0, 0xFFFFFFFF);
                GL11.glPopMatrix();
                renderX += 10;
            }
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1, 1000000F);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();

    }

    class StoredBed {
        public BlockPos head;
        public BlockPos foot;
        public StoredBed(BlockPos head, BlockPos foot)
        {
            this.head = head;
            this.foot = foot;
        }
        public HashMap<Block, Integer> surroundingBlocks = new HashMap<>();

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof StoredBed)
            {
                StoredBed stored = (StoredBed) obj;
                return this.foot.equals(stored.foot) && this.head.equals(stored.head);
            } else {
                return false;
            }
        }

        public void addSurroundingBlock(Block block, int count)
        {
            if(this.surroundingBlocks.containsKey(block))
            {
                int currentCount = this.surroundingBlocks.get(block);
                this.surroundingBlocks.replace(block, currentCount + count);
            } else {
                this.surroundingBlocks.put(block, count);
            }
        }

        public HashMap<Block, Integer> getSurroundingBlocks()
        {
            return this.surroundingBlocks;
        }

        @Override
        public int hashCode() {
            return head.hashCode() + foot.hashCode();
        }
    }
}
