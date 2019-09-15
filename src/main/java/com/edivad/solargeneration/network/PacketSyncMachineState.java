package com.edivad.solargeneration.network;

import com.edivad.solargeneration.Main;
import com.edivad.solargeneration.tools.inter.ISolarPanelStateContainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncMachineState implements IMessage {

	private int energy, energyProducing;

	public PacketSyncMachineState()
	{

	}

	public PacketSyncMachineState(int energy, int energyProducing)
	{
		this.energy = energy;
		this.energyProducing = energyProducing;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(energy);
		buf.writeInt(energyProducing);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		try
		{
			energy = buf.readInt();
			energyProducing = buf.readInt();
		}
		catch (IndexOutOfBoundsException ioe)
		{
			Main.logger.debug(ioe.getMessage());
			return;
		}
	}

	public static class Handler implements IMessageHandler<PacketSyncMachineState, IMessage> {

		@Override
		public IMessage onMessage(PacketSyncMachineState message, MessageContext ctx)
		{
			Main.proxy.addScheduledTaskClient(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketSyncMachineState message, MessageContext ctx)
		{
			EntityPlayer player = Main.proxy.getClientPlayer();
			if(player.openContainer instanceof ISolarPanelStateContainer)
			{
				((ISolarPanelStateContainer) player.openContainer).sync(message.energy, message.energyProducing);
			}
		}
	}
}
