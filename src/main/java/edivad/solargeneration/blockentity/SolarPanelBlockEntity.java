package edivad.solargeneration.blockentity;

import org.jspecify.annotations.Nullable;
import edivad.solargeneration.menu.SolarPanelMenu;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import edivad.solargeneration.setup.ModRegistration;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarGenerationDataComponents;
import edivad.solargeneration.tools.SolarPanelBattery;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class SolarPanelBlockEntity extends BlockEntity implements MenuProvider {

  private final int energyGeneration;
  private final int maxTransfer;
  private final SolarPanelBattery solarPanelBattery;
  private final SolarPanelLevel levelSolarPanel;
  public int energyClient, energyProductionClient;

  public SolarPanelBlockEntity(SolarPanelLevel levelSolarPanel, BlockPos pos, BlockState state) {
    super(ModRegistration.SOLAR_PANEL_BLOCK_ENTITY.get(levelSolarPanel).get(), pos, state);
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
    int energyStored = solarPanel.solarPanelBattery.getAmountAsInt();
    if (solarPanel.energyClient != energyStored
        || solarPanel.energyProductionClient != energyProducedBySun) {
      int energyProduced = solarPanel.solarPanelBattery.isFullEnergy() ? 0 : energyProducedBySun;
      solarPanel.setChanged();
      var message = new UpdateSolarPanel(blockPos, energyStored, energyProduced);
      PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level,
          level.getChunk(blockPos).getPos(), message);
    }
  }

  private int currentAmountEnergyProduced(Level level) {
    return (int) (energyGeneration *
        ProductionSolarPanel.computeSunIntensity(level, worldPosition, levelSolarPanel));
  }

  private void sendEnergy() {
    var capacity = solarPanelBattery.getAmountAsInt();

    for (int i = 0; (i < Direction.values().length) && capacity > 0; i++) {
      var facing = Direction.values()[i];
      if (facing.equals(Direction.UP)) {
        continue;
      }

      var handler = level.getCapability(Capabilities.Energy.BLOCK,
          worldPosition.relative(facing), facing.getOpposite());
      if (handler == null) {
        continue;
      }

      try (var tx = Transaction.openRoot()) {
        var energyInserted = handler.insert(Math.min(capacity, maxTransfer), tx);
        if (energyInserted == 0) {
          // If we can't insert any energy, skip this side.
          continue;
        }
        capacity -= energyInserted;
        solarPanelBattery.consumePower(energyInserted);
        setChanged();
        tx.commit();
      }
    }
  }
  @Nullable
  public SolarPanelBattery getSolarPanelBattery(@Nullable Direction direction) {
    return direction != Direction.UP ? solarPanelBattery : null;
  }

  public SolarPanelLevel getLevelSolarPanel() {
    return levelSolarPanel;
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    solarPanelBattery.deserialize(input);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    solarPanelBattery.serialize(output);
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter componentGetter) {
    int energy = componentGetter.getOrDefault(SolarGenerationDataComponents.ENERGY_COMPONENT, 0);
    solarPanelBattery.set(energy);
    super.applyImplicitComponents(componentGetter);
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder components) {
    components.set(SolarGenerationDataComponents.ENERGY_COMPONENT, solarPanelBattery.getAmountAsInt());
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
