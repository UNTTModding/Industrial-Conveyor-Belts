package com.untt.icb.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityConveyorDetectorMob extends TileEntityConveyorBase
{
    private int count = 0;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    public int findMatchingMobs(World world)
    {
        List<EntityLiving> mobList = world.getEntitiesWithinAABB(EntityLiving.class, Block.FULL_BLOCK_AABB.offset(pos));

        int count = mobList.size();
        
        setCount(count);

        return count;
    }

    public int getCount()
    {
        return this.count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}