package edivad.solargeneration.tile;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import edivad.solargeneration.container.SolarPanelContainer;
import edivad.solargeneration.network.PacketHandler;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.MyEnergyStorage;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class TileEntitySolarPanel extends BlockEntity implements MenuProvider {

    // Energy
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private int energyGeneration, maxEnergyOutput;
    public int maxEnergy;

    private SolarPanelLevel levelSolarPanel;
    public int energyClient, energyProductionClient;

    public TileEntitySolarPanel(SolarPanelLevel levelSolarPanel, BlockPos pos, BlockState state)
    {
        super(Registration.SOLAR_PANEL_TILE.get(levelSolarPanel).get(), pos, state);
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

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileEntitySolarPanel tile)
    {
        tile.energy.ifPresent(e -> ((MyEnergyStorage) e).generatePower(tile.currentAmountEnergyProduced()));
        tile.sendEnergy();
        if(tile.energyClient != tile.getEnergy() || tile.energyProductionClient != tile.currentAmountEnergyProduced())
        {
            int energyProduced = (tile.getEnergy() != tile.getMaxEnergy()) ? tile.currentAmountEnergyProduced() : 0;
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UpdateSolarPanel(tile.getBlockPos(), tile.getEnergy(), energyProduced));
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
        return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(level, worldPosition, levelSolarPanel));
    }

    private void sendEnergy()
    {
        energy.ifPresent(energy -> {
            AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());

            for(int i = 0; (i < Direction.values().length) && (capacity.get() > 0); i++)
            {
                Direction facing = Direction.values()[i];
                if(facing != Direction.UP)
                {
                    BlockEntity tileEntity = level.getBlockEntity(worldPosition.relative(facing));
                    if(tileEntity != null)
                    {
                        tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).ifPresent(handler -> {
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

    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        Tag energyTag = compound.get("energy");
        energy.ifPresent(h -> ((EnergyStorage)h).deserializeNBT(energyTag));
    }

    @Override
    public CompoundTag save(CompoundTag compound)
    {
        energy.ifPresent(h -> {
            Tag tag = ((EnergyStorage)h).serializeNBT();
            compound.put("energy", tag);
        });
        return super.save(compound);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity)
    {
        return new SolarPanelContainer(id, playerEntity, this, levelSolarPanel);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }


}
