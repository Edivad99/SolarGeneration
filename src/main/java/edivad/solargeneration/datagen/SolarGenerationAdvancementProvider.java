package edivad.solargeneration.datagen;

import java.util.List;
import java.util.Optional;
import edivad.edivadlib.tools.TranslationsAdvancement;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.ModRegistration;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Translations;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

public class SolarGenerationAdvancementProvider {

  public static SingleRegistryBootstrap<Advancement> create() {
    return new AdvancementProvider(List.of(Advancements::new));
  }

  private static class Advancements extends AdvancementSubProvider {

    private Advancements(BootstrapContext<Advancement> output) {
      super(output);
    }

    private static AdvancementType getFrameType(SolarPanelLevel level) {
      return switch (level) {
        case LEADSTONE, HARDENED, REDSTONE -> AdvancementType.TASK;
        case SIGNALUM, RESONANT -> AdvancementType.GOAL;
        case ADVANCED, ULTIMATE -> AdvancementType.CHALLENGE;
      };
    }

    @Override
    public void generate() {
      var ROOT = Advancement.Builder.advancement()
          .display(new DisplayInfo(new ItemStackTemplate(ModRegistration.PHOTOVOLTAIC_CELL.get()),
              Translations.ADVANCEMENTS_ROOT.translateTitle(),
              Translations.ADVANCEMENTS_ROOT.translateDescription(),
              Optional.of(new ClientAsset.ResourceTexture(SolarGeneration.id("textures/gui/advancements.png"))),
              AdvancementType.TASK,
              true, true, false))
          .addCriterion("inv_changed",
              InventoryChangeTrigger.TriggerInstance.hasItems(ModRegistration.PHOTOVOLTAIC_CELL.get()))
          .save(output, SolarGeneration.id("root").toString());

      AdvancementHolder parent = ROOT;
      for (var level : SolarPanelLevel.values()) {
        var item = ModRegistration.SOLAR_PANEL_ITEM.get(level);
        var translations = Translations.SOLAR_PANEL_ADVANCEMENTS.get(level);
        parent = generateAdvancements(level, parent, item.get(),
            translations, level.getSolarPanelName());
      }

      parent = ROOT;
      for (var level : SolarPanelLevel.values()) {
        var item = ModRegistration.HELMET.get(level);
        var translations = Translations.HELMET_ADVANCEMENTS.get(level);
        parent = generateAdvancements(level, parent, item.get(),
            translations, level.getSolarHelmetName());
      }
    }

    private AdvancementHolder generateAdvancements(SolarPanelLevel level,
        AdvancementHolder parent,
        Item item,
        TranslationsAdvancement itemTranslations,
        String name) {
      return Advancement.Builder.advancement()
          .display(item,
              itemTranslations.translateTitle(),
              itemTranslations.translateDescription(),
              getFrameType(level),
              true, true, false)
          .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(item))
          .parent(parent)
          .save(output, SolarGeneration.id(name).toString());
    }
  }
}
