package edivad.solargeneration.datagen;

import java.util.concurrent.CompletableFuture;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.ModRegistration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

public class SolarPanelBlockTagsProvider extends BlockTagsProvider {

  public SolarPanelBlockTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider, SolarGeneration.ID);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    for (var level : SolarPanelLevel.values()) {
      this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .add(ModRegistration.SOLAR_PANEL_BLOCK.get(level).get());
      this.tag(BlockTags.NEEDS_IRON_TOOL)
          .add(ModRegistration.SOLAR_PANEL_BLOCK.get(level).get());
    }
  }
}
