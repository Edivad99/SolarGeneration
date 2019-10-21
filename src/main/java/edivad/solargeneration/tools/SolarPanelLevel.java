package edivad.solargeneration.tools;

import edivad.solargeneration.Main;
import net.minecraft.util.ResourceLocation;

public enum SolarPanelLevel {

	Leadstone, Hardened, Redstone, Signalum, Resonant, Advanced, Ultimate;

	public ResourceLocation getBlockResourceLocation()
	{
		return new ResourceLocation(Main.MODID, "solar_panel_" + this.name().toLowerCase());
	}

	public ResourceLocation getHelmetResourceLocation()
	{
		return new ResourceLocation(Main.MODID, "solar_helmet_" + this.name().toLowerCase());
	}
}
