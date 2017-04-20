package com.untt.icb.gui;

import com.untt.icb.gui.filter.ContainerFilter;
import com.untt.icb.gui.filter.GuiFilter;
import com.untt.icb.tileentity.TileEntityFilter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{
	
	public static final int FILTER=1;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case FILTER:
			return new ContainerFilter((TileEntityFilter) world.getTileEntity(new BlockPos(x, y, z)),player );
		default:
			break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case FILTER:
			return new GuiFilter(new ContainerFilter((TileEntityFilter) world.getTileEntity(new BlockPos(x, y, z)),player));
		default:
			break;
		}
		return null;
	}
	
	

}
