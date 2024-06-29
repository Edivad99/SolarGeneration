package edivad.solargeneration.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class SolarGenerationLootTableProvider extends LootTableProvider {

  public SolarGenerationLootTableProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, Set.of(), List.of(
        new LootTableProvider.SubProviderEntry(SolarGenerationBlockLoot::new, LootContextParamSets.BLOCK)
    ), lookupProvider);
  }
}
