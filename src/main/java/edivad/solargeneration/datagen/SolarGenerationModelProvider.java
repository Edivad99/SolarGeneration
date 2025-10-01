package edivad.solargeneration.datagen;

import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.setup.ModRegistration;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class SolarGenerationModelProvider extends ModelProvider {

  public SolarGenerationModelProvider(PackOutput output) {
    super(output, SolarGeneration.ID);
  }

  @Override
  protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    itemModels.generateFlatItem(ModRegistration.PHOTOVOLTAIC_CELL.get(), ModelTemplates.FLAT_ITEM);
    itemModels.generateFlatItem(ModRegistration.LAPIS_SHARD.get(), ModelTemplates.FLAT_ITEM);
    ModRegistration.CORE.forEach((core, item) ->
        itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
    ModRegistration.HELMET.forEach((core, item) ->
        itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));

    ModRegistration.SOLAR_PANEL_BLOCK.forEach((level, block) -> {
      blockModels.blockStateOutput.accept(
          BlockModelGenerators.createSimpleBlock(block.get(),
              BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block.get()))));
    });
  }
}
