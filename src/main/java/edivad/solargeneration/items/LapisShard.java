package edivad.solargeneration.items;

import edivad.solargeneration.Main;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class LapisShard extends Item {

	public LapisShard()
	{
		super(new Properties().group(Main.solarGenerationTab).maxStackSize(64));
		setRegistryName(new ResourceLocation(Main.MODID, "lapis_shard"));
	}
}