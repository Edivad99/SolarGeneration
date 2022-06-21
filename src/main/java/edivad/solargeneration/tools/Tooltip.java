package edivad.solargeneration.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class Tooltip {

    public static List<MutableComponent> showInfoShift(SolarPanelLevel solarPanelLevel) {
        List<MutableComponent> components = new ArrayList<>();
        if(Screen.hasShiftDown()) {
            var generation = String.valueOf(solarPanelLevel.getEnergyGeneration());
            var transfer = String.valueOf(solarPanelLevel.getMaxTransfer());
            var capacity = String.valueOf(solarPanelLevel.getCapacity());

            components.add(buildLineEnergy(Translations.GENERATION, generation, "FE/t"));
            components.add(buildLineEnergy(Translations.TRANSFER, transfer, "FE/t"));
            components.add(buildLineEnergy(Translations.CAPACITY, capacity, "FE"));
        } else {
            components.add(buildLineHoldKey("Shift", Translations.FOR_DATAILS));
        }
        return components;
    }

    public static MutableComponent showInfoCtrl(int energy) {
        if(Screen.hasControlDown()) {
            return buildLineEnergy(Translations.STORED_ENERGY, String.valueOf(energy), "FE");
        }
        return buildLineHoldKey("Ctrl", Translations.FOR_STORED_ENERGY);
    }

    private static MutableComponent buildLineEnergy(String translationKey, String value, String unit) {
        return Component.translatable(translationKey)
                .append(" ")
                .append(Component.literal(value).withStyle(ChatFormatting.GREEN))
                .append(" ")
                .append(Component.literal(unit).withStyle(ChatFormatting.DARK_RED));
    }

    private static MutableComponent buildLineHoldKey(String key, String reasonKey) {
        return Component.translatable(Translations.HOLD).withStyle(ChatFormatting.GRAY)
                .append(" ")
                .append(Component.literal(key).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC))
                .append(" ")
                .append(Component.translatable(reasonKey).withStyle(ChatFormatting.GRAY));
    }
}
