package edivad.solargeneration.tile;

import edivad.solargeneration.ModBlocks;
import edivad.solargeneration.tools.SolarPanelLevel;

public class TileEntityRedstoneSolarPanel extends TileEntitySolarPanel {

	public TileEntityRedstoneSolarPanel()
	{
		super(SolarPanelLevel.Redstone, ModBlocks.REDSTONE);
	}
}
