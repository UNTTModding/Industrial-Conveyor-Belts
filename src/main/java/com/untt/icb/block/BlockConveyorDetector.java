package com.untt.icb.block;

import com.untt.icb.tileentity.TileEntityConveyorDetector;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

public class BlockConveyorDetector extends BlockConveyorBase implements ITileEntityProvider
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.15, 1.0);
    private static final AxisAlignedBB COLLISION = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);

    public BlockConveyorDetector(String name)
    {
        super(name);

        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
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
        return new TileEntityConveyorDetector();
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    	return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.getHorizontalFacing()).withProperty(POWERED, false);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        updateRedstoneOutput(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            if (!playerIn.getHeldItem(hand).isEmpty() && worldIn.getTileEntity(pos) instanceof TileEntityConveyorDetector)
            {
                TileEntityConveyorDetector tileDetector = (TileEntityConveyorDetector) worldIn.getTileEntity(pos);

                tileDetector.addFilter(playerIn.getHeldItem(hand));

                playerIn.sendMessage(new TextComponentString("Added FilterItem: " + playerIn.getHeldItem(hand).getDisplayName()));
            }
        }

        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

        if (!worldIn.isRemote && entityIn instanceof EntityItem)
            updateRedstoneOutput(worldIn, pos, state);
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return side.getFrontOffsetY() == 0 ? getWeakPower(state, world, pos, side) : 0;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
    {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityConveyorDetector)
        {
            TileEntityConveyorDetector tileDetector = (TileEntityConveyorDetector) world.getTileEntity(pos);

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
        if (!world.isRemote && state.getValue(POWERED))
            updateRedstoneOutput(world, pos, state);
    }

    private void updateRedstoneOutput(World world, BlockPos pos, IBlockState state)
    {
        if (!world.isRemote)
        {
            TileEntityConveyorDetector tileDetector = (TileEntityConveyorDetector) world.getTileEntity(pos);

            // Temp "Fix" for item getting stuck (Facing of TE is reset)
            if (tileDetector.getFacing() != state.getValue(FACING))
                tileDetector.setFacing(state.getValue(FACING));

            int count = tileDetector.findMatchingItems(world);
            boolean powered = state.getValue(POWERED);

            if (count > 0 && !powered)
            {
                world.setBlockState(pos, state.withProperty(POWERED, true), 1 | 2);
                world.notifyNeighborsOfStateChange(pos, this, false);
                world.notifyNeighborsOfStateChange(pos.down(), this, false);
                world.markBlockRangeForRenderUpdate(pos, pos);
            }

            else if (count == 0 && powered)
            {
                world.setBlockState(pos, state.withProperty(POWERED, false), 1 | 2);
                world.notifyNeighborsOfStateChange(pos, this, false);
                world.notifyNeighborsOfStateChange(pos.down(), this, false);
                world.markBlockRangeForRenderUpdate(pos, pos);
            }

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
        return getDefaultState().withProperty(FACING, EnumFacing.getFront((meta & 3) + 2)).withProperty(POWERED, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(FACING).getIndex() - 2;

        if (state.getValue(POWERED))
            meta |= 4;

        return meta;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, POWERED);
    }
}