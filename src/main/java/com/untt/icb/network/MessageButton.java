package com.untt.icb.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.untt.icb.gui.detector.ContainerDetector;
import com.untt.icb.gui.filter.ContainerFilter;

public class MessageButton implements IMessage, IMessageHandler<MessageButton, IMessage> {
	NBTTagCompound nbt;

	public MessageButton() {
	}

	public MessageButton(NBTTagCompound nbt) {
		this.nbt = nbt;
	}

	@Override
	public IMessage onMessage(MessageButton message, MessageContext ctx) {
		ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
			EntityPlayer player = ctx.getServerHandler().player;
			if (player.openContainer instanceof ContainerFilter) {
				((ContainerFilter) player.openContainer).tile.handleMessage(player, message.nbt);
			}else if(player.openContainer instanceof ContainerDetector){
				((ContainerDetector) player.openContainer).tile.handleMessage(player, message.nbt);
			}
		});
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
	}

}
