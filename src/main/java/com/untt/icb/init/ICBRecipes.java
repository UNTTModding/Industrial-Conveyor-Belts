package com.untt.icb.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ICBRecipes
{
    public static void init()
    {
        GameRegistry.addRecipe(new ItemStack(ICBBlocks.BRIDGE, 3, 0), "qqq", 'q', new ItemStack(Blocks.STONE_SLAB, 1, 7));

        GameRegistry.addRecipe(new ItemStack(ICBBlocks.CONVEYOR, 6, 0), "nnn", "iri", 'l', Items.field_191525_da, 'i', Items.IRON_INGOT, 'r', Items.REDSTONE);
        GameRegistry.addRecipe(new ItemStack(ICBBlocks.CONVEYOR_DETECTOR, 1, 0), " c ", " p ", 'c', ICBBlocks.CONVEYOR, 'p', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        GameRegistry.addRecipe(new ItemStack(ICBBlocks.CONVEYOR_DETECTOR_MOB, 1, 0), " c ", "gpg", 'c', ICBBlocks.CONVEYOR, 'p', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 'g', Items.GOLD_NUGGET);

        GameRegistry.addRecipe(new ItemStack(ICBBlocks.FILTER, 1, 0), " r ", "gsg", "bpb", 'r', Items.REDSTONE, 'g', Items.GOLD_NUGGET, 's', Items.STRING, 'b', Blocks.STONE, 'p', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
    }
}