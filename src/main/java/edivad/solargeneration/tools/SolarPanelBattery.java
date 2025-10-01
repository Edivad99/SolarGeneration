package edivad.solargeneration.tools;

import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

public class SolarPanelBattery extends SimpleEnergyHandler {

  public SolarPanelBattery(int maxTransfer, int capacity) {
    super(capacity, 0, maxTransfer);
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }

  public void generatePower(int energy) {
    this.set(Math.min(capacity, this.energy + energy));
  }

  public void consumePower(int energy) {
    this.set(Math.max(0, this.energy - energy));
  }

  public boolean isFullEnergy() {
    return this.energy >= this.capacity;
  }
}
