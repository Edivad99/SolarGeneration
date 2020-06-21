package edivad.solargeneration.setup;

import edivad.solargeneration.Main;
import edivad.solargeneration.blocks.SolarPanel;
import edivad.solargeneration.blocks.containers.SolarPanelAdvancedContainer;
import edivad.solargeneration.blocks.containers.SolarPanelHardenedContainer;
import edivad.solargeneration.blocks.containers.SolarPanelLeadstoneContainer;
import edivad.solargeneration.blocks.containers.SolarPanelRedstoneContainer;
import edivad.solargeneration.blocks.containers.SolarPanelResonantContainer;
import edivad.solargeneration.blocks.containers.SolarPanelSignalumContainer;
import edivad.solargeneration.blocks.containers.SolarPanelUltimateContainer;
import edivad.solargeneration.items.SolarHelmet;
import edivad.solargeneration.items.Wrench;
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
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {

	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
	private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Main.MODID);
	private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MODID);

	public static void init()
	{
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	private static final Item.Properties property = new Item.Properties().group(ModSetup.solarGenerationTab);

	public static final RegistryObject<SolarPanel> LEADSTONE = BLOCKS.register("solar_panel_leadstone", () -> new SolarPanel(SolarPanelLevel.Leadstone));
	public static final RegistryObject<Item> LEADSTONE_ITEM = ITEMS.register("solar_panel_leadstone", () -> new BlockItem(LEADSTONE.get(), property));
	public static final RegistryObject<TileEntityType<TileEntityLeadstoneSolarPanel>> LEADSTONE_TILE = TILES.register("solar_panel_leadstone", () -> TileEntityType.Builder.create(TileEntityLeadstoneSolarPanel::new, LEADSTONE.get()).build(null));
	public static final RegistryObject<ContainerType<SolarPanelLeadstoneContainer>> LEADSTONE_CONTAINER = CONTAINERS.register("solar_panel_leadstone", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return new SolarPanelLeadstoneContainer(windowId, Main.proxy.getClientWorld(), data.readBlockPos(), Main.proxy.getClientPlayer());
	}));

	public static final RegistryObject<SolarPanel> HARDENED = BLOCKS.register("solar_panel_hardened", () -> new SolarPanel(SolarPanelLevel.Hardened));
	public static final RegistryObject<Item> HARDENED_ITEM = ITEMS.register("solar_panel_hardened", () -> new BlockItem(HARDENED.get(), property));
	public static final RegistryObject<TileEntityType<TileEntityHardenedSolarPanel>> HARDENED_TILE = TILES.register("solar_panel_hardened", () -> TileEntityType.Builder.create(TileEntityHardenedSolarPanel::new, HARDENED.get()).build(null));
	public static final RegistryObject<ContainerType<SolarPanelHardenedContainer>> HARDENED_CONTAINER = CONTAINERS.register("solar_panel_hardened", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return new SolarPanelHardenedContainer(windowId, Main.proxy.getClientWorld(), data.readBlockPos(), Main.proxy.getClientPlayer());
	}));

	public static final RegistryObject<SolarPanel> REDSTONE = BLOCKS.register("solar_panel_redstone", () -> new SolarPanel(SolarPanelLevel.Redstone));
	public static final RegistryObject<Item> REDSTONE_ITEM = ITEMS.register("solar_panel_redstone", () -> new BlockItem(REDSTONE.get(), property));
	public static final RegistryObject<TileEntityType<TileEntityRedstoneSolarPanel>> REDSTONE_TILE = TILES.register("solar_panel_redstone", () -> TileEntityType.Builder.create(TileEntityRedstoneSolarPanel::new, REDSTONE.get()).build(null));
	public static final RegistryObject<ContainerType<SolarPanelRedstoneContainer>> REDSTONE_CONTAINER = CONTAINERS.register("solar_panel_redstone", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return new SolarPanelRedstoneContainer(windowId, Main.proxy.getClientWorld(), data.readBlockPos(), Main.proxy.getClientPlayer());
	}));

	public static final RegistryObject<SolarPanel> SIGNALUM = BLOCKS.register("solar_panel_signalum", () -> new SolarPanel(SolarPanelLevel.Signalum));
	public static final RegistryObject<Item> SIGNALUM_ITEM = ITEMS.register("solar_panel_signalum", () -> new BlockItem(SIGNALUM.get(), property));
	public static final RegistryObject<TileEntityType<TileEntitySignalumSolarPanel>> SIGNALUM_TILE = TILES.register("solar_panel_signalum", () -> TileEntityType.Builder.create(TileEntitySignalumSolarPanel::new, SIGNALUM.get()).build(null));
	public static final RegistryObject<ContainerType<SolarPanelSignalumContainer>> SIGNALUM_CONTAINER = CONTAINERS.register("solar_panel_signalum", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return new SolarPanelSignalumContainer(windowId, Main.proxy.getClientWorld(), data.readBlockPos(), Main.proxy.getClientPlayer());
	}));

	public static final RegistryObject<SolarPanel> RESONANT = BLOCKS.register("solar_panel_resonant", () -> new SolarPanel(SolarPanelLevel.Resonant));
	public static final RegistryObject<Item> RESONANT_ITEM = ITEMS.register("solar_panel_resonant", () -> new BlockItem(RESONANT.get(), property));
	public static final RegistryObject<TileEntityType<TileEntityResonantSolarPanel>> RESONANT_TILE = TILES.register("solar_panel_resonant", () -> TileEntityType.Builder.create(TileEntityResonantSolarPanel::new, RESONANT.get()).build(null));
	public static final RegistryObject<ContainerType<SolarPanelResonantContainer>> RESONANT_CONTAINER = CONTAINERS.register("solar_panel_resonant", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return new SolarPanelResonantContainer(windowId, Main.proxy.getClientWorld(), data.readBlockPos(), Main.proxy.getClientPlayer());
	}));

	public static final RegistryObject<SolarPanel> ADVANCED = BLOCKS.register("solar_panel_advanced", () -> new SolarPanel(SolarPanelLevel.Advanced));
	public static final RegistryObject<Item> ADVANCED_ITEM = ITEMS.register("solar_panel_advanced", () -> new BlockItem(ADVANCED.get(), property));
	public static final RegistryObject<TileEntityType<TileEntityAdvancedSolarPanel>> ADVANCED_TILE = TILES.register("solar_panel_advanced", () -> TileEntityType.Builder.create(TileEntityAdvancedSolarPanel::new, ADVANCED.get()).build(null));
	public static final RegistryObject<ContainerType<SolarPanelAdvancedContainer>> ADVANCED_CONTAINER = CONTAINERS.register("solar_panel_advanced", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return new SolarPanelAdvancedContainer(windowId, Main.proxy.getClientWorld(), data.readBlockPos(), Main.proxy.getClientPlayer());
	}));

	public static final RegistryObject<SolarPanel> ULTIMATE = BLOCKS.register("solar_panel_ultimate", () -> new SolarPanel(SolarPanelLevel.Ultimate));
	public static final RegistryObject<Item> ULTIMATE_ITEM = ITEMS.register("solar_panel_ultimate", () -> new BlockItem(ULTIMATE.get(), property));
	public static final RegistryObject<TileEntityType<TileEntityUltimateSolarPanel>> ULTIMATE_TILE = TILES.register("solar_panel_ultimate", () -> TileEntityType.Builder.create(TileEntityUltimateSolarPanel::new, ULTIMATE.get()).build(null));
	public static final RegistryObject<ContainerType<SolarPanelUltimateContainer>> ULTIMATE_CONTAINER = CONTAINERS.register("solar_panel_ultimate", () -> IForgeContainerType.create((windowId, inv, data) ->
	{
		return new SolarPanelUltimateContainer(windowId, Main.proxy.getClientWorld(), data.readBlockPos(), Main.proxy.getClientPlayer());
	}));

	public static final RegistryObject<Item> LEADSTONE_HELMET = ITEMS.register("solar_helmet_leadstone", () -> new SolarHelmet(SolarPanelLevel.Leadstone));
	public static final RegistryObject<Item> HARDENED_HELMET = ITEMS.register("solar_helmet_hardened", () -> new SolarHelmet(SolarPanelLevel.Hardened));
	public static final RegistryObject<Item> REDSTONE_HELMET = ITEMS.register("solar_helmet_redstone", () -> new SolarHelmet(SolarPanelLevel.Redstone));
	public static final RegistryObject<Item> SIGNALUM_HELMET = ITEMS.register("solar_helmet_signalum", () -> new SolarHelmet(SolarPanelLevel.Signalum));
	public static final RegistryObject<Item> RESONANT_HELMET = ITEMS.register("solar_helmet_resonant", () -> new SolarHelmet(SolarPanelLevel.Resonant));
	public static final RegistryObject<Item> ADVANCED_HELMET = ITEMS.register("solar_helmet_advanced", () -> new SolarHelmet(SolarPanelLevel.Advanced));
	public static final RegistryObject<Item> ULTIMATE_HELMET = ITEMS.register("solar_helmet_ultimate", () -> new SolarHelmet(SolarPanelLevel.Ultimate));

	public static final RegistryObject<Item> LEADSTONE_CORE = ITEMS.register("solar_core_leadstone", () -> new Item(property));
	public static final RegistryObject<Item> HARDENED_CORE = ITEMS.register("solar_core_hardened", () -> new Item(property));
	public static final RegistryObject<Item> REDSTONE_CORE = ITEMS.register("solar_core_redstone", () -> new Item(property));
	public static final RegistryObject<Item> SIGNALUM_CORE = ITEMS.register("solar_core_signalum", () -> new Item(property));
	public static final RegistryObject<Item> RESONANT_CORE = ITEMS.register("solar_core_resonant", () -> new Item(property));
	public static final RegistryObject<Item> ADVANCED_CORE = ITEMS.register("solar_core_advanced", () -> new Item(property));
	public static final RegistryObject<Item> ULTIMATE_CORE = ITEMS.register("solar_core_ultimate", () -> new Item(property));

	public static final RegistryObject<Item> LAPIS_SHARD = ITEMS.register("lapis_shard", () -> new Item(property));
	public static final RegistryObject<Item> PHOTOVOLTAIC_CELL = ITEMS.register("photovoltaic_cell", () -> new Item(property));
	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", Wrench::new);
}