package edivad.solargeneration.tile;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;

public class TileEntityAdvancedSolarPanel extends TileEntitySolarPanel {

	public TileEntityAdvancedSolarPanel()
	{
		super(SolarPanelLevel.Advanced, Registration.ADVANCED_TILE.get());
	}
}
