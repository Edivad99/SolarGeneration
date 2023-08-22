package edivad.solargeneration.setup;

import java.util.HashMap;
import java.util.Map;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import edivad.solargeneration.blocks.SolarPanelBlock;
import edivad.solargeneration.items.SolarHelmet;
import edivad.solargeneration.lootable.SolarPanelLootFunction;
import edivad.solargeneration.menu.SolarPanelMenu;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {

  public static final Map<SolarPanelLevel, RegistryObject<SolarPanelBlock>> SOLAR_PANEL_BLOCK = new HashMap<>();
  public static final Map<SolarPanelLevel, RegistryObject<Item>> SOLAR_PANEL_ITEM = new HashMap<>();
  public static final Map<SolarPanelLevel, RegistryObject<BlockEntityType<SolarPanelBlockEntity>>> SOLAR_PANEL_TILE = new HashMap<>();
  public static final Map<SolarPanelLevel, RegistryObject<MenuType<SolarPanelMenu>>> SOLAR_PANEL_CONTAINER = new HashMap<>();
  public static final Map<SolarPanelLevel, RegistryObject<Item>> HELMET = new HashMap<>();
  public static final Map<SolarPanelLevel, RegistryObject<Item>> CORE = new HashMap<>();
  public static final Map<String, RegistryObject<LootItemFunctionType>> REGISTERED_LOOT_ITEM_FUNCTIONS = new HashMap<>();
  private static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, SolarGeneration.ID);
  private static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, SolarGeneration.ID);
  public static final RegistryObject<Item> LAPIS_SHARD =
      ITEMS.register("lapis_shard", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> PHOTOVOLTAIC_CELL =
      ITEMS.register("photovoltaic_cell", () -> new Item(new Item.Properties()));
  private static final DeferredRegister<BlockEntityType<?>> TILES =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SolarGeneration.ID);
  private static final DeferredRegister<MenuType<?>> CONTAINERS =
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, SolarGeneration.ID);
  private static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTIONS =
      DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, SolarGeneration.ID);

  public static void init(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    TILES.register(modEventBus);
    CONTAINERS.register(modEventBus);
    LOOT_ITEM_FUNCTIONS.register(modEventBus);

    for (SolarPanelLevel level : SolarPanelLevel.values()) {
      SOLAR_PANEL_BLOCK.put(level,
          BLOCKS.register(level.getSolarPanelName(), () -> new SolarPanelBlock(level,
              BlockBehaviour.Properties.of()
                  .sound(SoundType.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(1.5F, 6.0F))));
      SOLAR_PANEL_ITEM.put(level, ITEMS.register(level.getSolarPanelName(),
          () -> new BlockItem(SOLAR_PANEL_BLOCK.get(level).get(), new Item.Properties())));
      SOLAR_PANEL_TILE.put(level, TILES.register(level.getSolarPanelName(),
          () -> BlockEntityType.Builder.of(
              (pos, state) -> new SolarPanelBlockEntity(level, pos, state),
              SOLAR_PANEL_BLOCK.get(level).get()).build(null)));
      SOLAR_PANEL_CONTAINER.put(level, CONTAINERS.register(level.getSolarPanelName(),
          () -> IForgeMenuType.create((windowId, inv, data) -> {
            var pos = data.readBlockPos();
            var blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
            if (!(blockEntity instanceof SolarPanelBlockEntity solarPanelBlockEntity)) {
              SolarGeneration.LOGGER
                  .error("Wrong type of block entity (expected BlockEntitySolarPanel)!");
              return null;
            }
            return new SolarPanelMenu(windowId, solarPanelBlockEntity, level);
          })));

      HELMET.put(level, ITEMS.register(level.getSolarHelmetName(),
          () -> new SolarHelmet(level, new Item.Properties().stacksTo(1))));
      CORE.put(level,
          ITEMS.register(level.getSolarCoreName(), () -> new Item(new Item.Properties())));
    }

    REGISTERED_LOOT_ITEM_FUNCTIONS.put("solar_panel", LOOT_ITEM_FUNCTIONS
        .register("solar_panel",
            () -> new LootItemFunctionType(new SolarPanelLootFunction.Serializer())));
  }
}