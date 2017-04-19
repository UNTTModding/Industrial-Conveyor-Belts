package com.untt.icb.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityConveyorDetector extends TileEntityConveyorBase
{
    private NonNullList<ItemStack> filter = NonNullList.create();

    private int count = 0;

    public TileEntityConveyorDetector()
    {

    }

    public void addFilter(ItemStack stack)
    {
        filter.add(new ItemStack(stack.getItem()));
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    public int findMatchingItems(World world)
    {
        List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, Block.FULL_BLOCK_AABB.offset(pos));

        int count = 0;

        for (EntityItem item : itemList)
        {
            if (filter.isEmpty())
                count ++;

            else if (filterContainsItem(item.getEntityItem()))
                count ++;
        }

        setCount(count);

        return count;
    }

    private boolean filterContainsItem(ItemStack stack)
    {
        for (ItemStack filterStack : filter)
            if (stack.getItem().equals(filterStack.getItem())) return true;

        return false;
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