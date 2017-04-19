package com.untt.icb.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityConveyorDetector extends TileEntityConveyorBase
{
    private int count = 0;

    public TileEntityConveyorDetector()
    {

    }

    public int findMatchingItems(World world)
    {
        List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, Block.FULL_BLOCK_AABB.offset(pos));

        int count = 0;

        for (EntityItem item : itemList)
            count ++;

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