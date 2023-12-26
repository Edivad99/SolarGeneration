package edivad.solargeneration.datagen;

import java.util.concurrent.CompletableFuture;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tags.SolarGenerationTags;
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
    Registration.SOLAR_PANEL_ITEM.forEach((solarPanelLevel, itemRegistryObject) -> {
      this.tag(SolarGenerationTags.Items.SOLAR_PANEL)
          .add(itemRegistryObject.get());
    });
    Registration.HELMET.forEach((solarPanelLevel, itemRegistryObject) -> {
      this.tag(SolarGenerationTags.Items.SOLAR_HELMET)
          .add(itemRegistryObject.get());
    });
  }

  @Override
  public String getName() {
    return SolarGeneration.MODNAME + " Item Tag";
  }
}
