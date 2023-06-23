package edivad.solargeneration.setup;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SolarGenerationCreativeModeTabs {

  private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);

  public static final RegistryObject<CreativeModeTab> SOLAR_GENERATION_TAB =
      CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
          .title(Component.literal(Main.MODNAME))
          .icon(() -> new ItemStack(Registration.SOLAR_PANEL_ITEM.get(SolarPanelLevel.ADVANCED).get()))
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
