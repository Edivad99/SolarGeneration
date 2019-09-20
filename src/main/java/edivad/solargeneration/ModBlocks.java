package edivad.solargeneration;

import edivad.solargeneration.blocks.SolarPanel;
import edivad.solargeneration.blocks.containers.SolarPanelAdvancedContainer;
import edivad.solargeneration.tile.TileEntityAdvancedSolarPanel;
import edivad.solargeneration.tile.TileEntityHardenedSolarPanel;
import edivad.solargeneration.tile.TileEntityLeadstoneSolarPanel;
import edivad.solargeneration.tile.TileEntityRedstoneSolarPanel;
import edivad.solargeneration.tile.TileEntityResonantSolarPanel;
import edivad.solargeneration.tile.TileEntitySignalumSolarPanel;
import edivad.solargeneration.tile.TileEntityUltimateSolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
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
	
	//Container
	@ObjectHolder(Main.MODID + ":solar_panel_advanced")
	public static ContainerType<SolarPanelAdvancedContainer> solarPanelAdvancedContainer;
	/*@ObjectHolder(Main.MODID + ":solar_panel_hardened")
	public static ContainerType<SolarPanelHardenedContainer> solarPanelHardenedContainer;
	@ObjectHolder(Main.MODID + ":solar_panel_leadstone")
	public static ContainerType<SolarPanelLeadstoneContainer> solarPanelLeadstoneContainer;
	@ObjectHolder(Main.MODID + ":solar_panel_redstone")
	public static ContainerType<SolarPanelRedstoneContainer> solarPanelRedstoneContainer;
	@ObjectHolder(Main.MODID + ":solar_panel_resonant")
	public static ContainerType<SolarPanelResonantContainer> solarPanelResonantContainer;
	@ObjectHolder(Main.MODID + ":solar_panel_signalum")
	public static ContainerType<SolarPanelSignalumContainer> solarPanelSignalumContainer;
	@ObjectHolder(Main.MODID + ":solar_panel_ultimate")
	public static ContainerType<SolarPanelUltimateContainer> solarPanelUltimateContainer;*/

	public static TileEntityType<?> ADVANCED;
	public static TileEntityType<?> HARDENED;
	public static TileEntityType<?> LEADSTONE;
	public static TileEntityType<?> REDSTONE;
	public static TileEntityType<?> RESONANT;
	public static TileEntityType<?> SIGNALUM;
	public static TileEntityType<?> ULTIMATE;

	public static void registerTiles(IForgeRegistry<TileEntityType<?>> registry)
	{
		registry.register(ADVANCED = TileEntityType.Builder.create(TileEntityAdvancedSolarPanel::new).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Advanced)));
		registry.register(HARDENED = TileEntityType.Builder.create(TileEntityHardenedSolarPanel::new).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Hardened)));
		registry.register(LEADSTONE = TileEntityType.Builder.create(TileEntityLeadstoneSolarPanel::new).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Leadstone)));
		registry.register(REDSTONE = TileEntityType.Builder.create(TileEntityRedstoneSolarPanel::new).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Redstone)));
		registry.register(RESONANT = TileEntityType.Builder.create(TileEntityResonantSolarPanel::new).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Resonant)));
		registry.register(SIGNALUM = TileEntityType.Builder.create(TileEntitySignalumSolarPanel::new).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Signalum)));
		registry.register(ULTIMATE = TileEntityType.Builder.create(TileEntityUltimateSolarPanel::new).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Ultimate)));
	}
	
	public static void registerContainers(IForgeRegistry<ContainerType<?>> registry)
	{
		registry.register(IForgeContainerType.create((windowId, inv, data) -> {
			BlockPos pos = data.readBlockPos();
			return new SolarPanelAdvancedContainer(windowId, Main.proxy.getClientWorld(), pos);
		}).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Advanced)));
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
