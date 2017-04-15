package com.untt.icb.client.model;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
interface MeshDefinitionFix extends ItemMeshDefinition
{
    ModelResourceLocation getLocation(ItemStack stack);

    static ItemMeshDefinition create(MeshDefinitionFix lambda)
    {
        return lambda;
    }

    default ModelResourceLocation getModelLocation(ItemStack stack)
    {
        return getLocation(stack);
    }
}