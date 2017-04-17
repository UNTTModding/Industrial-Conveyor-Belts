package com.untt.icb.block;

import com.untt.icb.block.unlistedproperties.UnlistedPropertyBoolean;
import com.untt.icb.block.unlistedproperties.UnlistedPropertyFacing;
import com.untt.icb.tileentity.TileEntityConveyor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockConveyor extends BlockICB implements ITileEntityProvider
{
    public static final UnlistedPropertyFacing FACING = new UnlistedPropertyFacing("facing");
    public static final UnlistedPropertyBoolean SLOPE_UP = new UnlistedPropertyBoolean("slope_up");
    public static final UnlistedPropertyBoolean SLOPE_DOWN = new UnlistedPropertyBoolean("slope_down");

    private static final AxisAlignedBB BOUNDS_FLAT = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.16F, 1.0F);
    private static final AxisAlignedBB BOUNDS_SLOPED = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);

    private static final double transportation_speed = 0.05D;

    public BlockConveyor(String name)
    {
        super(Material.CLOTH, name);

        this.setHardness(0.5F);
        this.useNeighborBrightness = true;
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
        return new TileEntityConveyor();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if (source.getTileEntity(pos) != null && source.getTileEntity(pos) instanceof TileEntityConveyor)
        {
            TileEntityConveyor tileConveyor = (TileEntityConveyor) source.getTileEntity(pos);

            if (tileConveyor.isSlopeUp() || tileConveyor.isSlopeDown())
                return BOUNDS_SLOPED;
        }

        return BOUNDS_FLAT;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        // Get rotation
        EnumFacing facing = placer.getHorizontalFacing();

        // Create default values
        boolean slopeUp = false;
        boolean slopeDown = false;

        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityConveyor)
        {
            TileEntityConveyor tileConveyor = (TileEntityConveyor) worldIn.getTileEntity(pos);

            assert tileConveyor != null;

            // Check if conveyor is a slope
            BlockPos posFrontUp = pos.offset(facing).up();
            BlockPos posBackUp = pos.offset(facing.getOpposite()).up();

            TileEntityConveyor conveyorFrontUp = null;
            TileEntityConveyor conveyorBackUp = null;

            if (worldIn.getTileEntity(posFrontUp) != null && worldIn.getTileEntity(posFrontUp) instanceof TileEntityConveyor)
                conveyorFrontUp = (TileEntityConveyor) worldIn.getTileEntity(posFrontUp);

            if (worldIn.getTileEntity(posBackUp) != null && worldIn.getTileEntity(posBackUp) instanceof TileEntityConveyor)
                conveyorBackUp = (TileEntityConveyor) worldIn.getTileEntity(posBackUp);

            if (conveyorFrontUp != null && conveyorFrontUp.getFacing() == facing)
                slopeUp = true;

            if (conveyorBackUp != null && conveyorBackUp.getFacing() == facing)
                slopeDown = true;

            // Set state
            tileConveyor.setFacing(facing);
            tileConveyor.setSlopeUP(slopeUp);
            tileConveyor.setSlopeDown(slopeDown);

            // Update relevant conveyors
            BlockPos posFrontDown = pos.offset(facing).down();
            BlockPos posBackDown = pos.offset(facing.getOpposite()).down();

            TileEntityConveyor conveyorFrontDown = null;
            TileEntityConveyor conveyorBackDown = null;

            if (worldIn.getTileEntity(posFrontDown) != null && worldIn.getTileEntity(posFrontDown) instanceof TileEntityConveyor)
                conveyorFrontDown = (TileEntityConveyor) worldIn.getTileEntity(posFrontDown);

            if (worldIn.getTileEntity(posBackDown) != null && worldIn.getTileEntity(posBackDown) instanceof TileEntityConveyor)
                conveyorBackDown = (TileEntityConveyor) worldIn.getTileEntity(posBackDown);

            if (conveyorFrontDown != null && conveyorFrontDown.getFacing() == facing)
                conveyorFrontDown.setSlopeDown(true);

            if (conveyorBackDown != null && conveyorBackDown.getFacing() == facing)
                conveyorBackDown.setSlopeUP(true);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityConveyor)
                moveEntity(entityIn, (TileEntityConveyor) worldIn.getTileEntity(pos));
        }
    }

    private void moveEntity(Entity entity, TileEntityConveyor tileConveyor)
    {
        if (entity.canBePushed() || entity instanceof EntityItem || entity instanceof EntityLiving || entity instanceof EntityXPOrb)
        {
            EnumFacing facing = tileConveyor.getFacing();
            boolean slopeUp = tileConveyor.isSlopeUp();
            boolean slopeDown = tileConveyor.isSlopeDown();

            entity.isAirBorne = true;

            double movementX = transportation_speed * facing.getFrontOffsetX();
            double movementZ = transportation_speed * facing.getFrontOffsetZ();
            double movementY = 0.0D;

            if (slopeUp || slopeDown)
            {
                entity.onGround = false;

                movementY = transportation_speed * facing.getFrontOffsetY();
            }

            entity.setPosition(entity.posX + movementX, entity.posY + movementY, entity.posZ + movementZ);

            if (entity instanceof EntityItem)
                ((EntityItem) entity).setAgeToCreativeDespawnTime();
        }
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[] {  };
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { FACING, SLOPE_UP, SLOPE_DOWN };

        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    @Nonnull
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos)
    {
        IExtendedBlockState extendedState = (IExtendedBlockState) state;

        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityConveyor)
        {
            TileEntityConveyor tileConveyor = (TileEntityConveyor) tile;

            return tileConveyor.writeExtendedBlockState(extendedState);
        }

        return super.getExtendedState(state, world, pos);
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