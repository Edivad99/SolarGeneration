package edivad.solargeneration.network;

import edivad.solargeneration.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Messages {

	public static SimpleChannel INSTANCE;

	private static int ID = 0;

	private static int nextID()
	{
		return ID++;
	}

	public static void registerMessages(String channelName)
	{
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MODID, channelName), () -> "1.0", s -> true, s -> true);

		// Client side
		INSTANCE.registerMessage(nextID(), PacketSyncMachineState.class, PacketSyncMachineState::toBytes, PacketSyncMachineState::new, PacketSyncMachineState::handle);
	}
}