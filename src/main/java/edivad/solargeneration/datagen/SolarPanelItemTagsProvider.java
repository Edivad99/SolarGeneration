package edivad.solargeneration.datagen;

import java.util.concurrent.CompletableFuture;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tags.SolarGenerationTags;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SolarPanelItemTagsProvider extends ItemTagsProvider {

  public SolarPanelItemTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider,
      ExistingFileHelper fileHelper) {
    super(packOutput, lookupProvider, blockTagProvider, SolarGeneration.ID, fileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    for (var level : SolarPanelLevel.values()) {
      this.tag(SolarGenerationTags.Items.SOLAR_PANEL)
          .add(Registration.SOLAR_PANEL_ITEM.get(level).get());
      this.tag(SolarGenerationTags.Items.SOLAR_HELMET)
          .add(Registration.HELMET.get(level).get());
    }
  }
}
