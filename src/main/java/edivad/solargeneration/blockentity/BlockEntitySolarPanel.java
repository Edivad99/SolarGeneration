package edivad.solargeneration.blockentity;

import edivad.solargeneration.menu.SolarPanelMenu;
import edivad.solargeneration.network.PacketHandler;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelBattery;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockEntitySolarPanel extends BlockEntity implements MenuProvider {

    // Energy
    private final int energyGeneration;
    private final int maxTransfer;
    private final SolarPanelBattery solarPanelBattery;
    private LazyOptional<IEnergyStorage> energy;

    private final SolarPanelLevel levelSolarPanel;
    public int energyClient, energyProductionClient;

    public BlockEntitySolarPanel(SolarPanelLevel levelSolarPanel, BlockPos pos, BlockState state) {
        super(Registration.SOLAR_PANEL_TILE.get(levelSolarPanel).get(), pos, state);
        this.levelSolarPanel = levelSolarPanel;

        energyGeneration = levelSolarPanel.getEnergyGeneration();
        maxTransfer = levelSolarPanel.getMaxTransfer();
        int capacity = levelSolarPanel.getCapacity();

        solarPanelBattery = new SolarPanelBattery(maxTransfer, capacity);
        energy = LazyOptional.of(() -> solarPanelBattery);

        energyClient = energyProductionClient = -1;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntitySolarPanel tile) {
        int energyProducedBySun = tile.currentAmountEnergyProduced();
        tile.solarPanelBattery.generatePower(energyProducedBySun);
        tile.sendEnergy();
        int energyStored = tile.solarPanelBattery.getEnergyStored();
        if(tile.energyClient != energyStored || tile.energyProductionClient != energyProducedBySun) {
            int energyProduced = tile.solarPanelBattery.isFullEnergy() ? 0 : energyProducedBySun;
            tile.setChanged();
            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(blockPos)), new UpdateSolarPanel(blockPos, energyStored, energyProduced));
        }
    }

    private int currentAmountEnergyProduced() {
        return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(level, worldPosition, levelSolarPanel));
    }

    private void sendEnergy() {
        AtomicInteger capacity = new AtomicInteger(solarPanelBattery.getEnergyStored());

        for(int i = 0; (i < Direction.values().length) && (capacity.get() > 0); i++) {
            Direction facing = Direction.values()[i];
            if(facing != Direction.UP) {
                BlockEntity tileEntity = level.getBlockEntity(worldPosition.relative(facing));
                if(tileEntity != null) {
                    tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).ifPresent(handler -> {
                        if(handler.canReceive()) {
                            int received = handler.receiveEnergy(Math.min(capacity.get(), maxTransfer), false);
                            capacity.addAndGet(-received);
                            solarPanelBattery.consumePower(received);
                            setChanged();
                        }
                    });
                }
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
        if(capability == CapabilityEnergy.ENERGY && facing != Direction.UP) {
            return energy.cast();
        }
        return super.getCapability(capability, facing);
    }

    public SolarPanelLevel getLevelSolarPanel() {
        return levelSolarPanel;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        Tag energyTag = compound.get("energy");
        if(energyTag != null)
            solarPanelBattery.deserializeNBT(energyTag);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("energy", solarPanelBattery.serializeNBT());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new SolarPanelMenu(id, playerEntity, this, levelSolarPanel);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }
}
