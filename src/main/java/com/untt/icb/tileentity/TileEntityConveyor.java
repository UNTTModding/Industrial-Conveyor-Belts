package com.untt.icb.tileentity;

import com.untt.icb.block.BlockConveyor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityConveyor extends TileEntityICB
{
    private static final String TAG_FACING = "facing";
    private static final String TAG_SLOPE_UP = "slope_up";
    private static final String TAG_SLOPE_DOWN = "slope_down";

    public TileEntityConveyor()
    {
        super();
    }

    public IExtendedBlockState writeExtendedBlockState(IExtendedBlockState state)
    {
        EnumFacing facing = getFacing();
        boolean slopeUp = isSlopeUp();
        boolean slopeDown = isSlopeDown();

        state = state.withProperty(BlockConveyor.FACING, facing);
        state = state.withProperty(BlockConveyor.SLOPE_UP, slopeUp);
        state = state.withProperty(BlockConveyor.SLOPE_DOWN, slopeDown);

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
        NBTBase slopeUp = tag.getTag(TAG_SLOPE_UP);
        NBTBase slopeDown = tag.getTag(TAG_SLOPE_DOWN);

        getTileData().setTag(TAG_FACING, facing);
        getTileData().setTag(TAG_SLOPE_UP, slopeUp);
        getTileData().setTag(TAG_SLOPE_DOWN, slopeDown);

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

    public EnumFacing getFacing()
    {
        return EnumFacing.getFront(getTileData().getInteger(TAG_FACING));
    }

    public boolean isSlopeUp()
    {
        return getTileData().getBoolean(TAG_SLOPE_UP);
    }

    public boolean isSlopeDown()
    {
        return getTileData().getBoolean(TAG_SLOPE_DOWN);
    }

    public void setFacing(EnumFacing facing)
    {
        getTileData().setInteger(TAG_FACING, facing.getIndex());
    }

    public void setSlopeUP(boolean slopeUP)
    {
        getTileData().setBoolean(TAG_SLOPE_UP, slopeUP);
    }

    public void setSlopeDown(boolean slopeDown)
    {
        getTileData().setBoolean(TAG_SLOPE_DOWN, slopeDown);
    }
}