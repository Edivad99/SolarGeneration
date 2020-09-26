package edivad.solargeneration.setup;

import edivad.solargeneration.Main;
import edivad.solargeneration.blocks.SolarPanel;
import edivad.solargeneration.container.SolarPanelContainer;
import edivad.solargeneration.items.SolarHelmet;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Main.MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MODID);

    public static final Map<SolarPanelLevel, RegistryObject<SolarPanel>> SOLAR_PANEL_BLOCK = new HashMap<>();
    public static final Map<SolarPanelLevel, RegistryObject<Item>> SOLAR_PANEL_ITEM = new HashMap<>();
    public static final Map<SolarPanelLevel, RegistryObject<TileEntityType<TileEntitySolarPanel>>> SOLAR_PANEL_TILE = new HashMap<>();
    public static final Map<SolarPanelLevel, RegistryObject<ContainerType<SolarPanelContainer>>> SOLAR_PANEL_CONTAINER = new HashMap<>();

    public static final Map<SolarPanelLevel, RegistryObject<Item>> HELMET = new HashMap<>();
    public static final Map<SolarPanelLevel, RegistryObject<Item>> CORE = new HashMap<>();

    private static final Item.Properties property = new Item.Properties().group(ModSetup.solarGenerationTab);
    public static final RegistryObject<Item> LAPIS_SHARD = ITEMS.register("lapis_shard", () -> new Item(property));
    public static final RegistryObject<Item> PHOTOVOLTAIC_CELL = ITEMS.register("photovoltaic_cell", () -> new Item(property));

    public static void init()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        for(SolarPanelLevel level : SolarPanelLevel.values())
        {
            SOLAR_PANEL_BLOCK.put(level, BLOCKS.register(level.getSolarPanelName(), () -> new SolarPanel(level)));
            SOLAR_PANEL_ITEM.put(level, ITEMS.register(level.getSolarPanelName(), () -> new BlockItem(SOLAR_PANEL_BLOCK.get(level).get(), property)));
            SOLAR_PANEL_TILE.put(level, TILES.register(level.getSolarPanelName(), () -> TileEntityType.Builder.create(() -> new TileEntitySolarPanel(level), SOLAR_PANEL_BLOCK.get(level).get()).build(null)));
            SOLAR_PANEL_CONTAINER.put(level, CONTAINERS.register(level.getSolarPanelName(), () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity te = inv.player.getEntityWorld().getTileEntity(pos);
                if(!(te instanceof TileEntitySolarPanel))
                {
                    Main.logger.error("Wrong type of tile entity (expected TileEntitySolarPanel)!");
                    return null;
                }
                TileEntitySolarPanel tile = (TileEntitySolarPanel) te;
                return new SolarPanelContainer(windowId, inv.player, tile, level);
            })));

            HELMET.put(level, ITEMS.register(level.getSolarHelmetName(), () -> new SolarHelmet(level)));
            CORE.put(level, ITEMS.register(level.getSolarCoreName(), () -> new Item(property)));
        }
    }

}