package edivad.solargeneration;

import edivad.solargeneration.blocks.SolarPanel;
import edivad.solargeneration.blocks.containers.SolarPanelAdvancedContainer;
import edivad.solargeneration.blocks.containers.SolarPanelHardenedContainer;
import edivad.solargeneration.blocks.containers.SolarPanelLeadstoneContainer;
import edivad.solargeneration.blocks.containers.SolarPanelRedstoneContainer;
import edivad.solargeneration.blocks.containers.SolarPanelResonantContainer;
import edivad.solargeneration.blocks.containers.SolarPanelSignalumContainer;
import edivad.solargeneration.blocks.containers.SolarPanelUltimateContainer;
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

	// Container
	@ObjectHolder(Main.MODID + ":solar_panel_advanced")
	public static ContainerType<SolarPanelAdvancedContainer> solarPanelAdvancedContainer;
	@ObjectHolder(Main.MODID + ":solar_panel_hardened")
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
	public static ContainerType<SolarPanelUltimateContainer> solarPanelUltimateContainer;

	// TilEntity
	@ObjectHolder(Main.MODID + ":solar_panel_advanced")
	public static TileEntityType<?> ADVANCED;
	@ObjectHolder(Main.MODID + ":solar_panel_hardened")
	public static TileEntityType<?> HARDENED;
	@ObjectHolder(Main.MODID + ":solar_panel_leadstone")
	public static TileEntityType<?> LEADSTONE;
	@ObjectHolder(Main.MODID + ":solar_panel_redstone")
	public static TileEntityType<?> REDSTONE;
	@ObjectHolder(Main.MODID + ":solar_panel_resonant")
	public static TileEntityType<?> RESONANT;
	@ObjectHolder(Main.MODID + ":solar_panel_signalum")
	public static TileEntityType<?> SIGNALUM;
	@ObjectHolder(Main.MODID + ":solar_panel_ultimate")
	public static TileEntityType<?> ULTIMATE;

	public static void registerTiles(IForgeRegistry<TileEntityType<?>> registry)
	{
		registry.register(TileEntityType.Builder.create(TileEntityAdvancedSolarPanel::new, ModBlocks.solarPanelAdvanced).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Advanced)));
		registry.register(TileEntityType.Builder.create(TileEntityHardenedSolarPanel::new, ModBlocks.solarPanelHardened).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Hardened)));
		registry.register(TileEntityType.Builder.create(TileEntityLeadstoneSolarPanel::new, ModBlocks.solarPanelLeadstone).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Leadstone)));
		registry.register(TileEntityType.Builder.create(TileEntityRedstoneSolarPanel::new, ModBlocks.solarPanelRedstone).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Redstone)));
		registry.register(TileEntityType.Builder.create(TileEntityResonantSolarPanel::new, ModBlocks.solarPanelResonant).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Resonant)));
		registry.register(TileEntityType.Builder.create(TileEntitySignalumSolarPanel::new, ModBlocks.solarPanelSignalum).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Signalum)));
		registry.register(TileEntityType.Builder.create(TileEntityUltimateSolarPanel::new, ModBlocks.solarPanelUltimate).build(null).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Ultimate)));
	}

	public static void registerContainers(IForgeRegistry<ContainerType<?>> registry)
	{
		registry.register(IForgeContainerType.create((windowId, inv, data) ->
		{
			BlockPos pos = data.readBlockPos();
			return new SolarPanelAdvancedContainer(windowId, Main.proxy.getClientWorld(), pos, Main.proxy.getClientPlayer());
		}).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Advanced)));

		registry.register(IForgeContainerType.create((windowId, inv, data) ->
		{
			BlockPos pos = data.readBlockPos();
			return new SolarPanelHardenedContainer(windowId, Main.proxy.getClientWorld(), pos, Main.proxy.getClientPlayer());
		}).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Hardened)));

		registry.register(IForgeContainerType.create((windowId, inv, data) ->
		{
			BlockPos pos = data.readBlockPos();
			return new SolarPanelLeadstoneContainer(windowId, Main.proxy.getClientWorld(), pos, Main.proxy.getClientPlayer());
		}).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Leadstone)));

		registry.register(IForgeContainerType.create((windowId, inv, data) ->
		{
			BlockPos pos = data.readBlockPos();
			return new SolarPanelRedstoneContainer(windowId, Main.proxy.getClientWorld(), pos, Main.proxy.getClientPlayer());
		}).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Redstone)));

		registry.register(IForgeContainerType.create((windowId, inv, data) ->
		{
			BlockPos pos = data.readBlockPos();
			return new SolarPanelResonantContainer(windowId, Main.proxy.getClientWorld(), pos, Main.proxy.getClientPlayer());
		}).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Resonant)));

		registry.register(IForgeContainerType.create((windowId, inv, data) ->
		{
			BlockPos pos = data.readBlockPos();
			return new SolarPanelSignalumContainer(windowId, Main.proxy.getClientWorld(), pos, Main.proxy.getClientPlayer());
		}).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Signalum)));

		registry.register(IForgeContainerType.create((windowId, inv, data) ->
		{
			BlockPos pos = data.readBlockPos();
			return new SolarPanelUltimateContainer(windowId, Main.proxy.getClientWorld(), pos, Main.proxy.getClientPlayer());
		}).setRegistryName(SolarPanel.getResourceLocation(SolarPanelLevel.Ultimate)));
	}

	public static void register(IForgeRegistry<Block> registry)
	{
		for(SolarPanelLevel level : SolarPanelLevel.values())
			registry.register(new SolarPanel(level));
	}
}
