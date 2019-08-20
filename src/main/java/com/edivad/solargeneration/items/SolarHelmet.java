package com.edivad.solargeneration.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.edivad.solargeneration.Main;
import com.edivad.solargeneration.tools.ItemNBTHelper;
import com.edivad.solargeneration.tools.ModelCustomArmour;
import com.edivad.solargeneration.tools.MyEnergyStorage;
import com.edivad.solargeneration.tools.SolarPanelLevel;
import com.edivad.solargeneration.tools.Tooltip;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SolarHelmet extends ItemArmor{
	
	private SolarPanelLevel levelSolarHelmet;
	private MyEnergyStorage energyStorage;
	private int energyGeneration;
	private int maxEnergyOutput;
	
	public SolarHelmet(SolarPanelLevel levelSolarHelmet) {
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
	
	public static ResourceLocation getResourceLocation(SolarPanelLevel levelSolarHelmet) {
		switch (levelSolarHelmet) 
		{
			case Leadstone : return new ResourceLocation(Main.MODID, "solar_helmet_leadstone");
			case Hardened : return new ResourceLocation(Main.MODID, "solar_helmet_hardened");
			case Redstone : return new ResourceLocation(Main.MODID, "solar_helmet_redstone");
			case Resonant : return new ResourceLocation(Main.MODID, "solar_helmet_resonant");
			case Advanced : return new ResourceLocation(Main.MODID, "solar_helmet_advanced");
			case Ultimate : return new ResourceLocation(Main.MODID, "solar_helmet_ultimate");
			default : return new ResourceLocation(Main.MODID, "solar_helmet_leadstone");
		}
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) 
		{
			int energy = getEnergyStored(stack);
			String info = TextFormatting.WHITE + "Energy stored: " + TextFormatting.GREEN +  energy + TextFormatting.DARK_RED + " FE";
			tooltip.add(info);
		}
		else
			tooltip.add("Hold "+ TextFormatting.AQUA + TextFormatting.ITALIC + "Ctrl" + TextFormatting.GRAY + " for energy stored");
		
		Tooltip.showInfoShift(levelSolarHelmet, tooltip);
	}
	
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
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		String levelCore="";
		switch (levelSolarHelmet) 
		{
			case Leadstone : levelCore = "solar_helmet_leadstone"; break;
			case Hardened : levelCore = "solar_helmet_hardened"; break;
			case Redstone : levelCore = "solar_helmet_redstone"; break;
			case Resonant : levelCore = "solar_helmet_resonant"; break;
			case Advanced : levelCore = "solar_helmet_advanced"; break;
			case Ultimate : levelCore = "solar_helmet_ultimate"; break;
			default : levelCore = "solar_helmet_leadstone"; break;
		}
		return Main.MODID + ":textures/models/armor/" + levelCore + ".png";
	}

	public SolarPanelLevel getLevelSolarPanel() {
		return this.levelSolarHelmet;
	}
	
	//Energy
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !(getEnergyStored(stack) == getMaxEnergyStored());
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ((double) getEnergyStored(stack) / (double) getMaxEnergyStored());
	}
	
    public void saveEnergyItem(ItemStack container) {
    	ItemNBTHelper.setInteger(container, "energy", energyStorage.getEnergyStored());
    }
    
    public int getEnergyStored(ItemStack container) {
        return ItemNBTHelper.getInteger(container, "energy", 0);
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }
	
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if(!(getEnergyStored(itemStack) == getMaxEnergyStored()))
		{
			energyStorage.generatePower(currentAmountEnergyProduced(world, player));	
		}
		sendEnergy(world, player);
		saveEnergyItem(itemStack);
	}
	
	private void sendEnergy(World world, EntityPlayer player) {
		if (!world.isRemote) 
		{
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) 
            {
                ItemStack slot = player.inventory.getStackInSlot(i);
                if (slot.getCount() == 1) 
                {
                    if (slot.hasCapability(CapabilityEnergy.ENERGY, null)) 
                    {
                        IEnergyStorage handler = slot.getCapability(CapabilityEnergy.ENERGY, null);
                        if (handler != null && handler.canReceive()) 
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
	}
	
	private int currentAmountEnergyProduced(World world, EntityPlayer player)
	{
		if(!energyStorage.isFullEnergy())
			return (int) (energyGeneration * computeSunIntensity(world, player));
		return 0;
	}
	
	private float computeSunIntensity(World world, EntityPlayer player) {
		float sunIntensity = 0;
		
        if (world.canBlockSeeSky(new BlockPos(player.chasingPosX, player.chasingPosY + 1 , player.chasingPosZ)))
        {
        	float multiplicator = 1.5f;
            float displacement = 1.2f;
            // Celestial angle == 0 at zenith.
            float celestialAngleRadians = world.getCelestialAngleRadians(1.0f);
            if (celestialAngleRadians > Math.PI) {
                celestialAngleRadians = (2 * 3.141592f - celestialAngleRadians);
            }

            sunIntensity = multiplicator * MathHelper.cos(celestialAngleRadians / displacement);
            sunIntensity = Math.max(0, sunIntensity);
            sunIntensity = Math.min(1, sunIntensity);

            if (sunIntensity > 0) 
            {
            	if(getLevelSolarPanel() == SolarPanelLevel.Leadstone)
            		sunIntensity = 1;
            	
                if (world.isRaining()) 
                	sunIntensity *= 0.4;
                
                if (world.isThundering()) 
                	sunIntensity *= 0.2;
            }
        }
        
        return sunIntensity;
    }
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
