package edivad.solargeneration.datagen;

import edivad.solargeneration.Main;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Translations;
import edivad.solargeneration.tools.TranslationsAdvancement;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AdvancementProvider extends BaseAdvancementProvider {

    private final ExistingFileHelper existingFileHelper;

    public AdvancementProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, existingFileHelper, Main.MODID);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    protected void registerAdvancements(@NotNull Consumer<Advancement> consumer) {
        var ROOT = Advancement.Builder.advancement()
                .display(Registration.PHOTOVOLTAIC_CELL.get(),
                        Translations.ADVANCEMENTS_ROOT.translateTitle(),
                        Translations.ADVANCEMENTS_ROOT.translateDescription(),
                        new ResourceLocation(Main.MODID, "textures/gui/advancements.png"),
                        FrameType.TASK,
                        true, //showToast
                        true, //announceToChat
                        false) //hidden
                .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.PHOTOVOLTAIC_CELL.get()))
                .save(consumer, new ResourceLocation(Main.MODID, "root"), existingFileHelper);

        Advancement parent = ROOT;
        for(var level: SolarPanelLevel.values()) {
            var item = Registration.SOLAR_PANEL_ITEM.get(level);
            var translations = Translations.SOLAR_PANEL_ADVANCEMENTS.get(level);
            parent = generateAdvancements(consumer, level, parent, item, translations, level.getSolarPanelName());
        }
        parent = ROOT;
        for(var level: SolarPanelLevel.values()) {
            var item = Registration.HELMET.get(level);
            var translations = Translations.HELMET_ADVANCEMENTS.get(level);
            parent = generateAdvancements(consumer, level, parent, item, translations, level.getSolarHelmetName());
        }
    }


    private Advancement generateAdvancements(@NotNull Consumer<Advancement> consumer,
            SolarPanelLevel level,
            Advancement parent,
            RegistryObject<Item> item,
            TranslationsAdvancement itemTranslations,
            String name) {

        return Advancement.Builder.advancement()
                .display(item.get(),
                        itemTranslations.translateTitle(),
                        itemTranslations.translateDescription(),
                        null,
                        getFrameType(level),
                        true, //showToast
                        true, //announceToChat
                        false) //hidden
                .addCriterion("inv_changed", InventoryChangeTrigger.TriggerInstance.hasItems(item.get()))
                .parent(parent)
                .save(consumer, new ResourceLocation(Main.MODID, name), existingFileHelper);
    }

    private static FrameType getFrameType(SolarPanelLevel level) {
        return switch(level) {
            case LEADSTONE, HARDENED, REDSTONE -> FrameType.TASK;
            case SIGNALUM, RESONANT -> FrameType.GOAL;
            case ADVANCED, ULTIMATE -> FrameType.CHALLENGE;
        };
    }
}
