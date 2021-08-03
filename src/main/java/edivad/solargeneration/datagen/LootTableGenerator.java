package edivad.solargeneration.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import edivad.solargeneration.lootable.SolarPanelLootFunction;
import edivad.solargeneration.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LootTableGenerator extends LootTableProvider {

    public LootTableGenerator(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(SGBlockLootTables::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
    }

    @Override
    public String getName() {
        return "Solar Generation Loot Tables";
    }

    private static class SGBlockLootTables extends BlockLoot {

        @Override
        protected void addTables() {
            Registration.SOLAR_PANEL_BLOCK.values().forEach(block -> genBlockItemLootTableWithFunction(block.get(), SolarPanelLootFunction.builder()));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registration.SOLAR_PANEL_BLOCK.values().stream().map(RegistryObject::get).collect(Collectors.toList());
        }

        private void genBlockItemLootTableWithFunction(Block block, LootItemFunction.Builder function) {
            /*registerLootTable(block, LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(block).apply())
                            //        .acceptFunction(builder))
                            .acceptCondition(SurvivesExplosion.builder())));*/

            LootPool.Builder builder = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(function)).when(ExplosionCondition.survivesExplosion());

            add(block, LootTable.lootTable().withPool(builder));
        }
    }
}
