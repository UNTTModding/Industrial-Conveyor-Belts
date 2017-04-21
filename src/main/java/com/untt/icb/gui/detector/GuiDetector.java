package com.untt.icb.gui.detector;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import com.untt.icb.IndustrialConveyorBelts;
import com.untt.icb.network.MessageButton;
import com.untt.icb.tileentity.TileEntityConveyorDetector;
import com.untt.icb.utility.FilterFilter;
import com.untt.icb.utility.ResourceHelper;

public class GuiDetector extends GuiContainer {

	TileEntityConveyorDetector tile;

	public GuiDetector(ContainerDetector inventorySlotsIn) {
		super(inventorySlotsIn);
		ySize = 200;
		tile = ((ContainerDetector) inventorySlots).tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(ResourceHelper.getResource("textures/gui/guidetector.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/generic_54.png"));
		this.drawTexturedModalRect(guiLeft + 7, guiTop + ySize - 81, 7, 139, 162, 76);
		this.drawTexturedModalRect(guiLeft + 61, guiTop + 12, 7, 139, 54, 54);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButtonExt(2000, guiLeft + 61, guiTop + 69, 25, 14, ""));
		buttonList.add(new GuiButtonExt(2001, guiLeft + 89, guiTop + 69, 25, 14, ""));
		buttonList.add(new GuiButtonExt(2002, guiLeft + 61, guiTop + 86, 25, 14, ""));
		buttonList.add(new GuiButtonExt(2003, guiLeft + 89, guiTop + 86, 25, 14, ""));
		buttonList.add(new GuiButtonExt(2004, guiLeft + 61, guiTop + 103, 53, 14, ""));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		FilterFilter f = tile.getCenterF();
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
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("c", tile.getCenterF().serializeNBT());
		IndustrialConveyorBelts.networkWrapper.sendToServer(new MessageButton(nbt));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		getButton(2000).displayString = (tile.getCenterF().meta ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "MET";
		getButton(2001).displayString = (tile.getCenterF().nbt ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "NBT";
		getButton(2002).displayString = (tile.getCenterF().ore ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "MOD";
		getButton(2003).displayString = (tile.getCenterF().mod ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "ORE";
		getButton(2004).displayString = (tile.getCenterF().white ? TextFormatting.GREEN.toString() : TextFormatting.GOLD.toString()) + "WHITE";
	}

	private GuiButton getButton(int id) {
		return buttonList.stream().filter(b -> b.id == id).findFirst().orElse(null);
	}

}
