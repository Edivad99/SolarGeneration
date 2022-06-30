package edivad.solargeneration.tools;

import edivad.edivadlib.tools.TranslationsAdvancement;
import edivad.solargeneration.Main;

import java.util.HashMap;
import java.util.Map;

public class Translations {

    public static final String TRANSFER = "message." + Main.MODID + ".transfer";
    public static final String HOLD = "message." + Main.MODID + ".hold";
    public static final String FOR_DETAILS = "message." + Main.MODID + ".for_details";
    public static final String FOR_STORED_ENERGY = "message." + Main.MODID + ".for_stored_energy";

    public static final String STORED_ENERGY = "gui." + Main.MODID + ".stored_energy";
    public static final String CAPACITY = "gui." + Main.MODID + ".capacity";
    public static final String GENERATION = "gui." + Main.MODID + ".generation";
    public static final String ENERGY = "gui." + Main.MODID + ".energy";

    public static final TranslationsAdvancement ADVANCEMENTS_ROOT;
    public static final Map<SolarPanelLevel, TranslationsAdvancement> SOLAR_PANEL_ADVANCEMENTS;
    public static final Map<SolarPanelLevel, TranslationsAdvancement> HELMET_ADVANCEMENTS;

    static {
        SOLAR_PANEL_ADVANCEMENTS = new HashMap<>();
        HELMET_ADVANCEMENTS = new HashMap<>();

        ADVANCEMENTS_ROOT = new TranslationsAdvancement(Main.MODID, "root");
        for(var level : SolarPanelLevel.values()) {
            SOLAR_PANEL_ADVANCEMENTS.put(level, new TranslationsAdvancement(Main.MODID, level.getSolarPanelName()));
            HELMET_ADVANCEMENTS.put(level, new TranslationsAdvancement(Main.MODID, level.getSolarHelmetName()));
        }
    }
}
