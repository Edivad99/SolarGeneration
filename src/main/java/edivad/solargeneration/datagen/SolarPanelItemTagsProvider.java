package edivad.solargeneration.datagen;

import java.util.concurrent.CompletableFuture;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.ModRegistration;
import edivad.solargeneration.tags.SolarGenerationTags;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

public class SolarPanelItemTagsProvider extends ItemTagsProvider {

  public SolarPanelItemTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider, SolarGeneration.ID);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    for (var level : SolarPanelLevel.values()) {
      this.tag(SolarGenerationTags.Items.SOLAR_PANEL)
          .add(ModRegistration.SOLAR_PANEL_ITEM.get(level).get());
      this.tag(SolarGenerationTags.Items.SOLAR_HELMET)
          .add(ModRegistration.HELMET.get(level).get());
    }
    this.tag(Tags.Items.ARMORS)
        .addTag(SolarGenerationTags.Items.SOLAR_HELMET);
    this.tag(ItemTags.HEAD_ARMOR)
        .addTag(SolarGenerationTags.Items.SOLAR_HELMET);
  }
}
