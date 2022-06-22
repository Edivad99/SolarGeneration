package edivad.solargeneration.tools;

import edivad.solargeneration.Main;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.HashMap;
import java.util.Map;

public class Translations {

    public static final String TRANSFER = "message." + Main.MODID + ".transfer";
    public static final String HOLD = "message." + Main.MODID + ".hold";
    public static final String FOR_DATAILS = "message." + Main.MODID + ".for_details";
    public static final String FOR_STORED_ENERGY = "message." + Main.MODID + ".for_stored_energy";

    public static final String STORED_ENERGY = "gui." + Main.MODID + ".stored_energy";
    public static final String CAPACITY = "gui." + Main.MODID + ".capacity";
    public static final String GENERATION = "gui." + Main.MODID + ".generation";
    public static final String ENERGY = "gui." + Main.MODID + ".energy";

    public static final String ADVANCEMENTS_ROOT = "advancements." + Main.MODID + ".root";
    public static final String ADVANCEMENTS_ROOT_DESC = "advancements." + Main.MODID + ".root.desc";
    public static final Map<SolarPanelLevel, Advancement> SOLAR_PANEL_ADVANCEMENTS;
    public static final Map<SolarPanelLevel, Advancement> HELMET_ADVANCEMENTS;

    static {
        SOLAR_PANEL_ADVANCEMENTS = new HashMap<>();
        HELMET_ADVANCEMENTS = new HashMap<>();
        for(var level : SolarPanelLevel.values()) {
            var titleSolarPanel = "advancements." + Main.MODID + "." + level.getSolarPanelName();
            var titleSolarHelmet = "advancements." + Main.MODID + "." + level.getSolarHelmetName();
            SOLAR_PANEL_ADVANCEMENTS.put(level, new Advancement(titleSolarPanel, titleSolarPanel + ".desc"));
            HELMET_ADVANCEMENTS.put(level, new Advancement(titleSolarHelmet, titleSolarHelmet + ".desc"));
        }
    }

    public record Advancement(String title, String desc) {
        public MutableComponent translateTitle() {
            return Component.translatable(title);
        }

        public MutableComponent translateDescription() {
            return Component.translatable(desc);
        }
    }
}
