package edivad.solargeneration.tools;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tooltip {

    private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

    public static void showInfoShift(SolarPanelLevel solarPanelLevel, List<Component> tooltip) {
        if(Screen.hasShiftDown()) {
            int generation = (int) Math.pow(8, solarPanelLevel.ordinal());
            int transfer = generation * 2;
            int capacity = generation * 1000;

            addInformationLocalized(tooltip, Translations.MESSSAGE_SHIFT_INFO, generation, transfer, capacity);
        }
        else
            addInformationLocalized(tooltip, Translations.MESSSAGE_HOLD_SHIFT);
    }

    public static void showInfoCtrl(int energy, List<Component> tooltip) {
        if(Screen.hasControlDown())
            addInformationLocalized(tooltip, Translations.MESSSAGE_CTRL_INFO, energy);
        else
            addInformationLocalized(tooltip, Translations.MESSSAGE_HOLD_CTRL);
    }

    private static void addInformationLocalized(List<Component> tooltip, String key, Object... parameters) {
        String translated = I18n.get(key, parameters);
        translated = COMPILE.matcher(translated).replaceAll("\u00a7");
        Arrays.stream(translated.split("\n"))
                .map(Component::literal)
                .collect(Collectors.toCollection(() -> tooltip));
    }
}
