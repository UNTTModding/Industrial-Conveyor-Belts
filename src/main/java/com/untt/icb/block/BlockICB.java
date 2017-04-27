package com.untt.icb.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.untt.icb.creativetab.CreativeTabsICB;
import com.untt.icb.reference.Textures;
import com.untt.icb.tileentity.TileEntityICB;

public class BlockICB extends Block
{
    public BlockICB(Material material, String name)
    {
        super(material);

        this.setUnlocalizedName(name);
        this.setRegistryName(name);

        this.setCreativeTab(CreativeTabsICB.INDUSTRIAL_CONVEYOR_BELTS);
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


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityICB)
        {
            TileEntityICB tileICB = (TileEntityICB) worldIn.getTileEntity(pos);

            if (tileICB != null)
                tileICB.setFacing(placer.getHorizontalFacing());
        }
    }
}