package edivad.solargeneration;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.edivadlib.setup.UpdateChecker;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import edivad.solargeneration.client.screen.SolarPanelScreen;
import edivad.solargeneration.datagen.SolarGenerationAdvancementProvider;
import edivad.solargeneration.datagen.SolarGenerationLang;
import edivad.solargeneration.datagen.SolarGenerationLootTableProvider;
import edivad.solargeneration.datagen.SolarGenerationModelProvider;
import edivad.solargeneration.datagen.SolarGenerationRecipes;
import edivad.solargeneration.datagen.SolarPanelBlockTagsProvider;
import edivad.solargeneration.datagen.SolarPanelItemTagsProvider;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.setup.SolarGenerationCreativeModeTabs;
import edivad.solargeneration.tools.SolarGenerationDataComponents;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(SolarGeneration.ID)
public class SolarGeneration {

  public static final String ID = "solargeneration";
  public static final String MODNAME = "SolarGeneration";

  public static final Logger LOGGER = LogUtils.getLogger();

  public SolarGeneration(IEventBus modEventBus) {
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleRegisterMenuScreens);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::registerCapabilities);
    modEventBus.addListener(this::registerPayloads);
    Registration.register(modEventBus);
    SolarGenerationCreativeModeTabs.register(modEventBus);
    SolarGenerationDataComponents.register(modEventBus);
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    NeoForge.EVENT_BUS.register(new UpdateChecker(ID));
  }

  private void handleRegisterMenuScreens(RegisterMenuScreensEvent event) {
    for (var level : SolarPanelLevel.values()) {
      var menu = Registration.SOLAR_PANEL_MENU.get(level).get();
      event.register(menu, SolarPanelScreen::new);
    }
  }

  private void handleGatherData(GatherDataEvent.Client event) {
    event.createProvider(SolarGenerationLootTableProvider::new);
    event.createBlockAndItemTags(SolarPanelBlockTagsProvider::new,
        (packOutput, lookupProvider, __) ->
            new SolarPanelItemTagsProvider(packOutput, lookupProvider));
    event.createProvider(SolarGenerationAdvancementProvider::new);
    event.createProvider(SolarGenerationRecipes.Runner::new);
    event.createProvider(SolarGenerationLang::new);
    event.createProvider(SolarGenerationModelProvider::new);
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    Registration.SOLAR_PANEL_BLOCK_ENTITY.forEach((__, blockEntityType) ->
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK, blockEntityType.get(),
            SolarPanelBlockEntity::getSolarPanelBattery));

    Registration.HELMET.forEach((solarPanelLevel, item) ->
        event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, context) ->
            new ComponentEnergyStorage(stack,
                SolarGenerationDataComponents.ENERGY_COMPONENT.get(),
                solarPanelLevel.getCapacity(), solarPanelLevel.getMaxTransfer()), item.get()));
  }

  private void registerPayloads(RegisterPayloadHandlersEvent event) {
    var registrar = event.registrar(ID).versioned("1");
    registrar.playToClient(UpdateSolarPanel.TYPE, UpdateSolarPanel.STREAM_CODEC, UpdateSolarPanel::handle);
  }

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(ID, path);
  }
}
