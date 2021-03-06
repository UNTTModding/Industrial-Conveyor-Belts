package com.untt.icb.utility;

import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.oredict.OreDictionary;

public class Filter implements INBTSerializable<NBTTagCompound> {
	public boolean meta = true, nbt = false, ore = false, mod = false, white = true;

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("meta", meta);
		tag.setBoolean("nbt", nbt);
		tag.setBoolean("ore", ore);
		tag.setBoolean("mod", mod);
		tag.setBoolean("white", white);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		meta = tag.getBoolean("meta");
		nbt = tag.getBoolean("nbt");
		ore = tag.getBoolean("ore");
		mod = tag.getBoolean("mod");
		white = tag.getBoolean("white");
	}

	public boolean match(ItemStack a, ItemStack b) {
		if (a.isEmpty() || b.isEmpty())
			return false;
		boolean met = !meta || a.getItemDamage() == b.getItemDamage();
		boolean nb = !nbt || ItemStack.areItemStackTagsEqual(a, b);
		boolean or = ore && IntStream.of(OreDictionary.getOreIDs(a)).anyMatch(i -> IntStream.of(OreDictionary.getOreIDs(b)).anyMatch(j -> j == i));
		boolean mo = mod && a.getItem().getRegistryName().getResourceDomain().equals(b.getItem().getRegistryName().getResourceDomain());
		return (a.getItem() == b.getItem() || or || mo) && met && nb;
	}

}
