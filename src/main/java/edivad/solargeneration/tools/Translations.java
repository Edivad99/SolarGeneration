package edivad.solargeneration.tools;

import java.util.HashMap;
import java.util.Map;
import edivad.edivadlib.tools.TranslationsAdvancement;
import edivad.solargeneration.SolarGeneration;

public class Translations {

  public static final String TRANSFER = "message." + SolarGeneration.ID + ".transfer";
  public static final String HOLD = "message." + SolarGeneration.ID + ".hold";
  public static final String FOR_DETAILS = "message." + SolarGeneration.ID + ".for_details";
  public static final String FOR_STORED_ENERGY =
      "message." + SolarGeneration.ID + ".for_stored_energy";

  public static final String STORED_ENERGY = "gui." + SolarGeneration.ID + ".stored_energy";
  public static final String CAPACITY = "gui." + SolarGeneration.ID + ".capacity";
  public static final String GENERATION = "gui." + SolarGeneration.ID + ".generation";
  public static final String ENERGY = "gui." + SolarGeneration.ID + ".energy";

  public static final TranslationsAdvancement ADVANCEMENTS_ROOT;
  public static final Map<SolarPanelLevel, TranslationsAdvancement> SOLAR_PANEL_ADVANCEMENTS;
  public static final Map<SolarPanelLevel, TranslationsAdvancement> HELMET_ADVANCEMENTS;

  static {
    SOLAR_PANEL_ADVANCEMENTS = new HashMap<>();
    HELMET_ADVANCEMENTS = new HashMap<>();

    ADVANCEMENTS_ROOT = new TranslationsAdvancement(SolarGeneration.ID, "root");
    for (var level : SolarPanelLevel.values()) {
      SOLAR_PANEL_ADVANCEMENTS.put(level,
          new TranslationsAdvancement(SolarGeneration.ID, level.getSolarPanelName()));
      HELMET_ADVANCEMENTS.put(level,
          new TranslationsAdvancement(SolarGeneration.ID, level.getSolarHelmetName()));
    }
  }
}
