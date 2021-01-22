package edivad.solargeneration.tools;

public enum SolarPanelLevel {

    LEADSTONE, HARDENED, REDSTONE, SIGNALUM, RESONANT, ADVANCED, ULTIMATE;

    public String getSolarPanelName()
    {
        return "solar_panel_" + getCorrectName(this.ordinal());
    }

    public String getSolarHelmetName()
    {
        return "solar_helmet_" + getCorrectName(this.ordinal());
    }

    public String getSolarCoreName()
    {
        return "solar_core_" + getCorrectName(this.ordinal());
    }

    private String getCorrectName(int index)
    {
        switch (index)
        {
            case 0: return "leadstone";
            case 1: return "hardened";
            case 2: return "redstone";
            case 3: return "signalum";
            case 4: return "resonant";
            case 5: return "advanced";
            case 6: return "ultimate";
            default: throw new RuntimeException("Solar panel tier not yet implemented!");
        }
    }
}
