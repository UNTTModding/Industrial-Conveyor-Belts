package com.untt.icb.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.untt.icb.tileentity.TileEntityConveyorBase;

public class EventHandlerClientTickConveyor
{
    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        World world = Minecraft.getMinecraft().world;

        if (world == null)
            return;

        for (Entity e : world.loadedEntityList)
        {
            if (e instanceof EntityItem && e.ticksExisted % 15 == 0 && !(world.getTileEntity(new BlockPos(e)) instanceof TileEntityConveyorBase))
                e.getEntityData().setBoolean("onBelt", false);
        }
    }
}