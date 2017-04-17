package com.untt.icb.creativetab;

import com.untt.icb.init.ICBBlocks;
import com.untt.icb.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabsICB
{
    public static final CreativeTabs IMMERSIVE_CONVEYOR_BELTS = new CreativeTabs(Reference.MOD_ID.toLowerCase())
    {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ICBBlocks.CONVEYOR);
        }
    };
}