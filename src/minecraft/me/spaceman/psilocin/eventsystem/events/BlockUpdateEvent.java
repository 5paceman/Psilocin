package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class BlockUpdateEvent extends Event{

    private BlockPos pos;
    private IBlockState state;
    //World#setBlockState
    public BlockUpdateEvent(BlockPos blockPos, IBlockState state) {
        super(BLOCK_UPDATE_EVENT);
        this.pos = blockPos;
        this.state = state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockState getState() {
        return state;
    }
}
