package com.untt.icb.init;

import com.untt.icb.block.BlockBridge;
import com.untt.icb.block.BlockConveyor;
import com.untt.icb.block.BlockFilter;
import com.untt.icb.reference.Names;
import com.untt.icb.registry.BlockRegistry;
import net.minecraft.block.Block;

public class ICBBlocks
{
    public static Block CONVEYOR;
    public static Block CONVEYOR_SORTER;
    public static Block BRIDGE;

    public static void preInit()
    {
        CONVEYOR = BlockRegistry.registerBlock(new BlockConveyor(Names.Blocks.CONVEYOR));
        CONVEYOR_SORTER = BlockRegistry.registerBlock(new BlockFilter(Names.Blocks.FILTER));
        BRIDGE = BlockRegistry.registerBlock(new BlockBridge(Names.Blocks.BRIDGE));
    }
}