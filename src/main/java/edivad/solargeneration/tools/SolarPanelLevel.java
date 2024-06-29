package edivad.solargeneration.tools;

import java.util.Locale;
import edivad.solargeneration.SolarGeneration;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;

public enum SolarPanelLevel {

  LEADSTONE, HARDENED, REDSTONE, SIGNALUM, RESONANT, ADVANCED, ULTIMATE;

  public String getSolarPanelName() {
    return "solar_panel_" + getCorrectName();
  }

  public String getSolarHelmetName() {
    return "solar_helmet_" + getCorrectName();
  }

  public String getSolarCoreName() {
    return "solar_core_" + getCorrectName();
  }

  public ResourceLocation getArmorTexture() {
    return SolarGeneration.rl("textures/model/armor/solar_helmet_" + getCorrectName() + ".png");
  }

  public Holder<ArmorMaterial> getArmorMaterial() {
    return switch (this) {
      case LEADSTONE, HARDENED -> ArmorMaterials.IRON;
      case REDSTONE, SIGNALUM, RESONANT, ADVANCED -> ArmorMaterials.DIAMOND;
      case ULTIMATE -> ArmorMaterials.NETHERITE;
    };
  }

  public int getEnergyGeneration() {
    return (int) Math.pow(8, this.ordinal());
  }

  public int getMaxTransfer() {
    return getEnergyGeneration() * 2;
  }

  public int getCapacity() {
    return getEnergyGeneration() * 1000;
  }

  private String getCorrectName() {
    return this.name().toLowerCase(Locale.ROOT);
  }
}
