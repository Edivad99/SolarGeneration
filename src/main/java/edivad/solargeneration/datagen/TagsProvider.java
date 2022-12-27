package edivad.solargeneration.datagen;

import edivad.solargeneration.Main;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TagsProvider extends BlockTagsProvider {

    public TagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for(SolarPanelLevel level : SolarPanelLevel.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(Registration.SOLAR_PANEL_BLOCK.get(level).get());
            this.tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(Registration.SOLAR_PANEL_BLOCK.get(level).get());
        }
    }

    @Override
    public String getName() {
        return Main.MODNAME + " Block Tag";
    }
}
