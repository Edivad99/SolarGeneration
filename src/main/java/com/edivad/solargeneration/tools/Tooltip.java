package com.edivad.solargeneration.tools;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class Tooltip {

	public static void showInfoShift(SolarPanelLevel solarPanelLevel, List<String> tooltip) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.WHITE + "Generation: " + TextFormatting.GREEN +  ((int) Math.pow(8, solarPanelLevel.ordinal())) + TextFormatting.DARK_RED + " FE/t");
			tooltip.add(TextFormatting.WHITE + "Transfer: " + TextFormatting.GREEN +  ((int) Math.pow(8, solarPanelLevel.ordinal())) * 2 + TextFormatting.DARK_RED + " FE/t");
			tooltip.add(TextFormatting.WHITE + "Capacity: " + TextFormatting.GREEN +  ((int) Math.pow(8, solarPanelLevel.ordinal())) * 1000 + TextFormatting.DARK_RED + " FE");
        }
		else
			tooltip.add("Hold "+ TextFormatting.AQUA + TextFormatting.ITALIC + "Shift" + TextFormatting.GRAY + " for details");
	}
	
	public static void showInfoCtrl(ItemStack stack, List<String> tooltip) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) 
		{
			int energy = 0;
			if(tagCompound != null)
				energy = tagCompound.getInteger("energy");
				
			String info = TextFormatting.WHITE + "Energy stored: " + TextFormatting.GREEN +  energy + TextFormatting.DARK_RED + " FE";
			tooltip.add(info);
		}
		else
			tooltip.add("Hold "+ TextFormatting.AQUA + TextFormatting.ITALIC + "Ctrl" + TextFormatting.GRAY + " for energy stored");
	}
	
}
