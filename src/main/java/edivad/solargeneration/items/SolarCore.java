package edivad.solargeneration.items;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class SolarCore extends Item {

	public SolarCore(SolarPanelLevel levelCore)
	{
		super(new Properties().group(Main.solarGenerationTab).maxStackSize(64));
		setRegistryName(new ResourceLocation(Main.MODID, "solar_core_" + levelCore.name().toLowerCase()));
	}
}
