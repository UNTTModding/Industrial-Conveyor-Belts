package com.untt.icb.tileentity;

import com.untt.icb.block.BlockConveyor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TileEntityConveyor extends TileEntityConveyorBase
{
    private static final String TAG_SLOPE_UP = "slope_up";
    private static final String TAG_SLOPE_DOWN = "slope_down";
    private static final String TAG_TURN_LEFT = "turn_left";
    private static final String TAG_TURN_RIGHT = "turn_right";

    public TileEntityConveyor()
    {
        super();
    }

    public IExtendedBlockState writeExtendedBlockState(IExtendedBlockState state)
    {
        state = super.writeExtendedBlockState(state);

        boolean slopeUp = isSlopeUp();
        boolean slopeDown = isSlopeDown();
        boolean turn_left = isTurnLeft();
        boolean turn_right = isTurnRight();

        state = state.withProperty(BlockConveyor.SLOPE_UP, slopeUp);
        state = state.withProperty(BlockConveyor.SLOPE_DOWN, slopeDown);
        state = state.withProperty(BlockConveyor.TURN_LEFT, turn_left);
        state = state.withProperty(BlockConveyor.TURN_RIGHT, turn_right);

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

        getTileData().setTag(TAG_SLOPE_UP, slopeUp);
        getTileData().setTag(TAG_SLOPE_DOWN, slopeDown);
        getTileData().setTag(TAG_TURN_LEFT, turnLeft);
        getTileData().setTag(TAG_TURN_RIGHT, turnRight);

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

    public void setSlopeUp(boolean slopeUP)
    {
        getTileData().setBoolean(TAG_SLOPE_UP, slopeUP);
    }

    public void setSlopeDown(boolean slopeDown)
    {
        getTileData().setBoolean(TAG_SLOPE_DOWN, slopeDown);
    }

    public void setTurnLeft(boolean turnLeft)
    {
        getTileData().setBoolean(TAG_TURN_LEFT, turnLeft);
    }

    public void setTagTurnRight(boolean turnRight)
    {
        getTileData().setBoolean(TAG_TURN_RIGHT, turnRight);
    }
}