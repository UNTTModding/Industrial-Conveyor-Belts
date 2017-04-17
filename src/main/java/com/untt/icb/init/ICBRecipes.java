package com.untt.icb.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ICBRecipes
{
    public static void init()
    {
        GameRegistry.addRecipe(new ItemStack(ICBBlocks.CONVEYOR, 6, 0), "lll", "iri", 'l', Items.LEATHER, 'i', Items.IRON_INGOT, 'r', Items.REDSTONE);
    }
}