package edivad.solargeneration.items;

import java.util.List;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.ItemNBTHelper;
import edivad.solargeneration.tools.ModelCustomArmour;
import edivad.solargeneration.tools.MyEnergyStorage;
import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SolarHelmet extends ItemArmor {

	private SolarPanelLevel levelSolarHelmet;
	private MyEnergyStorage energyStorage;
	private int energyGeneration;
	private int maxEnergyOutput;

	public SolarHelmet(SolarPanelLevel levelSolarHelmet)
	{
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.HEAD);
		this.levelSolarHelmet = levelSolarHelmet;
		setRegistryName(getResourceLocation(levelSolarHelmet));
		setUnlocalizedName(Main.MODID + "." + getResourceLocation(levelSolarHelmet).getResourcePath());
		setCreativeTab(Main.solarGenerationTab);
		setMaxStackSize(1);

		energyGeneration = (int) Math.pow(8, levelSolarHelmet.ordinal());
		maxEnergyOutput = energyGeneration * 2;
		energyStorage = new MyEnergyStorage(energyGeneration * 2, energyGeneration * 1000);
	}

	public static ResourceLocation getResourceLocation(SolarPanelLevel levelSolarHelmet)
	{
		return new ResourceLocation(Main.MODID, "solar_helmet_" + levelSolarHelmet.name().toLowerCase());
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		Tooltip.showInfoCtrl(getEnergyStored(stack), tooltip);
		Tooltip.showInfoShift(levelSolarHelmet, tooltip);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		if(itemStack != ItemStack.EMPTY)
		{
			if(itemStack.getItem() instanceof ItemArmor)
			{
				ModelCustomArmour model = new ModelCustomArmour();

				model.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD;

				model.isChild = _default.isChild;
				model.isRiding = _default.isRiding;
				model.isSneak = _default.isSneak;
				model.rightArmPose = _default.rightArmPose;
				model.leftArmPose = _default.leftArmPose;
				return model;
			}
		}
		return null;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return Main.MODID + ":textures/models/armor/solar_helmet_" + levelSolarHelmet.name().toLowerCase() + ".png";
	}

	public SolarPanelLevel getLevelSolarPanel()
	{
		return this.levelSolarHelmet;
	}

	// Energy

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
		ItemNBTHelper.setInteger(container, "energy", energyStorage.getEnergyStored());
	}

	public int getEnergyStored(ItemStack container)
	{
		return ItemNBTHelper.getInteger(container, "energy", 0);
	}

	public int getMaxEnergyStored()
	{
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
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
	public EntityEquipmentSlot getEquipmentSlot(ItemStack stack)
	{
		if(stack.getTagCompound() == null)
		{
			energyStorage.setEnergy(0);
			saveEnergyItem(stack);
		}
		else
			energyStorage.setEnergy(getEnergyStored(stack));
		return super.getEquipmentSlot(stack);
	}

	private void sendEnergy(World world, EntityPlayer player)
	{
		for (int i = 0; i < player.inventory.getSizeInventory(); i++)
		{
			ItemStack slot = player.inventory.getStackInSlot(i);
			if(slot.getCount() == 1)
			{
				if(slot.hasCapability(CapabilityEnergy.ENERGY, null))
				{
					IEnergyStorage handler = slot.getCapability(CapabilityEnergy.ENERGY, null);
					if(handler != null && handler.canReceive())
					{
						int accepted = Math.min(maxEnergyOutput, handler.receiveEnergy(energyStorage.getEnergyStored(), true));
						energyStorage.consumePower(accepted);
						handler.receiveEnergy(accepted, false);

						if(energyStorage.getEnergyStored() <= 0)
							break;
					}

				}
			}
		}
	}

	private int currentAmountEnergyProduced(World world, EntityPlayer player)
	{
		if(!energyStorage.isFullEnergy())
		{
			BlockPos pos = new BlockPos(player.chasingPosX, player.chasingPosY + 1, player.chasingPosZ);
			return (int) (energyGeneration * ProductionSolarPanel.computeSunIntensity(world, pos, getLevelSolarPanel()));
		}

		return 0;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
