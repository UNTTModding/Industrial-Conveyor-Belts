package com.untt.icb.block;

import com.untt.icb.tileentity.TileEntityConveyor;
import com.untt.icb.tileentity.TileEntityICB;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
import javax.annotation.Nullable;

public class BlockConveyor extends BlockICB implements ITileEntityProvider
{
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final PropertyBool UP = PropertyBool.create("up");
    private static final PropertyBool DOWN = PropertyBool.create("down");

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.16F, 1.0F);

    public BlockConveyor(String name)
    {
        super(Material.CORAL, name);

        this.setHardness(0.5F);
        this.useNeighborBrightness = true;

        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(UP, false).withProperty(DOWN, false));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityConveyor();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        EnumFacing facing = placer.getHorizontalFacing();

        boolean up = false;
        boolean down = false;

        IBlockState state_front_up = worldIn.getBlockState(pos.offset(facing).up());
        IBlockState state_back_up = worldIn.getBlockState(pos.offset(facing.getOpposite()).up());

        if (state_front_up.getBlock() == this && state_front_up.getValue(FACING) == facing)
            up = true;

        if (state_back_up.getBlock() == this && state_back_up.getValue(FACING) == facing)
            down = true;

        worldIn.setBlockState(pos, state.withProperty(FACING, facing).withProperty(UP, up).withProperty(DOWN, down));

        BlockPos pos_front_down = pos.offset(facing).down();
        BlockPos pos_back_down = pos.offset(facing.getOpposite()).down();

        IBlockState state_front_down = worldIn.getBlockState(pos_front_down);
        IBlockState state_back_down = worldIn.getBlockState(pos_back_down);

        if (state_front_down.getBlock() == this)
            updateFacing(worldIn, pos_front_down, state_front_down, pos, state.withProperty(FACING, facing).withProperty(UP, up).withProperty(DOWN, down));

        if (state_back_down.getBlock() == this)
            updateFacing(worldIn, pos_back_down, state_back_down, pos, state.withProperty(FACING, facing).withProperty(UP, up).withProperty(DOWN, down));

        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityICB)
        {
            TileEntityConveyor tileConveyor = (TileEntityConveyor) worldIn.getTileEntity(pos);

            if (tileConveyor != null)
            {
                tileConveyor.setSlopeUp(up);
                tileConveyor.setSlopeDown(down);
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    private void updateFacing(World worldIn, BlockPos pos, IBlockState state, BlockPos posFrom, IBlockState stateFrom)
    {
        EnumFacing facing = state.getValue(FACING);

        boolean up = state.getValue(UP);
        boolean down = state.getValue(DOWN);

        if (stateFrom.getValue(FACING) == facing)
        {
            if (posFrom.equals(pos.offset(facing.getOpposite()).up()))
                down = true;

            if(posFrom.equals(pos.offset(facing).up()))
                up = true;
        }

        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityICB)
        {
            TileEntityConveyor tileConveyor = (TileEntityConveyor) worldIn.getTileEntity(pos);

            if (tileConveyor != null)
            {
                tileConveyor.setSlopeUp(up);
                tileConveyor.setSlopeDown(down);
            }
        }

        worldIn.setBlockState(pos, state.withProperty(FACING, facing).withProperty(UP, up).withProperty(DOWN, down));
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront((meta & 3) + 2)).withProperty(UP, (meta & 4) > 0).withProperty(DOWN, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = (state.getValue(FACING)).getIndex() - 2;

        if (state.getValue(UP))
            meta |= 4;

        if (state.getValue(DOWN))
            meta |= 8;

        return meta;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, UP, DOWN);
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