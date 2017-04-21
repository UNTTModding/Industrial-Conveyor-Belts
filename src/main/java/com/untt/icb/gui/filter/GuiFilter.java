package com.untt.icb.gui.filter;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import com.untt.icb.IndustrialConveyorBelts;
import com.untt.icb.block.BlockFilter;
import com.untt.icb.network.MessageButton;
import com.untt.icb.tileentity.TileEntityFilter;
import com.untt.icb.utility.FilterFilter;
import com.untt.icb.utility.ResourceHelper;

public class GuiFilter extends GuiContainer {

	TileEntityFilter tile;

	public GuiFilter(ContainerFilter inventorySlotsIn) {
		super(inventorySlotsIn);
		ySize = 200;
		xSize = 200;
		tile = ((ContainerFilter) inventorySlots).tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		//		drawRect(guiLeft, guiTop, xSize + guiLeft, ySize + guiTop, Color.CYAN.getRGB());
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(ResourceHelper.getResource("textures/gui/guifilter.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/generic_54.png"));
		this.drawTexturedModalRect(guiLeft + 20, guiTop + ySize - 81, 7, 139, 162, 76);
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 12, 7, 139, 54, 54);
		this.drawTexturedModalRect(guiLeft + 74, guiTop + 12, 7, 139, 54, 54);
		this.drawTexturedModalRect(guiLeft + 140, guiTop + 12, 7, 139, 54, 54);
		drawRect(guiLeft + 7, guiTop + 12, guiLeft + 7 + 54, guiTop + 12 + 54, 0x44FF0000);
		drawRect(guiLeft + 74, guiTop + 12, guiLeft + 74 + 54, guiTop + 12 + 54, 0x44FFFF00);
		drawRect(guiLeft + 140, guiTop + 12, guiLeft + 140 + 54, guiTop + 12 + 54, 0x440000FF);
		EnumFacing face = mc.world.getBlockState(tile.getPos()).getValue(BlockFilter.FACING);
		String left = face.rotateYCCW().getName().toUpperCase();
		fontRendererObj.drawString(left, guiLeft + 7, guiTop + 3, 0x12);
		String center = face.getOpposite().getName().toUpperCase();
		fontRendererObj.drawString(center, guiLeft + 74, guiTop + 3, 0x12);
		String right = face.rotateY().getName().toUpperCase();
		fontRendererObj.drawString(right, guiLeft + 140, guiTop + 3, 0x12);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButtonExt(1000, guiLeft + 7, guiTop + 69, 25, 14, ""));
		buttonList.add(new GuiButtonExt(1001, guiLeft + 35, guiTop + 69, 25, 14, ""));
		buttonList.add(new GuiButtonExt(1002, guiLeft + 7, guiTop + 86, 25, 14, ""));
		buttonList.add(new GuiButtonExt(1003, guiLeft + 35, guiTop + 86, 25, 14, ""));
		buttonList.add(new GuiButtonExt(1004, guiLeft + 7, guiTop + 103, 53, 14, ""));
		buttonList.add(new GuiButtonExt(2000, guiLeft + 74, guiTop + 69, 25, 14, ""));
		buttonList.add(new GuiButtonExt(2001, guiLeft + 102, guiTop + 69, 25, 14, ""));
		buttonList.add(new GuiButtonExt(2002, guiLeft + 74, guiTop + 86, 25, 14, ""));
		buttonList.add(new GuiButtonExt(2003, guiLeft + 102, guiTop + 86, 25, 14, ""));
		buttonList.add(new GuiButtonExt(2004, guiLeft + 74, guiTop + 103, 53, 14, ""));
		buttonList.add(new GuiButtonExt(3000, guiLeft + 140, guiTop + 69, 25, 14, ""));
		buttonList.add(new GuiButtonExt(3001, guiLeft + 168, guiTop + 69, 25, 14, ""));
		buttonList.add(new GuiButtonExt(3002, guiLeft + 140, guiTop + 86, 25, 14, ""));
		buttonList.add(new GuiButtonExt(3003, guiLeft + 168, guiTop + 86, 25, 14, ""));
		buttonList.add(new GuiButtonExt(3004, guiLeft + 140, guiTop + 103, 53, 14, ""));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		FilterFilter f = null;
		if (button.id >= 1000 && button.id <= 1100)
			f = tile.getLeftF();
		if (button.id >= 2000 && button.id <= 2100)
			f = tile.getCenterF();
		if (button.id >= 3000 && button.id <= 3100)
			f = tile.getRightF();
		if (f != null) {
			int lastCipher = button.id % 10;
			switch (lastCipher) {
			case 0:
				f.meta ^= true;
				break;
			case 1:
				f.nbt ^= true;
				break;
			case 2:
				f.ore ^= true;
				break;
			case 3:
				f.mod ^= true;
				break;
			case 4:
				f.white ^= true;
				break;
			default:
				break;
			}
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("l", tile.getLeftF().serializeNBT());
		nbt.setTag("c", tile.getCenterF().serializeNBT());
		nbt.setTag("r", tile.getRightF().serializeNBT());
		IndustrialConveyorBelts.networkWrapper.sendToServer(new MessageButton(nbt));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		getButton(1000).displayString = (tile.getLeftF().meta ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "MET";
		getButton(1001).displayString = (tile.getLeftF().nbt ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "NBT";
		getButton(1002).displayString = (tile.getLeftF().ore ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "MOD";
		getButton(1003).displayString = (tile.getLeftF().mod ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "ORE";
		getButton(1004).displayString = (tile.getLeftF().white ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "WHITE";
		getButton(2000).displayString = (tile.getCenterF().meta ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "MET";
		getButton(2001).displayString = (tile.getCenterF().nbt ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "NBT";
		getButton(2002).displayString = (tile.getCenterF().ore ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "MOD";
		getButton(2003).displayString = (tile.getCenterF().mod ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "ORE";
		getButton(2004).displayString = (tile.getCenterF().white ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "WHITE";
		getButton(3000).displayString = (tile.getRightF().meta ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "MET";
		getButton(3001).displayString = (tile.getRightF().nbt ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "NBT";
		getButton(3002).displayString = (tile.getRightF().ore ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "MOD";
		getButton(3003).displayString = (tile.getRightF().mod ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "ORE";
		getButton(3004).displayString = (tile.getRightF().white ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "WHITE";
	}

	private GuiButton getButton(int id) {
		return buttonList.stream().filter(b -> b.id == id).findFirst().orElse(null);
	}

}
