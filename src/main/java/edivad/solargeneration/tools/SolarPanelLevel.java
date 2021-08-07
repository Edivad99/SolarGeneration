package edivad.solargeneration.tools;

import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;

public enum SolarPanelLevel {

    LEADSTONE, HARDENED, REDSTONE, SIGNALUM, RESONANT, ADVANCED, ULTIMATE;

    public String getSolarPanelName() {
        return "solar_panel_" + getCorrectName();
    }

    public String getSolarHelmetName() {
        return "solar_helmet_" + getCorrectName();
    }

    public String getSolarCoreName() {
        return "solar_core_" + getCorrectName();
    }

    public String getArmorTexture() {
        return ":textures/models/armor/solar_helmet_" + getCorrectName() + ".png";
    }

    public ArmorMaterial getArmorMaterial() {
        return switch(this.ordinal()) {
            case 0, 1 -> ArmorMaterials.IRON;
            case 2, 3, 4 -> ArmorMaterials.DIAMOND;
            case 5, 6 -> ArmorMaterials.NETHERITE;
            default -> throw new RuntimeException("Solar panel tier not yet implemented!");
        };
    }

    public int getEnergyGeneration() {
        return (int) Math.pow(8, this.ordinal());
    }

    public int getMaxTransfer() {
        return getEnergyGeneration() * 2;
    }

    public int getCapacity() {
        return getEnergyGeneration() * 1000;
    }

    private String getCorrectName() {
        return switch(this.ordinal()) {
            case 0 -> "leadstone";
            case 1 -> "hardened";
            case 2 -> "redstone";
            case 3 -> "signalum";
            case 4 -> "resonant";
            case 5 -> "advanced";
            case 6 -> "ultimate";
            default -> throw new RuntimeException("Solar panel tier not yet implemented!");
        };
    }
}
