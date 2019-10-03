package edivad.solargeneration.tools;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class MyEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {

	public MyEnergyStorage(int energyTransfer, int energyCapacity)
	{

		super(energyCapacity, energyTransfer);
		this.maxReceive = 0;
	}

	public void setEnergy(int energy)
	{
		this.energy = energy;
	}

	public void generatePower(int energy)
	{
		this.energy += energy;
		if(this.energy > capacity)
			this.energy = capacity;
	}

	public void consumePower(int energy)
	{
		this.energy -= energy;
		if(this.energy < 0)
		{
			this.energy = 0;
		}
	}

	public boolean isFullEnergy()
	{
		return getEnergyStored() >= getMaxEnergyStored();
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("energy", getEnergyStored());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt)
	{
		setEnergy(nbt.getInt("energy"));
	}
}
