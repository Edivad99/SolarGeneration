package edivad.solargeneration.items;

import java.util.List;

import javax.annotation.Nullable;

import edivad.solargeneration.Main;
import edivad.solargeneration.setup.ModSetup;
import edivad.solargeneration.tools.MyEnergyStorage;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;

public class SolarHelmet extends ArmorItem {

    private SolarPanelLevel levelSolarHelmet;
    private MyEnergyStorage energyStorage;
    private int energyGeneration;
    private int maxEnergyOutput;

    public SolarHelmet(SolarPanelLevel levelSolarHelmet)
    {
        super(levelSolarHelmet.getArmorMaterial(), EquipmentSlot.HEAD, (new Item.Properties()).tab(ModSetup.solarGenerationTab).stacksTo(1));
        this.levelSolarHelmet = levelSolarHelmet;

        energyGeneration = (int) Math.pow(8, levelSolarHelmet.ordinal());
        maxEnergyOutput = energyGeneration * 2;
        energyStorage = new MyEnergyStorage(energyGeneration * 2, energyGeneration * 1000);
    }

    @Override
    public boolean canBeDepleted()
    {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        int energy = getEnergyStored(stack);
        if(energy != 0)
            Tooltip.showInfoCtrl(energy, tooltip);
        Tooltip.showInfoShift(levelSolarHelmet, tooltip);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        return Main.MODID + levelSolarHelmet.getArmorTexture();
    }

    public SolarPanelLevel getLevelSolarPanel()
    {
        return this.levelSolarHelmet;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return !(getEnergyStored(stack) == getMaxEnergyStored());
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1D - ((double) getEnergyStored(stack) / (double) getMaxEnergyStored());
    }

    public void saveEnergyItem(ItemStack container)
    {
        if(container.getTag() == null)
            container.setTag(new CompoundTag());
        container.getTag().putInt("energy", energyStorage.getEnergyStored());
    }

    public int getEnergyStored(ItemStack container)
    {
        if(container.getTag() == null)
            return 0;
        return container.getTag().getInt("energy");
    }

    public int getMaxEnergyStored()
    {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public void onArmorTick(ItemStack itemStack, Level world, Player player)
    {
        if(world.isClientSide)
            return;

        if(!(getEnergyStored(itemStack) == getMaxEnergyStored()))
        {
            energyStorage.generatePower(currentAmountEnergyProduced(world, player));
        }
        sendEnergy(player);
        saveEnergyItem(itemStack);
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack)
    {
        if(stack.getTag() == null)
        {
            energyStorage.setEnergy(0);
            saveEnergyItem(stack);
        }
        else
            energyStorage.setEnergy(getEnergyStored(stack));
        return super.getEquipmentSlot(stack);
    }

    private void sendEnergy(Player player)
    {
        //Armor priority
        for(int i = 36; i < 40 && energyStorage.getEnergyStored() > 0; i++)
        {
            //ItemStack slot = player.inventory.getItem(i);
            ItemStack slot = player.inventoryMenu.getItems().get(i);
            chargegItem(slot);
        }
        //Inventory
        for(int i = 0; i < 36 && energyStorage.getEnergyStored() > 0; i++)
        {
            //ItemStack slot = player.inventory.getItem(i);
            ItemStack slot = player.inventoryMenu.getItems().get(i);
            chargegItem(slot);
        }
    }

    private void chargegItem(ItemStack slot)
    {
        if(slot.getCount() == 1)
        {
            slot.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                if(handler.canReceive())
                {
                    while(handler.getEnergyStored() < handler.getMaxEnergyStored() && energyStorage.getEnergyStored() > 0)
                    {
                        int accepted = Math.min(maxEnergyOutput, handler.receiveEnergy(energyStorage.getEnergyStored(), true));
                        energyStorage.consumePower(accepted);
                        handler.receiveEnergy(accepted, false);
                    }
                }
            });
        }
    }

    private int currentAmountEnergyProduced(Level world, Player player)
    {
        if(!energyStorage.isFullEnergy())
        {
            return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(world, player.blockPosition().offset(0, 1, 0), getLevelSolarPanel()));
        }
        return 0;
    }
}
