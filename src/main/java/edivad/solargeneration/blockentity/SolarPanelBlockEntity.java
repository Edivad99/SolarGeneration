package edivad.solargeneration.blockentity;

import org.jetbrains.annotations.Nullable;
import edivad.solargeneration.menu.SolarPanelMenu;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.PacketDistributor;

public class SolarPanelBlockEntity extends BlockEntity implements MenuProvider {

  private final int energyGeneration;
  private final int maxTransfer;
  private final SolarPanelBattery solarPanelBattery;
  private final SolarPanelLevel levelSolarPanel;
  public int energyClient, energyProductionClient;

  public SolarPanelBlockEntity(SolarPanelLevel levelSolarPanel, BlockPos pos, BlockState state) {
    super(Registration.SOLAR_PANEL_BLOCK_ENTITY.get(levelSolarPanel).get(), pos, state);
    this.levelSolarPanel = levelSolarPanel;

    energyGeneration = levelSolarPanel.getEnergyGeneration();
    maxTransfer = levelSolarPanel.getMaxTransfer();
    int capacity = levelSolarPanel.getCapacity();

    solarPanelBattery = new SolarPanelBattery(maxTransfer, capacity);

    energyClient = energyProductionClient = -1;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      SolarPanelBlockEntity solarPanel) {
    int energyProducedBySun = solarPanel.currentAmountEnergyProduced(level);
    solarPanel.solarPanelBattery.generatePower(energyProducedBySun);
    solarPanel.sendEnergy();
    int energyStored = solarPanel.solarPanelBattery.getEnergyStored();
    if (solarPanel.energyClient != energyStored
        || solarPanel.energyProductionClient != energyProducedBySun) {
      int energyProduced = solarPanel.solarPanelBattery.isFullEnergy() ? 0 : energyProducedBySun;
      solarPanel.setChanged();
      var message = new UpdateSolarPanel(blockPos, energyStored, energyProduced);
      PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(blockPos)).send(message);
    }
  }

  private int currentAmountEnergyProduced(Level level) {
    return (int) (energyGeneration *
        ProductionSolarPanel.computeSunIntensity(level, worldPosition, levelSolarPanel));
  }

  private void sendEnergy() {
    var capacity = solarPanelBattery.getEnergyStored();

    for (int i = 0; (i < Direction.values().length) && capacity > 0; i++) {
      var facing = Direction.values()[i];
      if (facing.equals(Direction.UP)) {
        continue;
      }

      var energyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK,
          worldPosition.relative(facing), facing.getOpposite());

      if (energyStorage != null && energyStorage.canReceive()) {
        int received = energyStorage.receiveEnergy(Math.min(capacity, maxTransfer), false);
        capacity -= received;
        solarPanelBattery.consumePower(received);
        setChanged();
      }
    }
  }
  @Nullable
  public SolarPanelBattery getSolarPanelBattery(Direction direction) {
    return direction != Direction.UP ? solarPanelBattery : null;
  }

  public SolarPanelLevel getLevelSolarPanel() {
    return levelSolarPanel;
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
