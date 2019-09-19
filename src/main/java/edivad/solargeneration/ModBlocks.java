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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

	// Solar panel
	@ObjectHolder(Main.MODID + ":solar_panel_advanced")
	public static SolarPanel solarPanelAdvanced;
	@ObjectHolder(Main.MODID + ":solar_panel_hardened")
	public static SolarPanel solarPanelHardened;
	@ObjectHolder(Main.MODID + ":solar_panel_leadstone")
	public static SolarPanel solarPanelLeadstone;
	@ObjectHolder(Main.MODID + ":solar_panel_redstone")
	public static SolarPanel solarPanelRedstone;
	@ObjectHolder(Main.MODID + ":solar_panel_resonant")
	public static SolarPanel solarPanelResonant;
	@ObjectHolder(Main.MODID + ":solar_panel_signalum")
	public static SolarPanel solarPanelSignalum;
	@ObjectHolder(Main.MODID + ":solar_panel_ultimate")
	public static SolarPanel solarPanelUltimate;

	public static TileEntityType<?> ADVANCED;
	public static TileEntityType<?> HARDENED;
	public static TileEntityType<?> LEADSTONE;
	public static TileEntityType<?> REDSTONE;
	public static TileEntityType<?> RESONANT;
	public static TileEntityType<?> SIGNALUM;
	public static TileEntityType<?> ULTIMATE;

	public static void registerTiles(IForgeRegistry<TileEntityType<?>> registry)
	{
		registry.register(ADVANCED = TileEntityType.Builder.create(TileEntityAdvancedSolarPanel::new).build(null).setRegistryName(new ResourceLocation(Main.MODID, "solar_panel_advanced")));
		registry.register(HARDENED = TileEntityType.Builder.create(TileEntityHardenedSolarPanel::new).build(null).setRegistryName(new ResourceLocation(Main.MODID, "solar_panel_hardened")));
		registry.register(LEADSTONE = TileEntityType.Builder.create(TileEntityLeadstoneSolarPanel::new).build(null).setRegistryName(new ResourceLocation(Main.MODID, "solar_panel_leadstone")));
		registry.register(REDSTONE = TileEntityType.Builder.create(TileEntityRedstoneSolarPanel::new).build(null).setRegistryName(new ResourceLocation(Main.MODID, "solar_panel_redstone")));
		registry.register(RESONANT = TileEntityType.Builder.create(TileEntityResonantSolarPanel::new).build(null).setRegistryName(new ResourceLocation(Main.MODID, "solar_panel_resonant")));
		registry.register(SIGNALUM = TileEntityType.Builder.create(TileEntitySignalumSolarPanel::new).build(null).setRegistryName(new ResourceLocation(Main.MODID, "solar_panel_signalum")));
		registry.register(ULTIMATE = TileEntityType.Builder.create(TileEntityUltimateSolarPanel::new).build(null).setRegistryName(new ResourceLocation(Main.MODID, "solar_panel_ultimate")));
	}

	public static void register(IForgeRegistry<Block> registry)
	{
		registry.register(new SolarPanel(SolarPanelLevel.Advanced));
		registry.register(new SolarPanel(SolarPanelLevel.Hardened));
		registry.register(new SolarPanel(SolarPanelLevel.Leadstone));
		registry.register(new SolarPanel(SolarPanelLevel.Redstone));
		registry.register(new SolarPanel(SolarPanelLevel.Resonant));
		registry.register(new SolarPanel(SolarPanelLevel.Signalum));
		registry.register(new SolarPanel(SolarPanelLevel.Ultimate));
	}
}
