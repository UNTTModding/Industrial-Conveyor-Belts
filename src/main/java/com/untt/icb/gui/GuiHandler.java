package com.untt.icb.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.untt.icb.gui.detector.ContainerDetector;
import com.untt.icb.gui.detector.GuiDetector;
import com.untt.icb.gui.filter.ContainerFilter;
import com.untt.icb.gui.filter.GuiFilter;
import com.untt.icb.tileentity.TileEntityConveyorDetector;
import com.untt.icb.tileentity.TileEntityFilter;

public class GuiHandler implements IGuiHandler {

	public static final int FILTER = 1;
	public static final int DETECTOR = 2;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case FILTER:
			return new ContainerFilter((TileEntityFilter) world.getTileEntity(new BlockPos(x, y, z)), player);
		case DETECTOR:
			return new ContainerDetector((TileEntityConveyorDetector) world.getTileEntity(new BlockPos(x, y, z)), player);
		default:
			break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case FILTER:
			return new GuiFilter(new ContainerFilter((TileEntityFilter) world.getTileEntity(new BlockPos(x, y, z)), player));
		case DETECTOR:
			return new GuiDetector(new ContainerDetector((TileEntityConveyorDetector) world.getTileEntity(new BlockPos(x, y, z)), player));
		default:
			break;
		}
		return null;
	}

}
