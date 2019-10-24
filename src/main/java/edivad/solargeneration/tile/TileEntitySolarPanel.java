package edivad.solargeneration.tile;

import java.util.List;

import edivad.solargeneration.blocks.containers.SolarPanelContainer;
import edivad.solargeneration.compat.waila.WailaInfoProvider;
import edivad.solargeneration.gui.SolarPanelGui;
import edivad.solargeneration.tools.MyEnergyStorage;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.inter.IGuiTile;
import edivad.solargeneration.tools.inter.IRestorableTileEntity;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySolarPanel extends TileEntity implements ITickable, IGuiTile, IRestorableTileEntity, WailaInfoProvider {

	// Energy
	private MyEnergyStorage energyStorage;
	private int energyGeneration;
	private int maxEnergyOutput;
	private int clientEnergy = -1;

	private int clientCurrentAmountEnergyProduced = -1;

	private SolarPanelLevel levelSolarPanel;

	public TileEntitySolarPanel(SolarPanelLevel levelSolarPanel)
	{
		this.levelSolarPanel = levelSolarPanel;
		energyGeneration = (int) Math.pow(8, levelSolarPanel.ordinal());
		maxEnergyOutput = energyGeneration * 2;
		energyStorage = new MyEnergyStorage(maxEnergyOutput, energyGeneration * 1000);
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public void update()
	{
		if(world.isRemote)
			return;

		if(!energyStorage.isFullEnergy())
		{
			energyStorage.generatePower(currentAmountEnergyProduced());
			this.markDirty();
		}
		sendEnergy();
	}

	public int currentAmountEnergyProduced()
	{
		if(!energyStorage.isFullEnergy())
			return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(world, getPos(), getLevelSolarPanel()));

		return 0;
	}

	private void sendEnergy()
	{
		for(int i = 0; (i < EnumFacing.values().length) && (energyStorage.getEnergyStored() > 0); i++)
		{
			EnumFacing face = EnumFacing.values()[i];

			TileEntity tileEntity = world.getTileEntity(pos.offset(face));
			if(tileEntity != null && tileEntity.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite()))
			{
				IEnergyStorage handler = tileEntity.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
				if(handler != null && handler.canReceive())
				{
					int received = handler.receiveEnergy(Math.min(energyStorage.getEnergyStored(), maxEnergyOutput), false);
					energyStorage.consumePower(received);
					this.markDirty();
				}
			}
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
		{
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		return super.getCapability(capability, facing);
	}

	// Getter/Setter Client Energy
	public int getClientEnergy()
	{
		return clientEnergy;
	}

	public void setClientEnergy(int clientEnergy)
	{
		this.clientEnergy = clientEnergy;
	}

	public int getEnergy()
	{
		return energyStorage.getEnergyStored();
	}

	public int getMaxEnergy()
	{
		return energyStorage.getMaxEnergyStored();
	}

	public int getEnergyGeneration()
	{
		return energyGeneration;
	}

	public SolarPanelLevel getLevelSolarPanel()
	{
		return this.levelSolarPanel;
	}

	public int getClientCurrentAmountEnergyProduced()
	{
		return clientCurrentAmountEnergyProduced;
	}

	public void setClientCurrentAmountEnergyProduced(int energy)
	{
		this.clientCurrentAmountEnergyProduced = energy;
	}

	@Override
	public Container createContainer(EntityPlayer player)
	{
		return new SolarPanelContainer(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiContainer createGui(EntityPlayer player)
	{
		return new SolarPanelGui(this, new SolarPanelContainer(this));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		// To save tileEntity
		super.readFromNBT(compound);
		readRestorableFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		writeRestorableToNBT(compound);
		return compound;
	}

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound)
	{
		this.energyStorage.setEnergy(compound.getInteger("energy"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound)
	{
		compound.setInteger("energy", energyStorage.getEnergyStored());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		currenttip.add(TextFormatting.GRAY + "Energy stored: " + currentAmountEnergyProduced() + " FE");
		return currenttip;
	}
}
