package com.untt.icb.gui.filter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import com.untt.icb.gui.SlotGhost;
import com.untt.icb.tileentity.TileEntityFilter;

public class ContainerFilter extends Container {

	public TileEntityFilter tile;
	EntityPlayer player;

	public ContainerFilter(TileEntityFilter tile, EntityPlayer player) {
		super();
		this.tile = tile;
		this.player = player;
		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 13 + 8 + i1 * 18, 84 + k * 18 + 38));
			}
		}
		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(player.inventory, l, 13 + 8 + l * 18, 142 + 38));
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new SlotGhost(tile.getFilterLeft(), j + i * 3, 8 + j * 18, 13 + i * 18,player));
			}
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new SlotGhost(tile.getFilterCenter(), j + i * 3, 75 + j * 18, 13 + i * 18,player));
			}
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new SlotGhost(tile.getFilterRight(), j + i * 3, 141 + j * 18, 13 + i * 18,player));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tile != null && playerIn.getPositionVector().distanceTo(new Vec3d(tile.getPos())) < 32;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		return itemstack;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		tile.markDirty();
	}

}
