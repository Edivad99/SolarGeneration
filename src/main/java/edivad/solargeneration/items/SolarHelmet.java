package edivad.solargeneration.items;

import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelBattery;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.List;

public class SolarHelmet extends ArmorItem {

    private final SolarPanelLevel solarPanelLevel;
    private final SolarPanelBattery energyStorage;
    private final int energyGeneration;
    private final int maxTransfer;

    public SolarHelmet(SolarPanelLevel solarPanelLevel) {
        super(solarPanelLevel.getArmorMaterial(), Type.HELMET, new Item.Properties().stacksTo(1));
        this.solarPanelLevel = solarPanelLevel;

        energyGeneration = solarPanelLevel.getEnergyGeneration();
        maxTransfer = solarPanelLevel.getMaxTransfer();
        int capacity = solarPanelLevel.getCapacity();
        energyStorage = new SolarPanelBattery(maxTransfer, capacity);
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            int energy = getEnergyStored(stack);
            tooltip.add(Tooltip.showInfoCtrl(energy));
        }
        tooltip.addAll(Tooltip.showInfoShift(this.solarPanelLevel));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
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
        if (getEnergyStored(itemStack) == 0) return 0;
        return Math.min(1 + 12 * getEnergyStored(itemStack) / getMaxEnergyStored(), 13);
    }

    public void saveEnergyItem(ItemStack itemStack) {
        itemStack.getOrCreateTag().putInt("energy", energyStorage.getEnergyStored());
    }

    public int getEnergyStored(ItemStack itemStack) {
        if(!itemStack.hasTag())
            return 0;
        return itemStack.getTag().getInt("energy");
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public void onArmorTick(ItemStack itemStack, Level level, Player player) {
        if(level.isClientSide)
            return;

        if(!(getEnergyStored(itemStack) == getMaxEnergyStored())) {
            energyStorage.generatePower(currentAmountEnergyProduced(level, player));
        }
        sendEnergy(player);
        saveEnergyItem(itemStack);
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        if(stack.getTag() == null) {
            energyStorage.setEnergy(0);
            saveEnergyItem(stack);
        }
        else
            energyStorage.setEnergy(getEnergyStored(stack));
        return super.getEquipmentSlot(stack);
    }

    private void sendEnergy(Player player) {
        //Armor priority
        for(int i = 36; i < 40 && energyStorage.getEnergyStored() > 0; i++) {
            ItemStack item = player.getInventory().getItem(i);
            chargeItem(item);
        }
        //Inventory
        for(int i = 0; i < 36 && energyStorage.getEnergyStored() > 0; i++) {
            ItemStack item = player.getInventory().getItem(i);
            chargeItem(item);
        }
    }

    private void chargeItem(ItemStack slot) {
        if(slot.getCount() == 1) {
            slot.getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
                if(handler.canReceive()) {
                    while(handler.getEnergyStored() < handler.getMaxEnergyStored() && energyStorage.getEnergyStored() > 0) {
                        int accepted = Math.min(maxTransfer, handler.receiveEnergy(energyStorage.getEnergyStored(), true));
                        energyStorage.consumePower(accepted);
                        handler.receiveEnergy(accepted, false);
                    }
                }
            });
        }
    }

    private int currentAmountEnergyProduced(Level level, Player player) {
        if(!energyStorage.isFullEnergy()) {
            return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(level, player.blockPosition().offset(0, 1, 0), getLevelSolarPanel()));
        }
        return 0;
    }
}
