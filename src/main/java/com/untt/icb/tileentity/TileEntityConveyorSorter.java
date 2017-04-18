package com.untt.icb.tileentity;

import com.untt.icb.utility.LogHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TileEntityConveyorSorter extends TileEntityConveyorBase
{
    public TileEntityConveyorSorter()
    {

    }

    public void sortItemStack(ItemStack stack)
    {
        LogHelper.info("Sort item: " + stack.getItem().getUnlocalizedName());

        float posX = pos.getX() + 0.5F;
        float posY = pos.getY() + 0.2F;
        float posZ = pos.getZ() + 0.5F;

        float velX = 0.0F;
        float velY = 0.0F;
        float velZ = 0.0F;

        if (stack.getItem().equals(Items.REDSTONE))
        {
            posX += 0.6F;
            velX += 0.1F;
        }

        else if (stack.getItem().equals(Items.DIAMOND))
        {
            posZ -= 0.6F;
            velZ -= 0.1F;
        }

        else
        {
            posZ += 0.6F;
            velZ += 0.1F;
        }

        EntityItem entityItem = new EntityItem(world, posX, posY, posZ, stack);
        entityItem.setVelocity(velX, velY, velZ);

        world.spawnEntity(entityItem);
    }
}