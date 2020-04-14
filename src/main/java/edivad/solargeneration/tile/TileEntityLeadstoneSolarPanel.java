package edivad.solargeneration.tile;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;

public class TileEntityLeadstoneSolarPanel extends TileEntitySolarPanel {

	public TileEntityLeadstoneSolarPanel()
	{
		super(SolarPanelLevel.Leadstone, Registration.LEADSTONE_TILE.get());
	}
}
