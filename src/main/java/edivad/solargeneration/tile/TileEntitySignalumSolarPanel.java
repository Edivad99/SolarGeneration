package edivad.solargeneration.tile;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;

public class TileEntitySignalumSolarPanel extends TileEntitySolarPanel {

	public TileEntitySignalumSolarPanel()
	{
		super(SolarPanelLevel.Signalum, Registration.SIGNALUM_TILE.get());
	}
}
