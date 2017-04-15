package com.untt.icb.block;

import com.untt.icb.creativetab.CreativeTabsICB;
import com.untt.icb.reference.Textures;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;

public class BlockICB extends Block
{
    public BlockICB(Material material, String name)
    {
        super(material);

        this.setUnlocalizedName(name);
        this.setRegistryName(name);

        this.setCreativeTab(CreativeTabsICB.IMMERSIVE_CONVEYOR_BELTS);
    }

    public BlockICB(String name)
    {
        this(Material.GROUND, name);
    }

    @Override
    @Nonnull
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    private String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}