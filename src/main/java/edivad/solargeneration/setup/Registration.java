package edivad.solargeneration.setup;

import java.util.HashMap;
import java.util.Map;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import edivad.solargeneration.blocks.SolarPanelBlock;
import edivad.solargeneration.items.SolarHelmet;
import edivad.solargeneration.menu.SolarPanelMenu;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registration {

  public static final Map<SolarPanelLevel, DeferredBlock<SolarPanelBlock>> SOLAR_PANEL_BLOCK = new HashMap<>();
  public static final Map<SolarPanelLevel, DeferredItem<Item>> SOLAR_PANEL_ITEM = new HashMap<>();
  public static final Map<SolarPanelLevel, DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarPanelBlockEntity>>> SOLAR_PANEL_BLOCK_ENTITY = new HashMap<>();
  public static final Map<SolarPanelLevel, DeferredHolder<MenuType<?>, MenuType<SolarPanelMenu>>> SOLAR_PANEL_MENU = new HashMap<>();
  public static final Map<SolarPanelLevel, DeferredItem<Item>> HELMET = new HashMap<>();
  public static final Map<SolarPanelLevel, DeferredItem<Item>> CORE = new HashMap<>();
  private static final DeferredRegister.Blocks BLOCKS =
      DeferredRegister.createBlocks(SolarGeneration.ID);
  private static final DeferredRegister.Items ITEMS =
      DeferredRegister.createItems(SolarGeneration.ID);
  public static final DeferredItem<Item> LAPIS_SHARD =
      ITEMS.register("lapis_shard", () -> new Item(new Item.Properties()));
  public static final DeferredItem<Item> PHOTOVOLTAIC_CELL =
      ITEMS.register("photovoltaic_cell", () -> new Item(new Item.Properties()));
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SolarGeneration.ID);
  private static final DeferredRegister<MenuType<?>> MENU =
      DeferredRegister.create(BuiltInRegistries.MENU, SolarGeneration.ID);

  public static void init(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    BLOCK_ENTITIES.register(modEventBus);
    MENU.register(modEventBus);

    for (SolarPanelLevel level : SolarPanelLevel.values()) {
      SOLAR_PANEL_BLOCK.put(level,
          BLOCKS.register(level.getSolarPanelName(), () -> new SolarPanelBlock(level,
              BlockBehaviour.Properties.of()
                  .sound(SoundType.METAL)
                  .requiresCorrectToolForDrops()
                  .strength(1.5F, 6.0F))));

      SOLAR_PANEL_ITEM.put(level, ITEMS.register(level.getSolarPanelName(),
          () -> new BlockItem(SOLAR_PANEL_BLOCK.get(level).get(), new Item.Properties())));

      SOLAR_PANEL_BLOCK_ENTITY.put(level, BLOCK_ENTITIES.register(level.getSolarPanelName(),
          () -> BlockEntityType.Builder.of((pos, state) -> new SolarPanelBlockEntity(level, pos, state),
              SOLAR_PANEL_BLOCK.get(level).get()).build(null)));

      SOLAR_PANEL_MENU.put(level, MENU.register(level.getSolarPanelName(),
          () -> new MenuType<>((IContainerFactory<SolarPanelMenu>) (id, inventory, buf) -> {
            var pos = buf.readBlockPos();
            var blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
            if (!(blockEntity instanceof SolarPanelBlockEntity solarPanelBlockEntity)) {
              SolarGeneration.LOGGER
                  .error("Wrong type of block entity (expected BlockEntitySolarPanel)!");
              return null;
            }
            return new SolarPanelMenu(id, solarPanelBlockEntity, level);
          }, FeatureFlags.DEFAULT_FLAGS)));

      HELMET.put(level, ITEMS.register(level.getSolarHelmetName(),
          () -> new SolarHelmet(level, new Item.Properties().stacksTo(1))));
      CORE.put(level,
          ITEMS.register(level.getSolarCoreName(), () -> new Item(new Item.Properties())));
    }
  }
}