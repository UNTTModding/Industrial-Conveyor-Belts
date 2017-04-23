package com.untt.icb.block;

import com.untt.icb.block.unlistedproperties.UnlistedPropertyBoolean;
import com.untt.icb.tileentity.TileEntityConveyor;
import com.untt.icb.tileentity.TileEntityConveyorBase;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockConveyor extends BlockConveyorBase implements ITileEntityProvider
{
    public static final UnlistedPropertyBoolean SLOPE_UP = new UnlistedPropertyBoolean("slope_up");
    public static final UnlistedPropertyBoolean SLOPE_DOWN = new UnlistedPropertyBoolean("slope_down");
    public static final UnlistedPropertyBoolean TURN_LEFT = new UnlistedPropertyBoolean("turn_left");
    public static final UnlistedPropertyBoolean TURN_RIGHT = new UnlistedPropertyBoolean("turn_right");
    public static final UnlistedPropertyBoolean POWERED = new UnlistedPropertyBoolean("powered");

    private static final AxisAlignedBB BOUNDS_FLAT = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.15, 1.0);
    private static final AxisAlignedBB BOUNDS_SLOPED = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1, 1.0);

    private static final AxisAlignedBB COLLISION = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);

    public BlockConveyor(String name)
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
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) 
    {
        AxisAlignedBB box = getBoundingBox(blockState, worldIn, pos);
        
		    if (box.equals(BOUNDS_FLAT))
			      return COLLISION;
            
		    return box;
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
        boolean turnLeft = false;
        boolean turnRight = false;

        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityConveyor)
        {
            TileEntityConveyor tileConveyor = (TileEntityConveyor) worldIn.getTileEntity(pos);

            assert tileConveyor != null;

            // Check if conveyor is a slope
            BlockPos posFrontUp = pos.offset(facing).up();
            BlockPos posBackUp = pos.offset(facing.getOpposite()).up();

            if (worldIn.getTileEntity(posFrontUp) != null && worldIn.getTileEntity(posFrontUp) instanceof TileEntityConveyorBase)
                slopeUp = ((TileEntityConveyorBase) worldIn.getTileEntity(posFrontUp)).getFacing() == facing;

            if (worldIn.getTileEntity(posBackUp) != null && worldIn.getTileEntity(posBackUp) instanceof TileEntityConveyorBase)
                slopeDown = ((TileEntityConveyorBase) worldIn.getTileEntity(posBackUp)).getFacing() == facing;

            // Check if conveyor is a turn
            BlockPos posLeft = pos.offset(facing.rotateYCCW());
            BlockPos posRight = pos.offset(facing.rotateY());

            if (worldIn.getTileEntity(posLeft) != null && worldIn.getTileEntity(posLeft) instanceof TileEntityConveyorBase)
                turnLeft = ((TileEntityConveyorBase) worldIn.getTileEntity(posLeft)).getFacing() == facing.rotateY();

            if (worldIn.getTileEntity(posRight) != null && worldIn.getTileEntity(posRight) instanceof TileEntityConveyorBase)
                turnRight = ((TileEntityConveyorBase) worldIn.getTileEntity(posRight)).getFacing() == facing.rotateYCCW();


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
                conveyorBackDown.setSlopeUp(true);

            BlockPos posBack = pos.offset(facing.getOpposite());
            BlockPos posBackRight = pos.offset(facing.getOpposite()).offset(facing.rotateY());
            BlockPos posBackLeft = pos.offset(facing.getOpposite()).offset(facing.rotateYCCW());

            TileEntityConveyor conveyorBack = null;
            TileEntityConveyor conveyorBackRight = null;
            TileEntityConveyor conveyorBackLeft = null;

            if (worldIn.getTileEntity(posBack) != null && worldIn.getTileEntity(posBack) instanceof TileEntityConveyor)
                conveyorBack = (TileEntityConveyor) worldIn.getTileEntity(posBack);

            if (worldIn.getTileEntity(posBackRight) != null && worldIn.getTileEntity(posBackRight) instanceof TileEntityConveyor)
                conveyorBackRight = (TileEntityConveyor) worldIn.getTileEntity(posBackRight);

            if (worldIn.getTileEntity(posBackLeft) != null && worldIn.getTileEntity(posBackLeft) instanceof TileEntityConveyor)
                conveyorBackLeft = (TileEntityConveyor) worldIn.getTileEntity(posBackLeft);

            if ((conveyorBack != null && conveyorBack.getFacing() == tileConveyor.getFacing().rotateY())
                    && (conveyorBackRight == null || conveyorBackRight.getFacing() != conveyorBack.getFacing()))
            {
                conveyorBack.setFacing(conveyorBack.getFacing().rotateYCCW());
                conveyorBack.setTurnLeft(true);
            }

            if ((conveyorBack != null && conveyorBack.getFacing() == tileConveyor.getFacing().rotateYCCW())
                    && (conveyorBackLeft == null || conveyorBackLeft.getFacing() != conveyorBack.getFacing()))
            {
                conveyorBack.setFacing(conveyorBack.getFacing().rotateY());
                conveyorBack.setTurnRight(true);
            }

            // Set state
            tileConveyor.setFacing(facing);
            tileConveyor.setSlopeUp(slopeUp);
            tileConveyor.setSlopeDown(slopeDown);
            tileConveyor.setTurnLeft(turnLeft);
            tileConveyor.setTurnRight(turnRight);

            checkForTurnSwitch(worldIn, pos);
        }
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        checkForTurnSwitch(worldIn, pos);

        if (!worldIn.isRemote)
        {
            TileEntityConveyor tileConveyor = getTileEntity(worldIn, pos);

            if (tileConveyor != null && !worldIn.getBlockState(fromPos).isFullyOpaque())
            {
                if (tileConveyor.isSlopeUp() && fromPos.equals(pos.offset(tileConveyor.getFacing())))
                    tileConveyor.setSlopeUp(false);

                if (tileConveyor.isSlopeDown() && fromPos.equals(pos.offset(tileConveyor.getFacing().getOpposite())))
                    tileConveyor.setSlopeDown(false);

                worldIn.markChunkDirty(pos, tileConveyor);
            }
        }
    }

    @Override
    protected void moveEntity(Entity entity, TileEntityConveyorBase tileConveyorBase)
    {
        super.moveEntity(entity, tileConveyorBase);

        if (validEntity(entity))
        {
            if (tileConveyorBase instanceof TileEntityConveyor)
            {
                TileEntityConveyor tileConveyor = (TileEntityConveyor) tileConveyorBase;

                double factor=entity instanceof EntityLivingBase?2.2:1.;
                
                if (tileConveyor.isSlopeUp())
                {
                    EnumFacing facing = tileConveyor.getFacing();

                    double movementY = transportation_speed;

					entity.onGround = false;
					entity.fallDistance=0;
					entity.motionY += movementY * 0.7D*factor;
					entity.motionY=MathHelper.clamp(entity.motionY, 0., factor*.1);
					Vec3d next = new Vec3d(tileConveyor.getPos().offset(facing)).addVector(.5, .5, .5);
					Vec3d thiss = entity.getPositionVector();
					thiss = new Vec3d(thiss.xCoord, next.yCoord, thiss.zCoord);
					double diffXZ = thiss.distanceTo(next);
					double diffY = Math.abs(entity.posY - tileConveyor.getPos().getY());
					if (diffY > .9) {
						entity.setPosition(entity.posX, entity.posY + .25, entity.posZ);
						if(diffXZ<.9)
							entity.setPosition(entity.posX + .15 * facing.getFrontOffsetX(), entity.posY, entity.posZ + .15 * facing.getFrontOffsetZ());
					}
                }

                else if (tileConveyor.isSlopeDown())
                {
                    entity.onGround = false;
                    entity.fallDistance=0;
                    entity.motionY += 0.05D*factor;
                }
            }
        }
    }

    private void checkForTurnSwitch(World worldIn, BlockPos pos)
    {
        TileEntityConveyor tileConveyor = getTileEntity(worldIn, pos);

        EnumFacing enumfacing = tileConveyor.getFacing();
        boolean flag = shouldTurnBeSwitched(worldIn, pos);

        if (flag && !tileConveyor.isPowered() && (tileConveyor.isTurnLeft() || tileConveyor.isTurnRight()))
        {
            if (tileConveyor.isTurnLeft())
                tileConveyor.setTurnRight(true);

            else
                tileConveyor.setTurnLeft(true);

            tileConveyor.setFacing(enumfacing.getOpposite());
            tileConveyor.setPowered(true);

            worldIn.markChunkDirty(pos, tileConveyor);
            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 1 | 2);
        }

        else if (!flag && tileConveyor.isPowered() && (tileConveyor.isTurnLeft() || tileConveyor.isTurnRight()))
        {
            if (tileConveyor.isTurnLeft())
                tileConveyor.setTurnRight(true);

            else
                tileConveyor.setTurnLeft(true);

            tileConveyor.setFacing(enumfacing.getOpposite());
            tileConveyor.setPowered(false);

            worldIn.markChunkDirty(pos, tileConveyor);
            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 1 | 2);
        }
    }

    private boolean shouldTurnBeSwitched(World worldIn, BlockPos pos)
    {
        return worldIn.isBlockPowered(pos) || worldIn.isSidePowered(pos, EnumFacing.DOWN);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[] {  };
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { FACING, SLOPE_UP, SLOPE_DOWN, TURN_LEFT, TURN_RIGHT, POWERED };

        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    protected TileEntityConveyor getTileEntity(World worldIn, BlockPos pos)
    {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityConveyor)
            return (TileEntityConveyor) worldIn.getTileEntity(pos);

        return null;
    }
}