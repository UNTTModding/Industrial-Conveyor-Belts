package com.untt.icb.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityConveyor extends TileEntityICB implements ITickable
{
    private static final double speed = 1.0D;

    protected boolean slopeUp;
    protected boolean slopeDown;

    public TileEntityConveyor()
    {
        super();

        slopeUp = false;
        slopeDown = false;
    }

    public boolean isSlopeUp()
    {
        return slopeUp;
    }

    public void setSlopeUp(boolean slopeUp)
    {
        this.slopeUp = slopeUp;
    }

    public boolean isSlopeDown()
    {
        return slopeDown;
    }

    public void setSlopeDown(boolean slopeDown)
    {
        this.slopeDown = slopeDown;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("slopeUp"))
            slopeUp = compound.getBoolean("slopeUp");

        if (compound.hasKey("slopeDown"))
            slopeDown = compound.getBoolean("slopeDown");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setBoolean("slopeUp", slopeUp);
        compound.setBoolean("slopeDown", slopeDown);

        return compound;
    }

    @Override
    public void update()
    {
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class,
                new AxisAlignedBB(pos.getX(), pos.getY() + 0.16F, pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.0F, pos.getZ() + 1.0F));

        for (Entity entity : entities)
        {
            switch (getFacing())
            {
                case NORTH: entity.motionZ = - 1.0D / 16.0D * speed; break;
                case EAST: entity.motionX = 1.0D / 16.0D * speed; break;
                case SOUTH: entity.motionZ = 1.0D / 16.0D * speed; break;
                case WEST: entity.motionX = - 1.0D / 16.0D * speed; break;
            }

            if (isSlopeUp())
                entity.motionY = 1.0D / 16.0D * speed;

            if (isSlopeDown())
                entity.motionY = - 1.0D / 16.0D * speed;
        }
    }
}