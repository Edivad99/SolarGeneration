package edivad.solargeneration.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import edivad.edivadlib.tools.TranslationsAdvancement;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.ModRegistration;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Translations;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.world.item.Item;

public class SolarGenerationAdvancementProvider extends AdvancementProvider {

  public SolarGenerationAdvancementProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(packOutput, registries, List.of(new Advancements()));
  }

  private static class Advancements implements AdvancementSubProvider {

    private static AdvancementType getFrameType(SolarPanelLevel level) {
      return switch (level) {
        case LEADSTONE, HARDENED, REDSTONE -> AdvancementType.TASK;
        case SIGNALUM, RESONANT -> AdvancementType.GOAL;
        case ADVANCED, ULTIMATE -> AdvancementType.CHALLENGE;
      };
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer) {
      var ROOT = Advancement.Builder.advancement()
          .display(ModRegistration.PHOTOVOLTAIC_CELL.get(),
              Translations.ADVANCEMENTS_ROOT.translateTitle(),
              Translations.ADVANCEMENTS_ROOT.translateDescription(),
              SolarGeneration.rl("textures/gui/advancements.png"),
              AdvancementType.TASK,
              true, true, false)
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.PHOTOVOLTAIC_CELL.get()))
          .save(writer, SolarGeneration.rl("root"));

      AdvancementHolder parent = ROOT;
      for (var level : SolarPanelLevel.values()) {
        var item = ModRegistration.SOLAR_PANEL_ITEM.get(level);
        var translations = Translations.SOLAR_PANEL_ADVANCEMENTS.get(level);
        parent = generateAdvancements(writer, level, parent, item.get(),
            translations, level.getSolarPanelName());
      }

      parent = ROOT;
      for (var level : SolarPanelLevel.values()) {
        var item = ModRegistration.HELMET.get(level);
        var translations = Translations.HELMET_ADVANCEMENTS.get(level);
        parent = generateAdvancements(writer, level, parent, item.get(),
            translations, level.getSolarHelmetName());
      }
    }

    private AdvancementHolder generateAdvancements(Consumer<AdvancementHolder> writer,
        SolarPanelLevel level,
        AdvancementHolder parent,
        Item item,
        TranslationsAdvancement itemTranslations,
        String name) {
      return Advancement.Builder.advancement()
          .display(item,
              itemTranslations.translateTitle(),
              itemTranslations.translateDescription(),
              null,
              getFrameType(level),
              true, true, false)
          .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(item))
          .parent(parent)
          .save(writer, SolarGeneration.rl(name));
    }
  }
}
