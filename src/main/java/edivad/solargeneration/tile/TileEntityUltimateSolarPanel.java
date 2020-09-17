package edivad.solargeneration.tile;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;

public class TileEntityUltimateSolarPanel extends TileEntitySolarPanel {

    public TileEntityUltimateSolarPanel()
    {
        super(SolarPanelLevel.ULTIMATE, Registration.ULTIMATE_TILE.get());
    }
}
