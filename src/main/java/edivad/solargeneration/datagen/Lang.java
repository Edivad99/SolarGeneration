package edivad.solargeneration.datagen;

import edivad.solargeneration.Main;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Translations;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

public class Lang extends LanguageProvider {

    public Lang(PackOutput packOutput) {
        super(packOutput, Main.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        var translations = new HashMap<SolarPanelLevel, String>();
        translations.put(SolarPanelLevel.LEADSTONE, "Leadstone");
        translations.put(SolarPanelLevel.HARDENED, "Hardened");
        translations.put(SolarPanelLevel.REDSTONE, "Redstone");
        translations.put(SolarPanelLevel.SIGNALUM, "Signalum");
        translations.put(SolarPanelLevel.RESONANT, "Resonant");
        translations.put(SolarPanelLevel.ADVANCED, "Advanced");
        translations.put(SolarPanelLevel.ULTIMATE, "Ultimate");

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
        add(Translations.FOR_DETAILS, "for details");
        add(Translations.FOR_STORED_ENERGY, "for stored energy");
        add(Translations.HOLD, "Hold");

        generateAdvancements();
    }

    private void generateAdvancements() {
        add(Translations.ADVANCEMENTS_ROOT.title(), Main.MODNAME);
        add(Translations.ADVANCEMENTS_ROOT.desc(), "Start your solar-power journey by creating a Photovoltaic Cell.");

        var advancementsSolarPanel = new HashMap<SolarPanelLevel, Pair<String, String>>();
        advancementsSolarPanel.put(SolarPanelLevel.LEADSTONE, Pair.of("Start with the basics", "Craft a Leadstone Solar Panel."));
        advancementsSolarPanel.put(SolarPanelLevel.HARDENED, Pair.of("1 is better than 8", "Craft a Hardened Solar Panel."));
        advancementsSolarPanel.put(SolarPanelLevel.REDSTONE, Pair.of("Not enough energy", "Craft a Redstone Solar Panel."));
        advancementsSolarPanel.put(SolarPanelLevel.SIGNALUM, Pair.of("Looking for the signal", "Craft a Signalum Solar Panel."));
        advancementsSolarPanel.put(SolarPanelLevel.RESONANT, Pair.of("Escalation of power", "Craft a Resonant Solar Panel."));
        advancementsSolarPanel.put(SolarPanelLevel.ADVANCED, Pair.of("President of energy", "Craft a Advanced Solar Panel."));
        advancementsSolarPanel.put(SolarPanelLevel.ULTIMATE, Pair.of("Oh sun, I control you!", "Craft a Ultimate Solar Panel."));

        advancementsSolarPanel.forEach((level, pair) -> {
            add(Translations.SOLAR_PANEL_ADVANCEMENTS.get(level).title(), pair.getLeft());
            add(Translations.SOLAR_PANEL_ADVANCEMENTS.get(level).desc(), pair.getRight());
        });

        var advancementsSolarHelmet = new HashMap<SolarPanelLevel, Pair<String, String>>();
        advancementsSolarHelmet.put(SolarPanelLevel.LEADSTONE, Pair.of("Using your head!", "Craft a Leadstone Solar Helmet."));
        advancementsSolarHelmet.put(SolarPanelLevel.HARDENED, Pair.of("8 helmets in 1", "Craft a Hardened Solar Helmet."));
        advancementsSolarHelmet.put(SolarPanelLevel.REDSTONE, Pair.of("Electric mind", "Craft a Redstone Solar Helmet."));
        advancementsSolarHelmet.put(SolarPanelLevel.SIGNALUM, Pair.of("Brain signals", "Craft a Signalum Solar Helmet."));
        advancementsSolarHelmet.put(SolarPanelLevel.RESONANT, Pair.of("Resonating tune", "Craft a Resonant Solar Helmet."));
        advancementsSolarHelmet.put(SolarPanelLevel.ADVANCED, Pair.of("Fancy crown", "Craft a Advanced Solar Helmet."));
        advancementsSolarHelmet.put(SolarPanelLevel.ULTIMATE, Pair.of("The Ultimate Helmet", "Craft a Ultimate Solar Helmet."));

        advancementsSolarHelmet.forEach((level, pair) -> {
            add(Translations.HELMET_ADVANCEMENTS.get(level).title(), pair.getLeft());
            add(Translations.HELMET_ADVANCEMENTS.get(level).desc(), pair.getRight());
        });
    }
}
