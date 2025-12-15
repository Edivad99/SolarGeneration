package edivad.solargeneration.items;

import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarGenerationDataComponents;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class SolarHelmet extends Item {

  private final SolarPanelLevel solarPanelLevel;
  private final int energyGeneration;
  private final int maxTransfer;

  public SolarHelmet(SolarPanelLevel solarPanelLevel, Properties properties) {
    super(properties
        .component(DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.HEAD)
                .setEquipSound(null)
                .setAllowedEntities(HolderSet.direct(EntityType.PLAYER.builtInRegistryHolder()))
                .setDispensable(true)
                .setSwappable(false)
                .setDamageOnHurt(false)
                .setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID, SolarGeneration.id(solarPanelLevel.getSolarHelmetName())))
                .build()));
    this.solarPanelLevel = solarPanelLevel;

    this.energyGeneration = solarPanelLevel.getEnergyGeneration();
    this.maxTransfer = solarPanelLevel.getMaxTransfer();
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    int energy = stack.getOrDefault(SolarGenerationDataComponents.ENERGY_COMPONENT, 0);
    if (energy > 0) {
      tooltipAdder.accept(Tooltip.showInfoCtrl(energy));
    }
    Tooltip.showInfoShift(this.solarPanelLevel, tooltipAdder);
  }

  public SolarPanelLevel getLevelSolarPanel() {
    return this.solarPanelLevel;
  }

  @Override
  public boolean isBarVisible(ItemStack itemStack) {
    return true;
  }

  @Override
  public int getBarWidth(ItemStack itemStack) {
    int energy = itemStack.getOrDefault(SolarGenerationDataComponents.ENERGY_COMPONENT, 0);
    if (energy == 0) {
      return 0;
    }
    var charge = (double) energy / (double) this.solarPanelLevel.getCapacity();
    return (int) Math.min(1 + 12 * charge, 13);
  }

  @Override
  public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity,
      @Nullable EquipmentSlot slot) {
    if (!(entity instanceof Player player)) {
      return;
    }

    // Check if the player is wearing the helmet
    if (slot != EquipmentSlot.HEAD) {
      return;
    }

    var energy = ItemAccess.forStack(stack).getCapability(Capabilities.Energy.ITEM);
    if (energy == null) {
      return;
    }
    try (var tx = Transaction.open(null)) {
      var energyInserted = energy.insert(this.currentAmountEnergyProduced(level, player), tx);
      if (energyInserted != 0) {
        tx.commit();
      }
    }
    stack.set(SolarGenerationDataComponents.ENERGY_COMPONENT, energy.getAmountAsInt());
    this.sendEnergy(energy, player);
  }

  private void sendEnergy(EnergyHandler energy, Player player) {
    var inventory = player.getInventory();
    //Armor priority
    for (int i = Inventory.INVENTORY_SIZE; i < 40 && energy.getAmountAsInt() > 0; i++) {
      var item = inventory.getItem(i);
      if (item.getItem() != this) {
        chargeItem(energy, item);
      }
    }
    //Inventory
    for (int i = 0; i < Inventory.INVENTORY_SIZE && energy.getAmountAsInt() > 0; i++) {
      chargeItem(energy, inventory.getItem(i));
    }
  }

  private void chargeItem(EnergyHandler energyHandlerSender, ItemStack receiver) {
    if (receiver.getCount() != 1) {
      return;
    }
    var energyHandlerReceiver = ItemAccess.forStack(receiver).getCapability(Capabilities.Energy.ITEM);
    EnergyHandlerUtil.move(energyHandlerSender, energyHandlerReceiver, this.maxTransfer, null);
  }

  private int currentAmountEnergyProduced(Level level, Player player) {
    return (int) (this.energyGeneration * ProductionSolarPanel.computeSunIntensity(level,
        player.blockPosition().offset(0, 1, 0), getLevelSolarPanel()));
  }
}
