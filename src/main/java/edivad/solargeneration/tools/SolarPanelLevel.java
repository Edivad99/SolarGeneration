package edivad.solargeneration.tools;

public enum SolarPanelLevel {

    LEADSTONE, HARDENED, REDSTONE, SIGNALUM, RESONANT, ADVANCED, ULTIMATE;

    public String getSolarPanelName()
    {
        return "solar_panel_" + this.name().toLowerCase();
    }

    public String getSolarHelmetName()
    {
        return "solar_helmet_" + this.name().toLowerCase();
    }

    public String getSolarCoreName()
    {
        return "solar_core_" + this.name().toLowerCase();
    }
}
