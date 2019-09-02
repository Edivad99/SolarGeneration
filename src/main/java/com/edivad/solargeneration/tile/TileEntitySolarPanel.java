package com.edivad.solargeneration.tile;

import com.edivad.solargeneration.blocks.containers.SolarPanelContainer;
import com.edivad.solargeneration.gui.SolarPanelGui;
import com.edivad.solargeneration.tools.MyEnergyStorage;
import com.edivad.solargeneration.tools.SolarPanelLevel;
import com.edivad.solargeneration.tools.inter.IGuiTile;
import com.edivad.solargeneration.tools.inter.IRestorableTileEntity;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySolarPanel extends TileEntity implements ITickable, IGuiTile, IRestorableTileEntity {

	// Energy
	private MyEnergyStorage energyStorage;
	private int energyGeneration;
	private int maxEnergyOutput;
	private int clientEnergy = -1;

	private int clientCurrentAmountEnergyProduced = -1;

	private SolarPanelLevel levelSolarPanel;

	public TileEntitySolarPanel(SolarPanelLevel levelSolarPanel) {
		this.levelSolarPanel = levelSolarPanel;
		energyGeneration = (int) Math.pow(8, levelSolarPanel.ordinal());
		maxEnergyOutput = energyGeneration * 2;
		energyStorage = new MyEnergyStorage(maxEnergyOutput, energyGeneration * 1000);
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public void update() {

		if (!world.isRemote) {
			if (!energyStorage.isFullEnergy()) {
				energyStorage.generatePower(currentAmountEnergyProduced());

				this.markDirty();
			}
			sendEnergy();
		}
	}

	public int currentAmountEnergyProduced() {
		if (!energyStorage.isFullEnergy())
			return (int) (energyGeneration * computeSunIntensity());
		return 0;
	}

	private float computeSunIntensity() {
		float sunIntensity = 0;

		if (world.canBlockSeeSky(new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ()))) {
			float multiplicator = 1.5f;
			float displacement = 1.2f;
			// Celestial angle == 0 at zenith.
			float celestialAngleRadians = world.getCelestialAngleRadians(1.0f);
			if (celestialAngleRadians > Math.PI) {
				celestialAngleRadians = (2 * 3.141592f - celestialAngleRadians);
			}

			sunIntensity = multiplicator * MathHelper.cos(celestialAngleRadians / displacement);
			sunIntensity = Math.max(0, sunIntensity);
			sunIntensity = Math.min(1, sunIntensity);

			if (sunIntensity > 0) {
				if (getLevelSolarPanel() == SolarPanelLevel.Leadstone)
					sunIntensity = 1;

				if (world.isRaining())
					sunIntensity *= 0.4;

				if (world.isThundering())
					sunIntensity *= 0.2;
			}
		}

		return sunIntensity;
	}

	private void sendEnergy() {
		if (energyStorage.getEnergyStored() > 0) {
			for (EnumFacing facing : EnumFacing.VALUES) {
				TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
				if (tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
					IEnergyStorage handler = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
					if (handler != null && handler.canReceive()) {
						int accepted = Math.min(maxEnergyOutput, handler.receiveEnergy(energyStorage.getEnergyStored(), true));
						energyStorage.consumePower(accepted);
						handler.receiveEnergy(accepted, false);
						if (energyStorage.getEnergyStored() <= 0)
							break;
					}
				}
			}
			this.markDirty();
		}

	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		return super.getCapability(capability, facing);
	}

	// Getter/Setter Client Energy
	public int getClientEnergy() {
		return clientEnergy;
	}

	public void setClientEnergy(int clientEnergy) {
		this.clientEnergy = clientEnergy;
	}

	public int getEnergy() {
		return energyStorage.getEnergyStored();
	}

	public int getMaxEnergy() {
		return energyStorage.getMaxEnergyStored();
	}

	public int getEnergyGeneration() {
		return energyGeneration;
	}

	public SolarPanelLevel getLevelSolarPanel() {
		return this.levelSolarPanel;
	}

	public int getClientCurrentAmountEnergyProduced() {
		return clientCurrentAmountEnergyProduced;
	}

	public void setClientCurrentAmountEnergyProduced(int energy) {
		this.clientCurrentAmountEnergyProduced = energy;
	}

	@Override
	public Container createContainer(EntityPlayer player) {
		return new SolarPanelContainer(this);
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(EntityPlayer player) {
		return new SolarPanelGui(this, new SolarPanelContainer(this));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// To save tileEntity
		super.readFromNBT(compound);
		readRestorableFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		writeRestorableToNBT(compound);
		return compound;
	}

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound) {
		this.energyStorage.setEnergy(compound.getInteger("energy"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", energyStorage.getEnergyStored());
	}
}
