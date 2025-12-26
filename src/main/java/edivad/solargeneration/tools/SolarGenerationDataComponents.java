package edivad.solargeneration.tools;

import com.mojang.serialization.Codec;
import edivad.solargeneration.SolarGeneration;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SolarGenerationDataComponents {

  private static final DeferredRegister.DataComponents deferredRegister =
      DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, SolarGeneration.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY_COMPONENT =
      deferredRegister.registerComponentType("energy", builder ->
          builder
              .persistent(Codec.INT)
              .networkSynchronized(ByteBufCodecs.INT));
}
