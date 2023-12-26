package edivad.solargeneration.datagen;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import edivad.solargeneration.blocks.SolarPanelBlock;
import edivad.solargeneration.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LootTables extends BlockLootSubProvider {

  public LootTables() {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags());
  }

  private static LootTable.Builder createSolarPanelDrops(SolarPanelBlock block) {
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

  public static LootTableProvider create(PackOutput packOutput) {
    return new LootTableProvider(packOutput, Set.of(), List.of(
        new LootTableProvider.SubProviderEntry(LootTables::new, LootContextParamSets.BLOCK)
    ));
  }

  @Override
  protected void generate() {
    Registration.SOLAR_PANEL_BLOCK.values()
        .forEach(block -> this.add(block.get(), createSolarPanelDrops(block.get())));
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return Registration.SOLAR_PANEL_BLOCK.values().stream()
        .map(DeferredHolder::get)
        .collect(Collectors.toList());
  }
}
