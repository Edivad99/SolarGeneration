package edivad.solargeneration.datagen;

import com.google.gson.JsonObject;
import edivad.solargeneration.Main;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public abstract class BaseAdvancementProvider implements DataProvider {

    private final DataGenerator.PathProvider pathProvider;
    private final ExistingFileHelper existingFileHelper;
    private final String modId;

    public BaseAdvancementProvider(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId) {
        this.existingFileHelper = existingFileHelper;
        this.modId = modId;
        this.pathProvider = generator.createPathProvider(DataGenerator.Target.DATA_PACK, "advancements");
    }

    @Override
    public void run(CachedOutput cache) {
        registerAdvancements(advancement -> {
            ResourceLocation id = advancement.getId();
            if (existingFileHelper.exists(id, PackType.SERVER_DATA, ".json", "advancements")) {
                throw new IllegalStateException("Duplicate advancement " + id);
            }
            Path path = this.pathProvider.json(id);
            JsonObject json = advancement.deconstruct().serializeToJson();
            existingFileHelper.trackGenerated(id, PackType.SERVER_DATA, ".json", "advancements");
            try {
                DataProvider.saveStable(cache, json, path);
            }
            catch(IOException e) {
                Main.logger.error("Couldn't save advancement {}", path, e);
            }
        });
    }

    protected abstract void registerAdvancements(@Nonnull Consumer<Advancement> consumer);

    @Override
    public String getName() {
        return "Advancements: " + modId;
    }
}
