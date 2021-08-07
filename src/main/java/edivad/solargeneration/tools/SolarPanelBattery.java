package edivad.solargeneration.tools;

import net.minecraftforge.energy.EnergyStorage;

public class SolarPanelBattery extends EnergyStorage {

    public SolarPanelBattery(int maxTransfer, int capacity) {
        super(capacity, maxTransfer);
        this.maxReceive = 0;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void generatePower(int energy) {
        this.energy += energy;
        if(this.energy > capacity)
            this.energy = capacity;
    }

    public void consumePower(int energy) {
        this.energy -= energy;
        if(this.energy < 0) {
            this.energy = 0;
        }
    }

    public boolean isFullEnergy() {
        return getEnergyStored() >= getMaxEnergyStored();
    }
}
