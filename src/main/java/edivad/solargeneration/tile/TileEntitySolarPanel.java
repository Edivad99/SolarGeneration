package edivad.solargeneration.tile;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import edivad.solargeneration.blocks.containers.SolarPanelAdvancedContainer;
import edivad.solargeneration.blocks.containers.SolarPanelHardenedContainer;
import edivad.solargeneration.blocks.containers.SolarPanelLeadstoneContainer;
import edivad.solargeneration.blocks.containers.SolarPanelRedstoneContainer;
import edivad.solargeneration.blocks.containers.SolarPanelResonantContainer;
import edivad.solargeneration.blocks.containers.SolarPanelSignalumContainer;
import edivad.solargeneration.blocks.containers.SolarPanelUltimateContainer;
import edivad.solargeneration.network.PacketHandler;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import edivad.solargeneration.tools.MyEnergyStorage;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.PacketDistributor;

public class TileEntitySolarPanel extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	// Energy
	private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
	private int energyGeneration, maxEnergyOutput;
	public int maxEnergy;

	private SolarPanelLevel levelSolarPanel;
	public int energyClient, energyProductionClient;

	public TileEntitySolarPanel(SolarPanelLevel levelSolarPanel, TileEntityType<?> tileEntitySolarPanel)
	{
		super(tileEntitySolarPanel);
		this.levelSolarPanel = levelSolarPanel;
		energyGeneration = (int) Math.pow(8, levelSolarPanel.ordinal());
		maxEnergyOutput = energyGeneration * 2;
		maxEnergy = energyGeneration * 1000;
		energyClient = energyProductionClient = -1;
	}

	private IEnergyStorage createEnergy()
	{
		return new MyEnergyStorage(maxEnergyOutput, maxEnergy);
	}

	@Override
	public void tick()
	{
		if(!world.isRemote)
		{
			energy.ifPresent(e -> ((MyEnergyStorage) e).generatePower(currentAmountEnergyProduced()));
			sendEnergy();
			if(energyClient != getEnergy() || energyProductionClient != currentAmountEnergyProduced())
			{
				int energyProduced = (getEnergy() != getMaxEnergy()) ? currentAmountEnergyProduced() : 0;
				PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UpdateSolarPanel(getPos(), getEnergy(), energyProduced));
			}
		}
	}

	private int getMaxEnergy()
	{
		return getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
	}

	private int getEnergy()
	{
		return getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
	}

	private int currentAmountEnergyProduced()
	{
		return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(world, getPos(), levelSolarPanel));
	}

	private void sendEnergy()
	{
		energy.ifPresent(energy ->
		{
			AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());

			for(int i = 0; (i < Direction.values().length) && (capacity.get() > 0); i++)
			{
				Direction facing = Direction.values()[i];
				if(facing != Direction.UP)
				{
					TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
					if(tileEntity != null)
					{
						tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).ifPresent(handler ->
						{
							if(handler.canReceive())
							{
								int received = handler.receiveEnergy(Math.min(capacity.get(), maxEnergyOutput), false);
								capacity.addAndGet(-received);
								((MyEnergyStorage) energy).consumePower(received);
							}
						});
					}
				}
			}
		});
	}

	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing)
	{
		if(capability == CapabilityEnergy.ENERGY && facing != Direction.UP)
		{
			return energy.cast();
		}
		return super.getCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(CompoundNBT compound)
	{
		CompoundNBT energyTag = compound.getCompound("energy");
		energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
		super.read(compound);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		energy.ifPresent(h ->
		{
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
			compound.put("energy", tag);
		});
		return super.write(compound);
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		switch (levelSolarPanel)
		{
			case Advanced:
				return new SolarPanelAdvancedContainer(id, world, pos, playerEntity);
			case Hardened:
				return new SolarPanelHardenedContainer(id, world, pos, playerEntity);
			case Leadstone:
				return new SolarPanelLeadstoneContainer(id, world, pos, playerEntity);
			case Redstone:
				return new SolarPanelRedstoneContainer(id, world, pos, playerEntity);
			case Resonant:
				return new SolarPanelResonantContainer(id, world, pos, playerEntity);
			case Signalum:
				return new SolarPanelSignalumContainer(id, world, pos, playerEntity);
			case Ultimate:
				return new SolarPanelUltimateContainer(id, world, pos, playerEntity);
			default:
				return null;
		}
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
	}
}
