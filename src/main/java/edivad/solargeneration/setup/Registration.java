package edivad.solargeneration.setup;

import edivad.solargeneration.Main;
import edivad.solargeneration.blockentity.BlockEntitySolarPanel;
import edivad.solargeneration.blocks.SolarPanel;
import edivad.solargeneration.items.SolarHelmet;
import edivad.solargeneration.lootable.SolarPanelLootFunction;
import edivad.solargeneration.menu.SolarPanelMenu;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Main.MODID);
    private static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTIONS = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, Main.MODID);

    private static final Item.Properties PROPERTY = new Item.Properties().tab(ModSetup.solarGenerationTab);

    public static final Map<SolarPanelLevel, RegistryObject<SolarPanel>> SOLAR_PANEL_BLOCK = new HashMap<>();
    public static final Map<SolarPanelLevel, RegistryObject<Item>> SOLAR_PANEL_ITEM = new HashMap<>();
    public static final Map<SolarPanelLevel, RegistryObject<BlockEntityType<BlockEntitySolarPanel>>> SOLAR_PANEL_TILE = new HashMap<>();
    public static final Map<SolarPanelLevel, RegistryObject<MenuType<SolarPanelMenu>>> SOLAR_PANEL_CONTAINER = new HashMap<>();

    public static final Map<SolarPanelLevel, RegistryObject<Item>> HELMET = new HashMap<>();
    public static final Map<SolarPanelLevel, RegistryObject<Item>> CORE = new HashMap<>();
    public static final Map<String, RegistryObject<LootItemFunctionType>> REGISTERED_LOOT_ITEM_FUNCTIONS = new HashMap<>();

    public static final RegistryObject<Item> LAPIS_SHARD = ITEMS.register("lapis_shard", () -> new Item(PROPERTY));
    public static final RegistryObject<Item> PHOTOVOLTAIC_CELL = ITEMS.register("photovoltaic_cell", () -> new Item(PROPERTY));

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TILES.register(eventBus);
        CONTAINERS.register(eventBus);
        LOOT_ITEM_FUNCTIONS.register(eventBus);

        for(SolarPanelLevel level : SolarPanelLevel.values()) {
            SOLAR_PANEL_BLOCK.put(level, BLOCKS.register(level.getSolarPanelName(), () -> new SolarPanel(level)));
            SOLAR_PANEL_ITEM.put(level, ITEMS.register(level.getSolarPanelName(), () -> new BlockItem(SOLAR_PANEL_BLOCK.get(level).get(), PROPERTY)));
            SOLAR_PANEL_TILE.put(level, TILES.register(level.getSolarPanelName(), () -> BlockEntityType.Builder.of((pos, state) -> new BlockEntitySolarPanel(level, pos, state), SOLAR_PANEL_BLOCK.get(level).get()).build(null)));
            SOLAR_PANEL_CONTAINER.put(level, CONTAINERS.register(level.getSolarPanelName(), () -> IForgeMenuType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity te = inv.player.getCommandSenderWorld().getBlockEntity(pos);
                if(!(te instanceof BlockEntitySolarPanel tile)) {
                    Main.LOGGER.error("Wrong type of block entity (expected BlockEntitySolarPanel)!");
                    return null;
                }
                return new SolarPanelMenu(windowId, tile, level);
            })));

            HELMET.put(level, ITEMS.register(level.getSolarHelmetName(), () -> new SolarHelmet(level)));
            CORE.put(level, ITEMS.register(level.getSolarCoreName(), () -> new Item(PROPERTY)));
        }

        REGISTERED_LOOT_ITEM_FUNCTIONS.put("solar_panel", LOOT_ITEM_FUNCTIONS.register("solar_panel", () -> new LootItemFunctionType(new SolarPanelLootFunction.Serializer())));
    }
}