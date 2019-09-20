package edivad.solargeneration.tile;

import javax.annotation.Nonnull;

import edivad.solargeneration.blocks.containers.SolarPanelAdvancedContainer;
import edivad.solargeneration.tools.MyEnergyStorage;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.inter.IRestorableTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntitySolarPanel extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IRestorableTileEntity {

	// Energy
	private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
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
	}
	
	private IEnergyStorage createEnergy()
	{
		return new MyEnergyStorage(maxEnergyOutput, energyGeneration * 1000);
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
			for(int i = 0; (i < Direction.values().length) && (energyStorage.getEnergyStored() > 0); i++)
			{
				Direction facing = Direction.values()[i];
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
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing)
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
	public void read(CompoundNBT compound)
	{
		// To save tileEntity 
		super.read(compound);
		readRestorableFromNBT(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		writeRestorableToNBT(compound);
		return compound;

	}

	@Override
	public void readRestorableFromNBT(CompoundNBT compound)
	{
		this.energyStorage.setEnergy(compound.getInt("energy"));
	}

	@Override
	public void writeRestorableToNBT(CompoundNBT compound)
	{
		compound.putInt("energy", energyStorage.getEnergyStored());
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		switch (levelSolarPanel)
		{
			case Advanced: return new SolarPanelAdvancedContainer(id, world, pos);

			default: return null;
		}
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("Solar panel " + levelSolarPanel.name());
	}
}
