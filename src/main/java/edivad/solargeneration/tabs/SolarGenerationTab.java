package edivad.solargeneration.tabs;

import edivad.solargeneration.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SolarGenerationTab extends CreativeTabs {

	public SolarGenerationTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModBlocks.solarPanelAdvanced);
	}
}
