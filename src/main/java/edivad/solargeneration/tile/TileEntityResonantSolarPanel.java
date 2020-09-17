package edivad.solargeneration.tile;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;

public class TileEntityResonantSolarPanel extends TileEntitySolarPanel {

    public TileEntityResonantSolarPanel()
    {
        super(SolarPanelLevel.RESONANT, Registration.RESONANT_TILE.get());
    }
}
