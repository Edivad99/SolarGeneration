package edivad.solargeneration.tile;

import javax.annotation.Nonnull;

import edivad.solargeneration.Main;
import edivad.solargeneration.blocks.containers.SolarPanelContainer;
import edivad.solargeneration.gui.SolarPanelGui;
import edivad.solargeneration.tools.MyEnergyStorage;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.inter.IGuiTile;
import edivad.solargeneration.tools.inter.IRestorableTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntitySolarPanel extends TileEntity implements ITickable, IInteractionObject, IGuiTile, IRestorableTileEntity {

	// Energy
	private MyEnergyStorage energyStorage;
	private int energyGeneration;
	private int maxEnergyOutput;

	private int clientEnergy = -1;
	private int clientCurrentAmountEnergyProduced = -1;

	private SolarPanelLevel levelSolarPanel;

	public TileEntitySolarPanel(SolarPanelLevel levelSolarPanel, TileEntityType<?> tileEntitySolarPanel)
	{
		super(tileEntitySolarPanel);
		this.levelSolarPanel = levelSolarPanel;
		energyGeneration = (int) Math.pow(8, levelSolarPanel.ordinal());
		maxEnergyOutput = energyGeneration * 2;
		energyStorage = new MyEnergyStorage(maxEnergyOutput, energyGeneration * 1000);

	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !isRemoved() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public void tick()
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
		{
			BlockPos pos = new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
			return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(world, pos, getLevelSolarPanel()));
		}

		return 0;
	}

	private void sendEnergy()
	{
		if(energyStorage.getEnergyStored() > 0)
		{
			for(int i = 0; (i < EnumFacing.values().length) && (energyStorage.getEnergyStored() > 0); i++)
			{
				EnumFacing facing = EnumFacing.values()[i];
				TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
				if(tileEntity != null)
				{
					tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).ifPresent(handler ->
					{
						if(handler.canReceive())
						{
							int accepted = Math.min(maxEnergyOutput, handler.receiveEnergy(energyStorage.getEnergyStored(), true));
							energyStorage.consumePower(accepted);
							handler.receiveEnergy(accepted, false);
						}
					});
				}
			}
			this.markDirty();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
		{
			return LazyOptional.of(() -> (T) energyStorage);
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
	public ITextComponent getName()
	{
		return new TextComponentString("Solar panel " + levelSolarPanel.name());
	}

	@Override
	public boolean hasCustomName()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITextComponent getCustomName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new SolarPanelContainer(this);
	}

	@Override
	public String getGuiID()
	{
		return Main.MODID + ":gui_" + levelSolarPanel.name().toLowerCase();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public GuiContainer createGui(EntityPlayer player)
	{
		return new SolarPanelGui(this, new SolarPanelContainer(this));
	}

	@Override
	public void read(NBTTagCompound compound)
	{
		// To save tileEntity
		super.read(compound);
		readRestorableFromNBT(compound);
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		writeRestorableToNBT(compound);
		return compound;

	}

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound)
	{
		this.energyStorage.setEnergy(compound.getInt("energy"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound)
	{
		compound.setInt("energy", energyStorage.getEnergyStored());
	}
}
