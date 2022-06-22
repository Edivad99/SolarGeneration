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

        generateAdvancements();
    }

    private void generateAdvancements() {
        add(Translations.ADVANCEMENTS_ROOT, Main.MODNAME);
        add(Translations.ADVANCEMENTS_ROOT_DESC, "Start your solar-power journey by creating a Photovoltaic Cell.");

        var advancementsSolarPanel = new HashMap<SolarPanelLevel, String[]>();
        advancementsSolarPanel.put(SolarPanelLevel.LEADSTONE, new String[] { "Start with the basics", "Craft a Leadstone Solar Panel." });
        advancementsSolarPanel.put(SolarPanelLevel.HARDENED, new String[] { "1 is better than 8", "Craft a Hardened Solar Panel." });
        advancementsSolarPanel.put(SolarPanelLevel.REDSTONE, new String[] { "Not enough energy", "Craft a Redstone Solar Panel." });
        advancementsSolarPanel.put(SolarPanelLevel.SIGNALUM, new String[] { "Looking for the signal", "Craft a Signalum Solar Panel." });
        advancementsSolarPanel.put(SolarPanelLevel.RESONANT, new String[] { "Escalation of power", "Craft a Resonant Solar Panel." });
        advancementsSolarPanel.put(SolarPanelLevel.ADVANCED, new String[] { "President of energy", "Craft a Advanced Solar Panel." });
        advancementsSolarPanel.put(SolarPanelLevel.ULTIMATE, new String[] { "Oh sun, I control you!", "Craft a Ultimate Solar Panel." });

        advancementsSolarPanel.forEach((l, t) -> {
            add(Translations.SOLAR_PANEL_ADVANCEMENTS.get(l).title(), t[0]);
            add(Translations.SOLAR_PANEL_ADVANCEMENTS.get(l).desc(), t[1]);
        });

        var advancementsSolarHelmet = new HashMap<SolarPanelLevel, String[]>();
        advancementsSolarHelmet.put(SolarPanelLevel.LEADSTONE, new String[] { "Using your head!", "Craft a Leadstone Solar Helmet." });
        advancementsSolarHelmet.put(SolarPanelLevel.HARDENED, new String[] { "8 helmets in 1", "Craft a Hardened Solar Helmet." });
        advancementsSolarHelmet.put(SolarPanelLevel.REDSTONE, new String[] { "Electric mind", "Craft a Redstone Solar Helmet." });
        advancementsSolarHelmet.put(SolarPanelLevel.SIGNALUM, new String[] { "Brain signals", "Craft a Signalum Solar Helmet." });
        advancementsSolarHelmet.put(SolarPanelLevel.RESONANT, new String[] { "Resonating tune", "Craft a Resonant Solar Helmet." });
        advancementsSolarHelmet.put(SolarPanelLevel.ADVANCED, new String[] { "Fancy crown", "Craft a Advanced Solar Helmet." });
        advancementsSolarHelmet.put(SolarPanelLevel.ULTIMATE, new String[] { "The Ultimate Helmet", "Craft a Ultimate Solar Helmet." });

        advancementsSolarHelmet.forEach((l, t) -> {
            add(Translations.SOLAR_HELMET_ADVANCEMENTS.get(l).title(), t[0]);
            add(Translations.SOLAR_HELMET_ADVANCEMENTS.get(l).desc(), t[1]);
        });
    }
}
