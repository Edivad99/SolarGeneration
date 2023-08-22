package edivad.solargeneration.blockentity;

import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;

public class SolarPanelBlockEntity extends BlockEntity implements MenuProvider {

  private final int energyGeneration;
  private final int maxTransfer;
  private final SolarPanelBattery solarPanelBattery;
  private final LazyOptional<IEnergyStorage> energy;

  private final SolarPanelLevel levelSolarPanel;
  public int energyClient, energyProductionClient;

  public SolarPanelBlockEntity(SolarPanelLevel levelSolarPanel, BlockPos pos, BlockState state) {
    super(Registration.SOLAR_PANEL_TILE.get(levelSolarPanel).get(), pos, state);
    this.levelSolarPanel = levelSolarPanel;

    energyGeneration = levelSolarPanel.getEnergyGeneration();
    maxTransfer = levelSolarPanel.getMaxTransfer();
    int capacity = levelSolarPanel.getCapacity();

    solarPanelBattery = new SolarPanelBattery(maxTransfer, capacity);
    energy = LazyOptional.of(() -> solarPanelBattery);

    energyClient = energyProductionClient = -1;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SolarPanelBlockEntity solarPanel) {
    int energyProducedBySun = solarPanel.currentAmountEnergyProduced();
    solarPanel.solarPanelBattery.generatePower(energyProducedBySun);
    solarPanel.sendEnergy();
    int energyStored = solarPanel.solarPanelBattery.getEnergyStored();
    if (solarPanel.energyClient != energyStored
        || solarPanel.energyProductionClient != energyProducedBySun) {
      int energyProduced = solarPanel.solarPanelBattery.isFullEnergy() ? 0 : energyProducedBySun;
      solarPanel.setChanged();
      var message = new UpdateSolarPanel(blockPos, energyStored, energyProduced);
      PacketHandler.INSTANCE.send(
          PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(blockPos)), message);
    }
  }

  private int currentAmountEnergyProduced() {
    return (int) (energyGeneration *
        ProductionSolarPanel.computeSunIntensity(level, worldPosition, levelSolarPanel));
  }

  private void sendEnergy() {
    AtomicInteger capacity = new AtomicInteger(solarPanelBattery.getEnergyStored());

    for (int i = 0; (i < Direction.values().length) && (capacity.get() > 0); i++) {
      var facing = Direction.values()[i];
      if (facing.equals(Direction.UP)) {
        continue;
      }

      var blockEntity = level.getBlockEntity(worldPosition.relative(facing));
      if (blockEntity == null) {
        continue;
      }
      blockEntity.getCapability(ForgeCapabilities.ENERGY, facing.getOpposite())
          .ifPresent(handler -> {
            if (handler.canReceive()) {
              int received = handler.receiveEnergy(Math.min(capacity.get(), maxTransfer), false);
              capacity.addAndGet(-received);
              solarPanelBattery.consumePower(received);
              setChanged();
            }
          });
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY && side != Direction.UP) {
      return energy.cast();
    }
    return super.getCapability(cap, side);
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
  public void load(CompoundTag tag) {
    super.load(tag);
    var energyTag = tag.get("energy");
    if (energyTag != null) {
      solarPanelBattery.deserializeNBT(energyTag);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("energy", solarPanelBattery.serializeNBT());
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SolarPanelMenu(id, this, levelSolarPanel);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
  }
}
