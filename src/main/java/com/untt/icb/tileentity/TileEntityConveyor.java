package com.untt.icb.tileentity;

import com.untt.icb.utility.LogHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityConveyor extends TileEntityICB implements ITickable
{
    private static final double speed = 1.0D;

    public TileEntityConveyor()
    {

    }

    @Override
    public void update()
    {
        LogHelper.info(getFacing());

        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class,
                new AxisAlignedBB(pos.getX(), pos.getY() + 0.16F, pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.0F, pos.getZ() + 1.0F));

        for (Entity entity : entities)
        {
            entity.move(MoverType.SELF, 1.0D / 16.0D * speed, 0.0D, 0.0D);
        }
    }
}