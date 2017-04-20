package com.untt.icb.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotGhost extends SlotItemHandler {
	EntityPlayer player;

	public SlotGhost(NonNullList<ItemStack> stacks, int index, int xPosition, int yPosition, EntityPlayer player) {
		super(new ItemStackHandler(stacks), index, xPosition, yPosition);
		this.player = player;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		putStack(ItemStack.EMPTY);
		return ItemStack.EMPTY;
	}

	@Override
	public void putStack(ItemStack stack) {
		super.putStack(stack.isEmpty() ? stack : ItemHandlerHelper.copyStackWithSize(stack, 1));
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (player == null || player.inventory.getItemStack().isEmpty())
			return isItemValid(stack);
		putStack(stack);
		return false;
	}

}
