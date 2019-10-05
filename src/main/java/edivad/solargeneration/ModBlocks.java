package edivad.solargeneration;

import edivad.solargeneration.blocks.SolarPanel;
import edivad.solargeneration.tile.TileEntityAdvancedSolarPanel;
import edivad.solargeneration.tile.TileEntityHardenedSolarPanel;
import edivad.solargeneration.tile.TileEntityLeadstoneSolarPanel;
import edivad.solargeneration.tile.TileEntityRedstoneSolarPanel;
import edivad.solargeneration.tile.TileEntityResonantSolarPanel;
import edivad.solargeneration.tile.TileEntitySignalumSolarPanel;
import edivad.solargeneration.tile.TileEntityUltimateSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	// Solar panel
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
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_panel_signalum")
	public static SolarPanel solarPanelSignalum;
	@GameRegistry.ObjectHolder(Main.MODID + ":solar_panel_ultimate")
	public static SolarPanel solarPanelUltimate;

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		solarPanelAdvanced.initModel();
		solarPanelHardened.initModel();
		solarPanelLeadstone.initModel();
		solarPanelRedstone.initModel();
		solarPanelResonant.initModel();
		solarPanelSignalum.initModel();
		solarPanelUltimate.initModel();
	}

	public static void register(IForgeRegistry<Block> registry)
	{
		for(SolarPanelLevel level : SolarPanelLevel.values())
			registry.register(new SolarPanel(level));

		GameRegistry.registerTileEntity(TileEntityAdvancedSolarPanel.class, Main.MODID + ":solar_panel_advanced");
		GameRegistry.registerTileEntity(TileEntityHardenedSolarPanel.class, Main.MODID + ":solar_panel_hardened");
		GameRegistry.registerTileEntity(TileEntityLeadstoneSolarPanel.class, Main.MODID + ":solar_panel_leadstone");
		GameRegistry.registerTileEntity(TileEntityRedstoneSolarPanel.class, Main.MODID + ":solar_panel_redstone");
		GameRegistry.registerTileEntity(TileEntityResonantSolarPanel.class, Main.MODID + ":solar_panel_resonant");
		GameRegistry.registerTileEntity(TileEntitySignalumSolarPanel.class, Main.MODID + ":solar_panel_signalum");
		GameRegistry.registerTileEntity(TileEntityUltimateSolarPanel.class, Main.MODID + ":solar_panel_ultimate");
	}

}
