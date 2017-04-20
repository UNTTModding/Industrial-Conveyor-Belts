package com.untt.icb.block;

import com.untt.icb.tileentity.TileEntityConveyorDetectorMob;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockConveyorDetectorMob extends BlockConveyorBase implements ITileEntityProvider
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.15, 1.0);
    private static final AxisAlignedBB COLLISION = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);

    public BlockConveyorDetectorMob(String name)
    {
        super(name);

        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
        return new TileEntityConveyorDetectorMob();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return COLLISION;
    }
    
    @Override
    @Nonnull
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
    	return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

        if (!worldIn.isRemote && entityIn instanceof EntityLiving)
            updateComparatorOutput(worldIn, pos);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
    {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityConveyorDetectorMob)
        {
            TileEntityConveyorDetectorMob tileDetector = (TileEntityConveyorDetectorMob) world.getTileEntity(pos);

            return tileDetector.getCount();
        }

        return 0;
    }

    @Override
    public int tickRate(World world)
    {
        return 5;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (!world.isRemote)
            updateComparatorOutput(world, pos);
    }

    private void updateComparatorOutput(World world, BlockPos pos)
    {
        if (!world.isRemote)
        {
            TileEntityConveyorDetectorMob tileDetector = (TileEntityConveyorDetectorMob) world.getTileEntity(pos);

            int count = tileDetector.findMatchingMobs(world);

            if (count > 0)
                world.scheduleUpdate(new BlockPos(pos), this, tickRate(world));

            world.updateComparatorOutputLevel(pos, this);
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta + 2));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex() - 2;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }
}