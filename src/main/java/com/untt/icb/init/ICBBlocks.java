package com.untt.icb.init;

import com.untt.icb.block.BlockICB;
import com.untt.icb.reference.Names;
import com.untt.icb.registry.BlockRegistry;
import net.minecraft.block.Block;

public class ICBBlocks
{
    public static Block CONVEYOR;

    public static void preInit()
    {
        CONVEYOR = BlockRegistry.registerBlock(new BlockICB(Names.Blocks.CONVEYOR));
    }
}