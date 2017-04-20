package com.untt.icb.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import com.untt.icb.utility.FilterFilter;

public class TileEntityConveyorDetector extends TileEntityConveyorBase
{
    private NonNullList<ItemStack> filter;
    private FilterFilter centerF=new FilterFilter();

    private int count = 0;
    
    public TileEntityConveyorDetector() {
    	filter = NonNullList.create();
    	for (int i = 0; i < 9; i++) {
    		filter.add(ItemStack.EMPTY);
		}
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
            if (filterContainsItem(item.getEntityItem()))
                count ++;
        }
        
        setCount(count);

        return count;
    }

    private boolean filterContainsItem(ItemStack stack)
    {
    	boolean match = filter.stream().anyMatch(fs -> centerF.match(fs, stack));
		return centerF.white^!match;
    }

    public int getCount()
    {
        return this.count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
    
    public FilterFilter getCenterF() {
		return centerF;
	}
    
    public NonNullList<ItemStack> getFilter() {
		return filter;
	}

	public void handleMessage(EntityPlayer player,NBTTagCompound nbt){
		centerF.deserializeNBT(nbt.getCompoundTag("c"));
	}

	@Override
    public void readFromNBT(NBTTagCompound compound) {
    	ItemStackHelper.loadAllItems(compound.getCompoundTag("filter"), filter);
    	centerF.deserializeNBT(compound.getCompoundTag("centerF"));
    	super.readFromNBT(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	NBTTagCompound center=new NBTTagCompound();
    	ItemStackHelper.saveAllItems(center,filter);
    	compound.setTag("filter", center);
    	compound.setTag("centerF", centerF.serializeNBT());
    	return super.writeToNBT(compound);
    }
}