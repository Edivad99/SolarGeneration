package edivad.solargeneration.tabs;

import edivad.solargeneration.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class SolarGenerationTab extends ItemGroup {

	public SolarGenerationTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ModBlocks.solarPanelAdvanced);
	}
}
