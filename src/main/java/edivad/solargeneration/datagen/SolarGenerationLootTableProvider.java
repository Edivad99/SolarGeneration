package edivad.solargeneration.datagen;

import java.util.List;
import java.util.Set;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class SolarGenerationLootTableProvider extends LootTableProvider {

  public SolarGenerationLootTableProvider(PackOutput packOutput) {
    super(packOutput, Set.of(), List.of(
        new LootTableProvider.SubProviderEntry(SolarGenerationBlockLoot::new, LootContextParamSets.BLOCK)
    ));
  }
}
