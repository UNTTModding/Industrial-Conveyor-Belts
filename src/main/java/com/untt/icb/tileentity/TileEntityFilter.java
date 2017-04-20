package com.untt.icb.tileentity;

import com.untt.icb.utility.FilterFilter;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;

public class TileEntityFilter extends TileEntityICB
{
    private NonNullList<ItemStack> filterLeft;
    private NonNullList<ItemStack> filterRight;
    private NonNullList<ItemStack> filterCenter;
    private FilterFilter leftF=new FilterFilter();
    private FilterFilter centerF=new FilterFilter();
    private FilterFilter rightF=new FilterFilter();

    public TileEntityFilter()
    {
		filterLeft = NonNullList.create();
		filterRight = NonNullList.create();
		filterCenter = NonNullList.create();
		for (int i = 0; i < 9; i++) {
			filterLeft.add(ItemStack.EMPTY);
			filterRight.add(ItemStack.EMPTY);
			filterCenter.add(ItemStack.EMPTY);
		}
    }

    public void addFilter(ItemStack stack, EnumFacing facing)
    {
        if (facing == getFacing().rotateYCCW())
            filterLeft.add(stack.copy());

        else if (facing == getFacing().rotateY())
            filterRight.add(stack.copy());

        else if (facing == getFacing())
            filterCenter.add(stack.copy());
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

        if (filterContainsItem(stack, filterLeft,leftF))
            return facingSorter.rotateYCCW();

        else if (filterContainsItem(stack, filterRight,rightF))
            return facingSorter.rotateY();

        else if (filterContainsItem(stack, filterCenter,centerF))
            return facingSorter;

        else
            return EnumFacing.UP;
    }

    private boolean filterContainsItem(ItemStack stack, NonNullList<ItemStack> filter,FilterFilter f)
    {
		boolean match = filter.stream().anyMatch(fs -> f.match(fs, stack));
//		System.out.println(match+" "+filter);
		return f.white^!match;
//		if (f.white)
//			return match;
//		else
//			return !match;
    }
    
    public NonNullList<ItemStack> getFilterLeft() {
		return filterLeft;
	}

	public NonNullList<ItemStack> getFilterRight() {
		return filterRight;
	}

	public NonNullList<ItemStack> getFilterCenter() {
		return filterCenter;
	}
	
	public FilterFilter getLeftF() {
		return leftF;
	}

	public FilterFilter getCenterF() {
		return centerF;
	}

	public FilterFilter getRightF() {
		return rightF;
	}

	public void handleMessage(EntityPlayer player,NBTTagCompound nbt){
		leftF.deserializeNBT(nbt.getCompoundTag("l"));
		centerF.deserializeNBT(nbt.getCompoundTag("c"));
		rightF.deserializeNBT(nbt.getCompoundTag("r"));
	}

	@Override
    public void readFromNBT(NBTTagCompound compound) {
    	ItemStackHelper.loadAllItems(compound.getCompoundTag("filterLeft"), filterLeft);
    	ItemStackHelper.loadAllItems(compound.getCompoundTag("filterCenter"), filterCenter);
    	ItemStackHelper.loadAllItems(compound.getCompoundTag("filterRight"), filterRight);
    	leftF.deserializeNBT(compound.getCompoundTag("leftF"));
    	centerF.deserializeNBT(compound.getCompoundTag("centerF"));
    	rightF.deserializeNBT(compound.getCompoundTag("rightF"));
    	super.readFromNBT(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	NBTTagCompound left=new NBTTagCompound();
    	ItemStackHelper.saveAllItems(left,filterLeft);
    	compound.setTag("filterLeft", left);
    	NBTTagCompound center=new NBTTagCompound();
    	ItemStackHelper.saveAllItems(center,filterCenter);
    	compound.setTag("filterCenter", center);
    	NBTTagCompound right=new NBTTagCompound();
    	ItemStackHelper.saveAllItems(right,filterRight);
    	compound.setTag("filterRight", right);
    	compound.setTag("leftF", leftF.serializeNBT());
    	compound.setTag("centerF", centerF.serializeNBT());
    	compound.setTag("rightF", rightF.serializeNBT());
    	return super.writeToNBT(compound);
    }
}