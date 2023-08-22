package edivad.solargeneration.tools;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Tooltip {

  public static List<MutableComponent> showInfoShift(SolarPanelLevel solarPanelLevel) {
    List<MutableComponent> components = new ArrayList<>();
    if (Screen.hasShiftDown()) {
      var generation = String.valueOf(solarPanelLevel.getEnergyGeneration());
      var transfer = String.valueOf(solarPanelLevel.getMaxTransfer());
      var capacity = String.valueOf(solarPanelLevel.getCapacity());

      components.add(buildLineEnergy(Translations.GENERATION, generation));
      components.add(buildLineEnergy(Translations.TRANSFER, transfer));
      components.add(buildLineEnergy(Translations.CAPACITY, capacity).append(" FE"));
    } else {
      components.add(buildLineHoldKey("Shift", Translations.FOR_DETAILS));
    }
    return components;
  }

  public static MutableComponent showInfoCtrl(int energy) {
    if (Screen.hasControlDown()) {
      return buildLineEnergy(Translations.STORED_ENERGY, String.valueOf(energy)).append(" FE");
    }
    return buildLineHoldKey(Minecraft.ON_OSX ? "Cmd" : "Ctrl", Translations.FOR_STORED_ENERGY);
  }

  private static MutableComponent buildLineEnergy(String translationKey, String value) {
    return Component.translatable(translationKey, value).withStyle(ChatFormatting.GRAY);
  }

  private static MutableComponent buildLineHoldKey(String key, String reasonKey) {
    return Component.translatable(Translations.HOLD).withStyle(ChatFormatting.GRAY)
        .append(" ")
        .append(Component.literal(key).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC))
        .append(" ")
        .append(Component.translatable(reasonKey).withStyle(ChatFormatting.GRAY));
  }
}
