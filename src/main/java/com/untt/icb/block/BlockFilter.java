package com.untt.icb.block;

import com.untt.icb.tileentity.TileEntityFilter;
import com.untt.icb.utility.LogHelper;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockFilter extends BlockICB implements ITileEntityProvider
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    private static final AxisAlignedBB COLLISION_NORTH = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.9F);
    private static final AxisAlignedBB COLLISION_EAST = new AxisAlignedBB(0.1F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB COLLISION_SOUTH = new AxisAlignedBB(0.0F, 0.0F, 0.1F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB COLLISION_WEST = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.9F, 1.0F, 1.0F);

    public BlockFilter(String name)
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
        return new TileEntityFilter();
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        switch (blockState.getValue(FACING))
        {
            case NORTH: return COLLISION_NORTH;
            case EAST: return COLLISION_EAST;
            case SOUTH: return COLLISION_SOUTH;
            case WEST: return COLLISION_WEST;
        }

        return super.getCollisionBoundingBox(blockState, worldIn, pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()));

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            if (!playerIn.getHeldItem(hand).isEmpty() && worldIn.getTileEntity(pos) instanceof TileEntityFilter)
            {
                TileEntityFilter tileSorter = (TileEntityFilter) worldIn.getTileEntity(pos);

                tileSorter.addFilter(playerIn.getHeldItem(hand), facing);
                
                playerIn.sendMessage(new TextComponentString("Added: "+playerIn.getHeldItem(hand).getDisplayName()));
            }
        }

        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn instanceof EntityItem)
            {
                EntityItem entityItem = (EntityItem) entityIn;

                if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityFilter)
                {
                    TileEntityFilter tileSorter = (TileEntityFilter) worldIn.getTileEntity(pos);

                    tileSorter.sortItemStack(entityItem.getEntityItem());

                    worldIn.removeEntity(entityItem);
                }
            }
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
        return (state.getValue(FACING)).getIndex() - 2;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
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
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state)
    {
        return true;
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
        return BlockRenderLayer.SOLID;
    }
}