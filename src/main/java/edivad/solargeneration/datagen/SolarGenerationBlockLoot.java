package edivad.solargeneration.datagen;

import java.util.Set;
import java.util.stream.Collectors;
import edivad.solargeneration.setup.ModRegistration;
import edivad.solargeneration.tools.SolarGenerationDataComponents;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SolarGenerationBlockLoot extends BlockLootSubProvider {

  public SolarGenerationBlockLoot(HolderLookup.Provider provider) {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
  }

  private LootTable.Builder createSolarPanelDrops(Block block) {
    return LootTable.lootTable()
        .withPool(this.applyExplosionCondition(block, LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(block)
                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                    .include(SolarGenerationDataComponents.ENERGY_COMPONENT.get())
                    .include(DataComponents.CUSTOM_NAME))
                )
        ));
  }

  @Override
  protected void generate() {
    for (var level : SolarPanelLevel.values()) {
      this.add(ModRegistration.SOLAR_PANEL_BLOCK.get(level).get(), this::createSolarPanelDrops);
    }
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return ModRegistration.SOLAR_PANEL_BLOCK.values().stream()
        .map(DeferredHolder::get)
        .collect(Collectors.toList());
  }
}
