package edivad.solargeneration.network;

import java.util.function.Supplier;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.inter.ISolarPanelStateContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncMachineState {

	private int energy, energyProducing;

	public PacketSyncMachineState(int energy, int energyProducing)
	{
		this.energy = energy;
		this.energyProducing = energyProducing;
	}

	public PacketSyncMachineState(ByteBuf buf)
	{
		try
		{
			energy = buf.readInt();
			energyProducing = buf.readInt();
		}
		catch(IndexOutOfBoundsException ioe)
		{
			Main.logger.debug(ioe.getMessage());
			return;
		}
	}

	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(energy);
		buf.writeInt(energyProducing);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			EntityPlayer player = Main.proxy.getClientPlayer();
			if(player.openContainer instanceof ISolarPanelStateContainer)
			{
				((ISolarPanelStateContainer) player.openContainer).sync(energy, energyProducing);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
