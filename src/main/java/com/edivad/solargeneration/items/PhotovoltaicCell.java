package com.edivad.solargeneration.items;

import com.edivad.solargeneration.Main;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PhotovoltaicCell extends Item {

	public PhotovoltaicCell() {

		setRegistryName(new ResourceLocation(Main.MODID, "photovoltaic_cell"));
		setUnlocalizedName(Main.MODID + ".photovoltaic_cell");
		setCreativeTab(Main.solarGenerationTab);
		setMaxStackSize(64);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}