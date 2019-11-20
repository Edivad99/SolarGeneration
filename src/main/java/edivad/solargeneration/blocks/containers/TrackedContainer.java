package edivad.solargeneration.blocks.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import edivad.solargeneration.Main;
import edivad.solargeneration.message.IntTrackerContainerMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class TrackedContainer extends Container {

	private final List<IntReferenceHolder> intTracker;

	public TrackedContainer(ContainerType<?> type, int id) {
		super(type, id);
		intTracker = new ArrayList<>();
	}

	protected <E extends IntReferenceHolder> E addTracker(E holder) {
		intTracker.add(holder);
		return holder;
	}

	/**
	 * INTERNAL. Used by {@link IntTrackerContainerMessage}
	 * 
	 * @param message
	 */
	public final void updateValue(IntTrackerContainerMessage message) {
		intTracker.get(message.getProperty()).set(message.getValue());
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		final List<NetworkManager> networkManagers = listeners.stream() //
				.filter(listener -> listener instanceof ServerPlayerEntity) //
				.map(listener -> ((ServerPlayerEntity) listener).connection.getNetworkManager()) //
				.collect(Collectors.toList());

		IntStream.range(0, intTracker.size()) //
				.filter(index -> intTracker.get(index).isDirty()) //
				.boxed() //
				.collect(Collectors.toMap(Function.identity(), index -> intTracker.get(index))) //
				.forEach((property, holder) -> {
					Main.NETWORK.send(PacketDistributor.NMLIST.with(() -> networkManagers),	new IntTrackerContainerMessage(windowId, property, holder.get()));
				});
	}

}
