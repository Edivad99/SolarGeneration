package edivad.solargeneration.message;

import java.util.Optional;
import java.util.function.Supplier;

import edivad.solargeneration.blocks.containers.TrackedContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class IntTrackerContainerMessage {

	private final int id;
	private final int property;
	private final int value;

	public IntTrackerContainerMessage(int id, int property, int value)
	{
		this.id = id;
		this.property = property;
		this.value = value;
	}

	public static void encode(IntTrackerContainerMessage message, PacketBuffer sendBuffer)
	{
		sendBuffer.writeByte(message.id);
		sendBuffer.writeShort(message.property);
		sendBuffer.writeInt(message.value);
	}

	public static IntTrackerContainerMessage decode(PacketBuffer sendBuffer)
	{
		final int id = sendBuffer.readByte();
		final int property = sendBuffer.readShort();
		final int value = sendBuffer.readInt();
		return new IntTrackerContainerMessage(id, property, value);
	}

	public int getProperty()
	{
		return property;
	}

	public int getValue()
	{
		return value;
	}

	public static class Handler {

		public static void handle(IntTrackerContainerMessage message, Supplier<Context> contextSupplier)
		{
			final Context context = contextSupplier.get();
			context.enqueueWork(() ->
			{
				handle(message);
			});
			context.setPacketHandled(true);
		}

		@OnlyIn(Dist.CLIENT)
		private static void handle(IntTrackerContainerMessage message)
		{
			getContainer(Minecraft.getInstance().player.openContainer, message.id).ifPresent(container -> container.updateValue(message));
		}

		private static final Optional<TrackedContainer> getContainer(Container container, int id)
		{
			if(container instanceof TrackedContainer && container.windowId == id)
			{
				return Optional.of((TrackedContainer) container);
			}
			return Optional.empty();
		}
	}

}
