package com.untt.icb.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.untt.icb.gui.filter.ContainerFilter;

public class MessageButtonFilter implements IMessage, IMessageHandler<MessageButtonFilter, IMessage> {
	NBTTagCompound nbt;

	public MessageButtonFilter() {
	}

	public MessageButtonFilter(NBTTagCompound nbt) {
		this.nbt = nbt;
	}

	@Override
	public IMessage onMessage(MessageButtonFilter message, MessageContext ctx) {
		ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(() -> {
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			if (player.openContainer instanceof ContainerFilter) {
				((ContainerFilter) player.openContainer).tile.handleMessage(player, message.nbt);
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
