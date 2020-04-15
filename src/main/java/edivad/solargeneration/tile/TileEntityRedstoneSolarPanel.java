package edivad.solargeneration.tile;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;

public class TileEntityRedstoneSolarPanel extends TileEntitySolarPanel {

	public TileEntityRedstoneSolarPanel()
	{
		super(SolarPanelLevel.Redstone, Registration.REDSTONE_TILE.get());
	}
}
