package com.untt.icb.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.untt.icb.init.ICBBlocks;
import com.untt.icb.reference.Reference;

public class CreativeTabsICB
{
    public static final CreativeTabs INDUSTRIAL_CONVEYOR_BELTS = new CreativeTabs(Reference.MOD_ID.toLowerCase())
    {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ICBBlocks.CONVEYOR);
        }
    };
}