package com.untt.icb.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class TileEntityFilter extends TileEntityICB
{
    private ArrayList<ItemStack> filterLeft = new ArrayList<>();
    private ArrayList<ItemStack> filterRight = new ArrayList<>();
    private ArrayList<ItemStack> filterCenter = new ArrayList<>();

    public TileEntityFilter()
    {

    }

    public void addFilter(ItemStack stack, EnumFacing facing)
    {
        if (facing == getFacing().rotateYCCW())
            filterLeft.add(new ItemStack(stack.getItem()));

        else if (facing == getFacing().rotateY())
            filterRight.add(new ItemStack(stack.getItem()));

        else if (facing == getFacing())
            filterCenter.add(new ItemStack(stack.getItem()));
    }

    public void sortItemStack(ItemStack stack)
    {
        EnumFacing facingSorted = getSideForItem(stack);

        Vec3d posSpawn = new Vec3d(pos.offset(facingSorted).getX() + 0.5D-facingSorted.getFrontOffsetX()*.35, pos.offset(facingSorted).getY() + 0.4D, pos.offset(facingSorted).getZ() + 0.5D-facingSorted.getFrontOffsetZ()*.35);
        Vec3d velocity = new Vec3d(0.03D * facingSorted.getFrontOffsetX(), 0.1D, 0.03D * facingSorted.getFrontOffsetZ());

        EntityItem entityItem = new EntityItem(world, posSpawn.xCoord, posSpawn.yCoord, posSpawn.zCoord, stack);
        entityItem.isAirBorne=true;
        entityItem.motionX=velocity.xCoord;
        entityItem.motionY=velocity.yCoord;
        entityItem.motionZ=velocity.zCoord;

        world.spawnEntity(entityItem);
    }

    private EnumFacing getSideForItem(ItemStack stack)
    {
        EnumFacing facingSorter = getFacing();

        if (filterContainsItem(stack, filterLeft))
            return facingSorter.rotateYCCW();

        else if (filterContainsItem(stack, filterRight))
            return facingSorter.rotateY();

        else if (filterContainsItem(stack, filterCenter))
            return facingSorter;

        else
            return EnumFacing.UP;
    }

    private boolean filterContainsItem(ItemStack stack, ArrayList<ItemStack> filter)
    {
        for (ItemStack filterStack : filter)
            if (stack.getItem().equals(filterStack.getItem())) return true;

        return false;
    }
}