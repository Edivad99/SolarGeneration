package edivad.solargeneration.items;

import java.util.List;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarGenerationDataComponents;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class SolarHelmet extends ArmorItem {

  private final SolarPanelLevel solarPanelLevel;
  private final int energyGeneration;
  private final int maxTransfer;

  public SolarHelmet(SolarPanelLevel solarPanelLevel, Properties properties) {
    super(solarPanelLevel.getArmorMaterial(), Type.HELMET, properties);
    this.solarPanelLevel = solarPanelLevel;

    this.energyGeneration = solarPanelLevel.getEnergyGeneration();
    this.maxTransfer = solarPanelLevel.getMaxTransfer();
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      List<Component> tooltip, TooltipFlag flag) {
    int energy = stack.getOrDefault(SolarGenerationDataComponents.ENERGY_COMPONENT, 0);
    if (energy > 0) {
      tooltip.add(Tooltip.showInfoCtrl(energy));
    }
    tooltip.addAll(Tooltip.showInfoShift(this.solarPanelLevel));
  }

  @Override
  public ResourceLocation getArmorTexture(ItemStack stack, Entity entity,
      EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
    return this.solarPanelLevel.getArmorTexture();
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
    var energy = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
    if (energy == null) {
      return 0;
    }
    if (energy.getEnergyStored() == 0) {
      return 0;
    }
    var charge = (double) energy.getEnergyStored() / (double) energy.getMaxEnergyStored();
    return (int) Math.min(1 + 12 * charge, 13);
  }

  @Override
  public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId,
      boolean isSelected) {
    if (!(entity instanceof ServerPlayer player)) {
      return;
    }

    // Check if the player is wearing the helmet
    if (slotId != Inventory.INVENTORY_SIZE + EquipmentSlot.HEAD.getIndex()) {
      return;
    }

    var energy = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
    if (energy == null) {
      return;
    }

    if (energy.getEnergyStored() != energy.getMaxEnergyStored()) {
      energy.receiveEnergy(this.currentAmountEnergyProduced(level, player), false);
    }
    this.sendEnergy(energy, player);
  }

  private void sendEnergy(IEnergyStorage energy, Player player) {
    var inventory = player.getInventory();
    //Armor priority
    for (int i = Inventory.INVENTORY_SIZE; i < 40 && energy.getEnergyStored() > 0; i++) {
      var item = inventory.getItem(i);
      if (item.getItem() != this) {
        chargeItem(energy, item);
      }
    }
    //Inventory
    for (int i = 0; i < Inventory.INVENTORY_SIZE && energy.getEnergyStored() > 0; i++) {
      chargeItem(energy, inventory.getItem(i));
    }
  }

  private void chargeItem(IEnergyStorage energyStorage, ItemStack receiver) {
    if (receiver.getCount() != 1) {
      return;
    }
    var handler = receiver.getCapability(Capabilities.EnergyStorage.ITEM);
    if (handler == null) {
      return;
    }
    if (handler.canReceive()) {
      while (handler.getEnergyStored() < handler.getMaxEnergyStored()
          && energyStorage.getEnergyStored() > 0) {
        int accepted = Math.min(this.maxTransfer,
            handler.receiveEnergy(energyStorage.getEnergyStored(), true));
        energyStorage.extractEnergy(accepted, false);
        handler.receiveEnergy(accepted, false);
      }
    }
  }

  private int currentAmountEnergyProduced(Level level, Player player) {
    return (int) (this.energyGeneration * ProductionSolarPanel.computeSunIntensity(level,
        player.blockPosition().offset(0, 1, 0), getLevelSolarPanel()));
  }
}
