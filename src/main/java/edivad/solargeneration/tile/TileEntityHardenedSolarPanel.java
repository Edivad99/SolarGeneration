package edivad.solargeneration.tile;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;

public class TileEntityHardenedSolarPanel extends TileEntitySolarPanel {

    public TileEntityHardenedSolarPanel()
    {
        super(SolarPanelLevel.HARDENED, Registration.HARDENED_TILE.get());
    }
}
