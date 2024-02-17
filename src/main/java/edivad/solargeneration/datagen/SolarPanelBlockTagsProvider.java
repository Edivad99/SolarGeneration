package edivad.solargeneration.datagen;

import java.util.concurrent.CompletableFuture;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SolarPanelBlockTagsProvider extends BlockTagsProvider {

  public SolarPanelBlockTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, lookupProvider, SolarGeneration.ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    for (var level : SolarPanelLevel.values()) {
      this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .add(Registration.SOLAR_PANEL_BLOCK.get(level).get());
      this.tag(BlockTags.NEEDS_IRON_TOOL)
          .add(Registration.SOLAR_PANEL_BLOCK.get(level).get());
    }
  }
}
