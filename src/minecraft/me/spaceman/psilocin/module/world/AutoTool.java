package me.spaceman.psilocin.module.world;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.PlayerLivingUpdateEvent;
import me.spaceman.psilocin.module.Module;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", Keyboard.KEY_J, Category.WORLD, 0xFFFF5722);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
    }

    @EventSubscriber
    public void onPlayerUpdate(final PlayerLivingUpdateEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(this.isEnabled() && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            Block block = mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
            float highest = 0f;
            ItemStack highestItem = null;
            int itemSlot = mc.thePlayer.inventory.currentItem;
            for(int i = 0; i < 9; i++)
            {
                ItemStack itemStack = event.getPlayer().inventory.mainInventory[i];
                if(itemStack != null)
                {
                    if(itemStack.getStrVsBlock(block) > highest) {
                        highest = itemStack.getStrVsBlock(block);
                        highestItem = itemStack;
                        itemSlot = i;
                    }
                }
            }
            mc.thePlayer.inventory.currentItem = itemSlot;
        }
    }
}
