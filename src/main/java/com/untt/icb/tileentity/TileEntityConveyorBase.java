package com.untt.icb.tileentity;

import com.untt.icb.block.BlockConveyor;
import com.untt.icb.block.BlockConveyorBase;
import com.untt.icb.reference.Names;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityConveyorBase extends TileEntityICB implements ITickable
{
    protected static final String TAG_FACING = "facing";

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey(Names.NBT.FACING))
            getTileData().setInteger(TAG_FACING, compound.getInteger(Names.NBT.FACING));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setInteger(Names.NBT.FACING, getFacing().getIndex());

        return compound;
    }

    public IExtendedBlockState writeExtendedBlockState(IExtendedBlockState state)
    {
        EnumFacing facing = getFacing();

        state = state.withProperty(BlockConveyor.FACING, facing);

        return state;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = getTileData().copy();

        writeToNBT(tag);

        return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();

        NBTBase facing = tag.getTag(TAG_FACING);

        getTileData().setTag(TAG_FACING, facing);

        readFromNBT(tag);
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(@Nullable NBTTagCompound tag)
    {
        readFromNBT(tag);
    }

    @Override
    @Nonnull
    public EnumFacing getFacing()
    {
        return EnumFacing.getFront(getTileData().getInteger(TAG_FACING));
    }

    @Override
    public void setFacing(EnumFacing facing)
    {
        getTileData().setInteger(TAG_FACING, facing.getIndex());
    }
    

	@Override
	public void update() {
		if (BlockConveyorBase.INSERTIONEX && !world.isRemote && world.getTotalWorldTime() % 20 == 0) {
			if (!(this instanceof TileEntityConveyor) || !(((TileEntityConveyor) this).isSlopeUp() && !((TileEntityConveyor) this).isSlopeDown() && !((TileEntityConveyor) this).isTurnRight() && !((TileEntityConveyor) this).isTurnLeft())) {
				EnumFacing facing = getFacing();
				BlockPos handlerPos = getPos().offset(facing.getOpposite());
				TileEntity ht = world.getTileEntity(handlerPos);
				if (ht != null && ht.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
					IItemHandler inv = ht.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
					if (inv != null) {
						ItemStack ex = ItemStack.EMPTY;
						for (int i = 0; i < inv.getSlots(); i++) {
							ex = inv.extractItem(i, 2, false);
							if (!ex.isEmpty())
								break;
						}
						if (!ex.isEmpty()) {
							Vec3d posSpawn = new Vec3d(pos.getX() + 0.5D - facing.getFrontOffsetX() * .35, pos.getY() + 0.4D, pos.getZ() + 0.5D - facing.getFrontOffsetZ() * .35);
							EntityItem ei = new EntityItem(world, posSpawn.xCoord, posSpawn.yCoord, posSpawn.zCoord, ex);
							ei.motionY = 0.1;
							world.spawnEntity(ei);
						}
					}
				}
			}
		}
	}
}