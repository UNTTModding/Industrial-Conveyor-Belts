package com.untt.icb.tileentity;

import com.untt.icb.block.BlockConveyor;
import com.untt.icb.reference.Names;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityConveyorBase extends TileEntityICB
{
    protected static final String TAG_FACING = "facing";

    public TileEntityConveyorBase()
    {
        super();
    }

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
}