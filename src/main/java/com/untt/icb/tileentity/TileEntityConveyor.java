package com.untt.icb.tileentity;

import com.untt.icb.block.BlockConveyor;
import com.untt.icb.reference.Names;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nonnull;

public class TileEntityConveyor extends TileEntityConveyorBase
{
    private static final String TAG_SLOPE_UP = "slope_up";
    private static final String TAG_SLOPE_DOWN = "slope_down";
    private static final String TAG_TURN_LEFT = "turn_left";
    private static final String TAG_TURN_RIGHT = "turn_right";
    private static final String TAG_POWERED = "powered";

    public TileEntityConveyor()
    {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey(TAG_SLOPE_UP))
            getTileData().setBoolean(TAG_SLOPE_UP, compound.getBoolean(TAG_SLOPE_UP));

        if (compound.hasKey(TAG_SLOPE_DOWN))
            getTileData().setBoolean(TAG_SLOPE_DOWN, compound.getBoolean(TAG_SLOPE_DOWN));

        if (compound.hasKey(TAG_TURN_LEFT))
            getTileData().setBoolean(TAG_TURN_LEFT, compound.getBoolean(TAG_TURN_LEFT));

        if (compound.hasKey(TAG_TURN_RIGHT))
            getTileData().setBoolean(TAG_TURN_RIGHT, compound.getBoolean(TAG_TURN_RIGHT));

        if (compound.hasKey(TAG_POWERED))
            getTileData().setBoolean(TAG_POWERED, compound.getBoolean(TAG_POWERED));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setBoolean(TAG_SLOPE_UP, isSlopeUp());
        compound.setBoolean(TAG_SLOPE_DOWN, isSlopeDown());
        compound.setBoolean(TAG_TURN_LEFT, isTurnLeft());
        compound.setBoolean(TAG_TURN_RIGHT, isTurnRight());
        compound.setBoolean(TAG_POWERED, isPowered());

        return compound;
    }

    public IExtendedBlockState writeExtendedBlockState(IExtendedBlockState state)
    {
        state = super.writeExtendedBlockState(state);

        boolean slopeUp = isSlopeUp();
        boolean slopeDown = isSlopeDown();
        boolean turn_left = isTurnLeft();
        boolean turn_right = isTurnRight();
        boolean powered = isPowered();

        state = state.withProperty(BlockConveyor.SLOPE_UP, slopeUp);
        state = state.withProperty(BlockConveyor.SLOPE_DOWN, slopeDown);
        state = state.withProperty(BlockConveyor.TURN_LEFT, turn_left);
        state = state.withProperty(BlockConveyor.TURN_RIGHT, turn_right);
        state = state.withProperty(BlockConveyor.POWERED, powered);

        return state;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);

        NBTTagCompound tag = pkt.getNbtCompound();

        NBTBase slopeUp = tag.getTag(TAG_SLOPE_UP);
        NBTBase slopeDown = tag.getTag(TAG_SLOPE_DOWN);
        NBTBase turnLeft = tag.getTag(TAG_TURN_LEFT);
        NBTBase turnRight = tag.getTag(TAG_TURN_RIGHT);
        NBTBase powered = tag.getTag(TAG_POWERED);

        getTileData().setTag(TAG_SLOPE_UP, slopeUp);
        getTileData().setTag(TAG_SLOPE_DOWN, slopeDown);
        getTileData().setTag(TAG_TURN_LEFT, turnLeft);
        getTileData().setTag(TAG_TURN_RIGHT, turnRight);
        getTileData().setTag(TAG_POWERED, powered);

        readFromNBT(tag);
    }

    public boolean isSlopeUp()
    {
        return getTileData().getBoolean(TAG_SLOPE_UP);
    }

    public boolean isSlopeDown()
    {
        return getTileData().getBoolean(TAG_SLOPE_DOWN);
    }

    public boolean isTurnLeft()
    {
        return getTileData().getBoolean(TAG_TURN_LEFT);
    }

    public boolean isTurnRight()
    {
        return getTileData().getBoolean(TAG_TURN_RIGHT);
    }

    public boolean isPowered()
    {
        return getTileData().getBoolean(TAG_POWERED);
    }

    public void setSlopeUp(boolean slopeUP)
    {
        getTileData().setBoolean(TAG_SLOPE_UP, slopeUP);

        if (slopeUP)
            setSlopeDown(false);
    }

    public void setSlopeDown(boolean slopeDown)
    {
        getTileData().setBoolean(TAG_SLOPE_DOWN, slopeDown);

        if (slopeDown)
            setSlopeUp(false);
    }

    public void setTurnLeft(boolean turnLeft)
    {
        getTileData().setBoolean(TAG_TURN_LEFT, turnLeft);

        if (turnLeft)
            setTurnRight(false);
    }

    public void setTurnRight(boolean turnRight)
    {
        getTileData().setBoolean(TAG_TURN_RIGHT, turnRight);

        if (turnRight)
            setTurnLeft(false);
    }

    public void setPowered(boolean powered)
    {
        getTileData().setBoolean(TAG_POWERED, powered);
    }

}