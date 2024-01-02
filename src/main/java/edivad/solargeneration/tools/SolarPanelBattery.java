package edivad.solargeneration.tools;

import net.neoforged.neoforge.energy.EnergyStorage;

public class SolarPanelBattery extends EnergyStorage {

  public SolarPanelBattery(int maxTransfer, int capacity) {
    super(capacity, 0, maxTransfer);
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }

  public void generatePower(int energy) {
    this.energy = Math.min(capacity, this.energy + energy);
  }

  public void consumePower(int energy) {
    this.energy = Math.max(0, this.energy - energy);
  }

  public boolean isFullEnergy() {
    return getEnergyStored() >= getMaxEnergyStored();
  }
}
