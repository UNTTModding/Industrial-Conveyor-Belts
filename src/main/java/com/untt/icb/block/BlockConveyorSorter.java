package com.untt.icb.block;

import com.untt.icb.tileentity.TileEntityConveyorSorter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockConveyorSorter extends BlockConveyorBase
{

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);

    public BlockConveyorSorter(String name)
    {
        super(name);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasTileEntity()
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityConveyorSorter();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn instanceof EntityItem)
            {
                EntityItem entityItem = (EntityItem) entityIn;


                if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityConveyorSorter)
                {
                    TileEntityConveyorSorter tileSorter = (TileEntityConveyorSorter) worldIn.getTileEntity(pos);

                    tileSorter.sortItemStack(entityItem.getEntityItem());

                    worldIn.removeEntity(entityItem);
                }
            }
        }
    }
}