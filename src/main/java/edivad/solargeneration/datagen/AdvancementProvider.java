package edivad.solargeneration.datagen;

import edivad.edivadlib.tools.TranslationsAdvancement;
import edivad.solargeneration.Main;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Translations;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.RegistryObject;

public class AdvancementProvider extends ForgeAdvancementProvider {

    public AdvancementProvider(PackOutput packOutput,
                               CompletableFuture<HolderLookup.Provider> registries,
                               ExistingFileHelper existingFileHelper) {
        super(packOutput, registries, existingFileHelper, List.of(new Advancements()));
    }

    private static class Advancements implements ForgeAdvancementProvider.AdvancementGenerator {

        private static FrameType getFrameType(SolarPanelLevel level) {
            return switch (level) {
                case LEADSTONE, HARDENED, REDSTONE -> FrameType.TASK;
                case SIGNALUM, RESONANT -> FrameType.GOAL;
                case ADVANCED, ULTIMATE -> FrameType.CHALLENGE;
            };
        }

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer,
                             ExistingFileHelper existingFileHelper) {
            var ROOT = Advancement.Builder.advancement()
                    .display(Registration.PHOTOVOLTAIC_CELL.get(),
                            Translations.ADVANCEMENTS_ROOT.translateTitle(),
                            Translations.ADVANCEMENTS_ROOT.translateDescription(),
                            new ResourceLocation(Main.MODID, "textures/gui/advancements.png"),
                            FrameType.TASK,
                            true, true, false)
                    .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.PHOTOVOLTAIC_CELL.get()))
                    .save(consumer, new ResourceLocation(Main.MODID, "root"), existingFileHelper);

            Advancement parent = ROOT;
            for (var level : SolarPanelLevel.values()) {
                var item = Registration.SOLAR_PANEL_ITEM.get(level);
                var translations = Translations.SOLAR_PANEL_ADVANCEMENTS.get(level);
                parent = generateAdvancements(consumer, existingFileHelper, level, parent, item, translations, level.getSolarPanelName());
            }
            parent = ROOT;
            for (var level : SolarPanelLevel.values()) {
                var item = Registration.HELMET.get(level);
                var translations = Translations.HELMET_ADVANCEMENTS.get(level);
                parent = generateAdvancements(consumer, existingFileHelper, level, parent, item, translations, level.getSolarHelmetName());
            }
        }

        private Advancement generateAdvancements(Consumer<Advancement> consumer,
                                                 ExistingFileHelper existingFileHelper,
                                                 SolarPanelLevel level,
                                                 Advancement parent,
                                                 RegistryObject<Item> item,
                                                 TranslationsAdvancement itemTranslations,
                                                 String name) {
            return Advancement.Builder.advancement()
                    .display(item.get(),
                            itemTranslations.translateTitle(),
                            itemTranslations.translateDescription(),
                            null,
                            getFrameType(level),
                            true, true, false)
                    .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(item.get()))
                    .parent(parent)
                    .save(consumer, new ResourceLocation(Main.MODID, name), existingFileHelper);
        }


    }
}
