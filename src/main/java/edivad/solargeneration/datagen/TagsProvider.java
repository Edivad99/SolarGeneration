package edivad.solargeneration.datagen;

import edivad.solargeneration.Main;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class TagsProvider extends BlockTagsProvider {

    public TagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {

        for(SolarPanelLevel level : SolarPanelLevel.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.SOLAR_PANEL_BLOCK.get(level).get());
            tag(BlockTags.NEEDS_IRON_TOOL).add(Registration.SOLAR_PANEL_BLOCK.get(level).get());
        }
    }
}
