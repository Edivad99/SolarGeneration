package edivad.solargeneration.tools;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Tooltip {

  public static void showInfoShift(SolarPanelLevel solarPanelLevel, Consumer<Component> tooltipAdder) {
    if (Minecraft.getInstance().hasShiftDown()) {
      var generation = String.valueOf(solarPanelLevel.getEnergyGeneration());
      var transfer = String.valueOf(solarPanelLevel.getMaxTransfer());
      var capacity = String.valueOf(solarPanelLevel.getCapacity());

      tooltipAdder.accept(buildLineEnergy(Translations.GENERATION, generation));
      tooltipAdder.accept(buildLineEnergy(Translations.TRANSFER, transfer));
      tooltipAdder.accept(buildLineEnergy(Translations.CAPACITY, capacity).append(" FE"));
    } else {
      tooltipAdder.accept(buildLineHoldKey("Shift", Translations.FOR_DETAILS));
    }
  }

  public static MutableComponent showInfoCtrl(int energy) {
    if (Minecraft.getInstance().hasControlDown()) {
      return buildLineEnergy(Translations.STORED_ENERGY, String.valueOf(energy)).append(" FE");
    }
    //return buildLineHoldKey(InputQuirks.ON_OSX ? "Cmd" : "Ctrl", Translations.FOR_STORED_ENERGY);
    return buildLineHoldKey("Ctrl", Translations.FOR_STORED_ENERGY);
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
