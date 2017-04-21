package com.untt.icb.gui.detector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import com.untt.icb.gui.SlotGhost;
import com.untt.icb.tileentity.TileEntityConveyorDetector;

public class ContainerDetector extends Container {

	public TileEntityConveyorDetector tile;
	EntityPlayer player;

	public ContainerDetector(TileEntityConveyorDetector tile, EntityPlayer player) {
		super();
		this.tile = tile;
		this.player = player;
		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18 + 36));
			}
		}
		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142 + 36));
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new SlotGhost(tile.getFilter(), j + i * 3, 62 + j * 18, 13 + i * 18, player));
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
