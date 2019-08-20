package com.edivad.solargeneration;

import com.edivad.solargeneration.blocks.SolarPanel;
import com.edivad.solargeneration.tile.TileEntityAdvancedSolarPanel;
import com.edivad.solargeneration.tile.TileEntityHardenedSolarPanel;
import com.edivad.solargeneration.tile.TileEntityLeadstoneSolarPanel;
import com.edivad.solargeneration.tile.TileEntityRedstoneSolarPanel;
import com.edivad.solargeneration.tile.TileEntityResonantSolarPanel;
import com.edivad.solargeneration.tile.TileEntityUltimateSolarPanel;
import com.edivad.solargeneration.tools.SolarPanelLevel;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	//Solar panel
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_panel_advanced")
    public static SolarPanel solarPanelAdvanced;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_panel_hardened")
    public static SolarPanel solarPanelHardened;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_panel_leadstone")
    public static SolarPanel solarPanelLeadstone;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_panel_redstone")
    public static SolarPanel solarPanelRedstone;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_panel_resonant")
    public static SolarPanel solarPanelResonant;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_panel_ultimate")
    public static SolarPanel solarPanelUltimate;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        solarPanelAdvanced.initModel();
        solarPanelHardened.initModel();
        solarPanelLeadstone.initModel();
        solarPanelRedstone.initModel();
        solarPanelResonant.initModel();
        solarPanelUltimate.initModel();
    }

    public static void register(IForgeRegistry<Block> registry) {
    	
    	registry.register(new SolarPanel(SolarPanelLevel.Advanced));
    	registry.register(new SolarPanel(SolarPanelLevel.Hardened));
    	registry.register(new SolarPanel(SolarPanelLevel.Leadstone));
    	registry.register(new SolarPanel(SolarPanelLevel.Redstone));
    	registry.register(new SolarPanel(SolarPanelLevel.Resonant));
        registry.register(new SolarPanel(SolarPanelLevel.Ultimate));
        
        GameRegistry.registerTileEntity(TileEntityAdvancedSolarPanel.class, Main.MODID + ":solar_panel_advanced");
        GameRegistry.registerTileEntity(TileEntityHardenedSolarPanel.class, Main.MODID + ":solar_panel_hardened");
        GameRegistry.registerTileEntity(TileEntityLeadstoneSolarPanel.class, Main.MODID + ":solar_panel_leadstone");
        GameRegistry.registerTileEntity(TileEntityRedstoneSolarPanel.class, Main.MODID + ":solar_panel_redstone");
        GameRegistry.registerTileEntity(TileEntityResonantSolarPanel.class, Main.MODID + ":solar_panel_resonant");
        GameRegistry.registerTileEntity(TileEntityUltimateSolarPanel.class, Main.MODID + ":solar_panel_ultimate");
    }

}

