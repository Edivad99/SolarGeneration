package edivad.solargeneration.compat;

import edivad.solargeneration.compat.waila.WailaCompatibility;
import net.minecraftforge.fml.common.Loader;

public class MainCompatHandler {

	public static void registerWaila()
	{
		if(Loader.isModLoaded("waila"))
		{
			WailaCompatibility.register();
		}
	}
}