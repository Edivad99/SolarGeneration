package edivad.solargeneration.items;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SolarCore extends Item {

	public SolarCore(SolarPanelLevel levelCore)
	{
		setRegistryName(new ResourceLocation(Main.MODID, "solar_core_" + levelCore.name().toLowerCase()));
		setUnlocalizedName(Main.MODID + "." + "solar_core_" + levelCore.name().toLowerCase());
		setCreativeTab(Main.solarGenerationTab);
		setMaxStackSize(64);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
