package edivad.solargeneration.tools;

import java.util.Locale;
import edivad.solargeneration.SolarGeneration;
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

  public String getArmorTexture() {
    return SolarGeneration.ID + ":textures/model/armor/solar_helmet_" + getCorrectName() + ".png";
  }

  public ArmorMaterial getArmorMaterial() {
    return switch (this.ordinal()) {
      case 0, 1 -> ArmorMaterials.IRON;
      case 2, 3, 4, 5 -> ArmorMaterials.DIAMOND;
      case 6 -> ArmorMaterials.NETHERITE;
      default -> throw new RuntimeException("Solar panel tier not yet implemented!");
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
