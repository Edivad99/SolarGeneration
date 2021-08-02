package edivad.solargeneration.tools;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;
import java.util.regex.Pattern;

public class Tooltip {

    private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

    public static void showInfoShift(SolarPanelLevel solarPanelLevel, List<Component> tooltip) {
        if(Screen.hasShiftDown()) {
            int generation = (int) Math.pow(8, solarPanelLevel.ordinal());
            int transfer = generation * 2;
            int capacity = generation * 1000;

            addInformationLocalized(tooltip, "message.solargeneration.shift_info", generation, transfer, capacity);
        }
        else
            addInformationLocalized(tooltip, "message.solargeneration.hold_shift");
    }

    public static void showInfoCtrl(int energy, List<Component> tooltip) {
        if(Screen.hasControlDown())
            addInformationLocalized(tooltip, "message.solargeneration.ctrl_info", energy);
        else
            addInformationLocalized(tooltip, "message.solargeneration.hold_ctrl");
    }

    private static void addInformationLocalized(List<Component> tooltip, String key, Object... parameters) {
        String translated = I18n.get(key, parameters);
        translated = COMPILE.matcher(translated).replaceAll("\u00a7");
        String[] formatted = translated.split("\n");
        for(String line : formatted)
            tooltip.add(new TranslatableComponent(line));
    }
}
