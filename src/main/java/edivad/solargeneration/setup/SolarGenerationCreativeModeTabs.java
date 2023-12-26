package edivad.solargeneration.setup;

import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SolarGenerationCreativeModeTabs {

  private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SolarGeneration.ID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SOLAR_GENERATION_TAB =
      CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
          .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
          .title(Component.literal(SolarGeneration.MODNAME))
          .icon(() ->
              new ItemStack(Registration.SOLAR_PANEL_ITEM.get(SolarPanelLevel.ADVANCED).get()))
          .displayItems((parameters, output) -> {
            for (var value : SolarPanelLevel.values()) {
              output.accept(new ItemStack(Registration.SOLAR_PANEL_ITEM.get(value).get()));
            }
            for (var value : SolarPanelLevel.values()) {
              output.accept(new ItemStack(Registration.HELMET.get(value).get()));
            }
            for (var value : SolarPanelLevel.values()) {
              output.accept(new ItemStack(Registration.CORE.get(value).get()));
            }
            output.accept(new ItemStack(Registration.LAPIS_SHARD.get()));
            output.accept(new ItemStack(Registration.PHOTOVOLTAIC_CELL.get()));
          }).build());

  public static void register(IEventBus modEventBus) {
    CREATIVE_MODE_TABS.register(modEventBus);
  }
}
