package edivad.solargeneration.datagen;

import java.util.Set;
import java.util.stream.Collectors;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SolarGenerationBlockLoot extends BlockLootSubProvider {

  public SolarGenerationBlockLoot() {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags());
  }

  private static LootTable.Builder createSolarPanelDrops(Block block) {
    var builder = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1))
        .add(LootItem.lootTableItem(block)
            .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
            .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy("energy", "energy", CopyNbtFunction.MergeStrategy.REPLACE))
            )
        .when(ExplosionCondition.survivesExplosion());

    return LootTable.lootTable().withPool(builder);
  }

  @Override
  protected void generate() {
    for (var level : SolarPanelLevel.values()) {
      this.add(Registration.SOLAR_PANEL_BLOCK.get(level).get(),
          SolarGenerationBlockLoot::createSolarPanelDrops);
    }
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return Registration.SOLAR_PANEL_BLOCK.values().stream()
        .map(DeferredHolder::get)
        .collect(Collectors.toList());
  }
}
