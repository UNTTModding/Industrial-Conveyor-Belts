package com.untt.icb.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.untt.icb.reference.Names;
import com.untt.icb.tileentity.TileEntityConveyor;
import com.untt.icb.tileentity.TileEntityConveyorDetector;
import com.untt.icb.tileentity.TileEntityConveyorDetectorMob;
import com.untt.icb.tileentity.TileEntityFilter;

public class ICBTileEntities
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityConveyor.class, Names.TileEntities.CONVEYOR);
        GameRegistry.registerTileEntity(TileEntityConveyorDetector.class, Names.TileEntities.CONVEYOR_DETECTOR);
        GameRegistry.registerTileEntity(TileEntityConveyorDetectorMob.class, Names.TileEntities.CONVEYOR_DETECTOR_MOB);

        GameRegistry.registerTileEntity(TileEntityFilter.class, Names.TileEntities.FILTER);
    }
}