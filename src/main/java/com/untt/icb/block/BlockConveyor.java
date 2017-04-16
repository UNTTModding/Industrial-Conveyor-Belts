package com.untt.icb.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockConveyor extends BlockICB
{
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final PropertyBool SLOPE_UP = PropertyBool.create("slope_up");
    private static final PropertyBool SLOPE_DOWN = PropertyBool.create("slope_down");

    private static final AxisAlignedBB BOUNDS_FLAT = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.16F, 1.0F);
    private static final AxisAlignedBB BOUNDS_SLOPED = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);

    private static final double transportation_speed = 0.1D;

    public BlockConveyor(String name)
    {
        super(Material.CLOTH, name);

        this.setHardness(0.5F);
        this.useNeighborBrightness = true;

        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SLOPE_UP, false).withProperty(SLOPE_DOWN, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if (state.getValue(SLOPE_UP) || state.getValue(SLOPE_DOWN))
            return BOUNDS_SLOPED;

        return BOUNDS_FLAT;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        // Get rotation
        EnumFacing facing = placer.getHorizontalFacing();

        // Create default values
        boolean slopeUp = false;
        boolean slopeDown = false;

        // Check if conveyor is a slope
        IBlockState stateFrontUp = worldIn.getBlockState(pos.offset(facing).up());
        IBlockState stateBackUp = worldIn.getBlockState(pos.offset(facing.getOpposite()).up());

        if (stateFrontUp.getBlock() == this && stateFrontUp.getValue(FACING) == facing)
            slopeUp = true;

        if (stateBackUp.getBlock() == this && stateBackUp.getValue(FACING) == facing)
            slopeDown = true;

        // Update relevant conveyors
        BlockPos posFrontDown = pos.offset(facing).down();
        BlockPos posBackDown = pos.offset(facing.getOpposite()).down();

        IBlockState stateFrontDown = worldIn.getBlockState(posFrontDown);
        IBlockState stateBackDown = worldIn.getBlockState(posBackDown);

        if (stateFrontDown.getBlock() == this)
            updateConveyor(worldIn, posFrontDown, stateFrontDown, pos, state.withProperty(FACING, facing).withProperty(SLOPE_UP, slopeUp).withProperty(SLOPE_DOWN, slopeDown));

        if (stateBackDown.getBlock() == this)
            updateConveyor(worldIn, posBackDown, stateBackDown, pos, state.withProperty(FACING, facing).withProperty(SLOPE_UP, slopeUp).withProperty(SLOPE_DOWN, slopeDown));

        // Set BlockState
        worldIn.setBlockState(pos, state.withProperty(FACING, facing).withProperty(SLOPE_UP, slopeUp).withProperty(SLOPE_DOWN, slopeDown));

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    private void updateConveyor(World worldIn, BlockPos pos, IBlockState state, BlockPos posFrom, IBlockState stateFrom)
    {
        // Get current state
        EnumFacing facing = state.getValue(FACING);

        boolean up = state.getValue(SLOPE_UP);
        boolean down = state.getValue(SLOPE_DOWN);

        // Facing same direction
        if (stateFrom.getValue(FACING) == facing)
        {
            // Slope - Down
            if (posFrom.equals(pos.offset(facing.getOpposite()).up()))
                down = true;

            // Slope - Up
            if(posFrom.equals(pos.offset(facing).up()))
                up = true;
        }

        // Update BlockState
        worldIn.setBlockState(pos, state.withProperty(FACING, facing).withProperty(SLOPE_UP, up).withProperty(SLOPE_DOWN, down));
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
            moveEntity(entityIn, state.getValue(FACING), state.getValue(SLOPE_UP), state.getValue(SLOPE_DOWN), pos);
    }

    private void moveEntity(Entity entity, EnumFacing facing, boolean slopeUp, boolean slopeDown, BlockPos pos)
    {
        if (entity.canBePushed() || entity instanceof EntityItem || entity instanceof EntityLiving || entity instanceof EntityXPOrb)
        {
            entity.isAirBorne = true;

            entity.motionX += transportation_speed * facing.getFrontOffsetX();
            entity.motionZ += transportation_speed * facing.getFrontOffsetZ();

            if (slopeUp)
            {
                entity.onGround = false;

                entity.motionY = transportation_speed;
            }

            if (entity instanceof EntityItem)
                ((EntityItem) entity).setAgeToCreativeDespawnTime();
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront((meta & 3) + 2)).withProperty(SLOPE_UP, (meta & 4) > 0).withProperty(SLOPE_DOWN, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = (state.getValue(FACING)).getIndex() - 2;

        if (state.getValue(SLOPE_UP))
            meta |= 4;

        if (state.getValue(SLOPE_DOWN))
            meta |= 8;

        return meta;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, SLOPE_UP, SLOPE_DOWN);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}