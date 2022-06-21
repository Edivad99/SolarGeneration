package edivad.solargeneration.datagen;

import edivad.solargeneration.Main;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Translations;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

public class Lang extends LanguageProvider {

    private final Map<SolarPanelLevel, String> translations;

    public Lang(DataGenerator gen) {
        super(gen, Main.MODID, "en_us");
        translations = new HashMap<>();
        translations.put(SolarPanelLevel.LEADSTONE, "Leadstone");
        translations.put(SolarPanelLevel.HARDENED, "Hardened");
        translations.put(SolarPanelLevel.REDSTONE, "Redstone");
        translations.put(SolarPanelLevel.SIGNALUM, "Signalum");
        translations.put(SolarPanelLevel.RESONANT, "Resonant");
        translations.put(SolarPanelLevel.ADVANCED, "Advanced");
        translations.put(SolarPanelLevel.ULTIMATE, "Ultimate");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + Main.MODID + "_tab", Main.MODNAME);

        Registration.CORE.forEach((level, item) -> add(item.get(), String.format("%s Solar Core", translations.get(level))));
        Registration.HELMET.forEach((level, item) -> add(item.get(), String.format("%s Solar Helmet", translations.get(level))));
        Registration.SOLAR_PANEL_BLOCK.forEach((level, item) -> add(item.get(), String.format("%s Solar Panel", translations.get(level))));

        add(Registration.LAPIS_SHARD.get(), "Lapis Shard");
        add(Registration.PHOTOVOLTAIC_CELL.get(), "Photovoltaic Cell");

        add(Translations.STORED_ENERGY, "Stored energy:");
        add(Translations.CAPACITY, "Capacity:");
        add(Translations.GENERATION, "Generation:");
        add(Translations.ENERGY, "Energy:");
        add(Translations.TRANSFER, "Transfer:");
        add(Translations.FOR_DATAILS, "for details");
        add(Translations.FOR_STORED_ENERGY, "for stored energy");
        add(Translations.HOLD, "Hold");
    }
}
