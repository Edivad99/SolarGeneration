package edivad.solargeneration.items;

import java.util.List;

import javax.annotation.Nullable;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.ModelCustomArmour;
import edivad.solargeneration.tools.MyEnergyStorage;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
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
		super(ArmorMaterial.IRON, EquipmentSlotType.HEAD, (new Item.Properties()).group(Main.solarGenerationTab).maxStackSize(1));
		this.levelSolarHelmet = levelSolarHelmet;
		setRegistryName(levelSolarHelmet.getHelmetResourceLocation());

		energyGeneration = (int) Math.pow(8, levelSolarHelmet.ordinal());
		maxEnergyOutput = energyGeneration * 2;
		energyStorage = new MyEnergyStorage(energyGeneration * 2, energyGeneration * 1000);
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		Tooltip.showInfoCtrl(getEnergyStored(stack), tooltip);
		Tooltip.showInfoShift(levelSolarHelmet, tooltip);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default)
	{
		if(itemStack != ItemStack.EMPTY)
		{
			if(itemStack.getItem() instanceof ArmorItem)
			{
				ModelCustomArmour model = new ModelCustomArmour();

				model.bipedHead.showModel = armorSlot == EquipmentSlotType.HEAD;

				model.isChild = _default.isChild;
				model.isSitting = _default.isSitting;
				model.isSneak = _default.isSneak;
				model.rightArmPose = _default.rightArmPose;
				model.leftArmPose = _default.leftArmPose;
				return (A) model;
			}
		}
		return null;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return Main.MODID + ":textures/models/armor/solar_helmet_" + levelSolarHelmet.name().toLowerCase() + ".png";
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
			container.setTag(new CompoundNBT());
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
	public void onArmorTick(ItemStack itemStack, World world, PlayerEntity player)
	{
		if(world.isRemote)
			return;

		if(!(getEnergyStored(itemStack) == getMaxEnergyStored()))
		{
			energyStorage.generatePower(currentAmountEnergyProduced(world, player));
		}
		sendEnergy(world, player);
		saveEnergyItem(itemStack);
	}

	@Override
	public EquipmentSlotType getEquipmentSlot(ItemStack stack)
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

	private void sendEnergy(World world, PlayerEntity player)
	{
		for(int i = 0; i < (player.inventory.getSizeInventory()) && energyStorage.getEnergyStored() > 0; i++)
		{
			ItemStack slot = player.inventory.getStackInSlot(i);
			if(slot.getCount() == 1)
			{
				slot.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler ->
				{
					if(handler.canReceive())
					{
						int accepted = Math.min(maxEnergyOutput, handler.receiveEnergy(energyStorage.getEnergyStored(), true));
						energyStorage.consumePower(accepted);
						handler.receiveEnergy(accepted, false);
					}
				});
			}
		}
	}

	private int currentAmountEnergyProduced(World world, PlayerEntity player)
	{
		if(!energyStorage.isFullEnergy())
		{
			BlockPos pos = new BlockPos(player.chasingPosX, player.chasingPosY + 1, player.chasingPosZ);
			return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(world, pos, getLevelSolarPanel()));
		}
		return 0;
	}
}
