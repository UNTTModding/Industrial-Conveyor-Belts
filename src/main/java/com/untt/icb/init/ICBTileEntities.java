package com.untt.icb.init;

import com.untt.icb.reference.Names;
import com.untt.icb.tileentity.TileEntityConveyor;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ICBTileEntities
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityConveyor.class, Names.Blocks.CONVEYOR);
    }
}