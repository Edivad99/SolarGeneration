package edivad.solargeneration.tools;

import java.util.Locale;

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
