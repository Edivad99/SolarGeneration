package edivad.solargeneration.items;

import edivad.solargeneration.Main;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class PhotovoltaicCell extends Item {

	public PhotovoltaicCell()
	{
		super(new Properties().group(Main.solarGenerationTab).maxStackSize(64));
		setRegistryName(new ResourceLocation(Main.MODID, "photovoltaic_cell"));
	}
}