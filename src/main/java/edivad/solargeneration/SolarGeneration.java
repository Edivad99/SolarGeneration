package edivad.solargeneration;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.edivadlib.setup.UpdateChecker;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import edivad.solargeneration.client.screen.SolarPanelScreen;
import edivad.solargeneration.datagen.Lang;
import edivad.solargeneration.datagen.Recipes;
import edivad.solargeneration.datagen.SolarGenerationAdvancementProvider;
import edivad.solargeneration.datagen.SolarGenerationLootTableProvider;
import edivad.solargeneration.datagen.SolarPanelBlockTagsProvider;
import edivad.solargeneration.datagen.SolarPanelItemTagsProvider;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.setup.SolarGenerationCreativeModeTabs;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

@Mod(SolarGeneration.ID)
public class SolarGeneration {

  public static final String ID = "solargeneration";
  public static final String MODNAME = "SolarGeneration";

  public static final Logger LOGGER = LogUtils.getLogger();

  public SolarGeneration(IEventBus modEventBus) {
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::registerCapabilities);
    modEventBus.addListener(this::registerPacketHandler);
    Registration.register(modEventBus);
    SolarGenerationCreativeModeTabs.register(modEventBus);
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    NeoForge.EVENT_BUS.register(new UpdateChecker(ID));

    for (var level : SolarPanelLevel.values()) {
      var menu = Registration.SOLAR_PANEL_MENU.get(level).get();
      MenuScreens.register(menu, SolarPanelScreen::new);
    }
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var fileHelper = event.getExistingFileHelper();

    generator.addProvider(event.includeServer(), new SolarGenerationLootTableProvider(packOutput));
    var blockTags = new SolarPanelBlockTagsProvider(packOutput, lookupProvider, fileHelper);
    var blockTagsLookup = blockTags.contentsGetter();
    generator.addProvider(event.includeServer(), blockTags);
    generator.addProvider(event.includeServer(),
        new SolarPanelItemTagsProvider(packOutput, lookupProvider, blockTagsLookup, fileHelper));
    generator.addProvider(event.includeServer(),
        new SolarGenerationAdvancementProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(), new Recipes(packOutput, lookupProvider));
    generator.addProvider(event.includeClient(), new Lang(packOutput));
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    Registration.SOLAR_PANEL_BLOCK_ENTITY.forEach((__, blockEntityType) ->
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK, blockEntityType.get(),
            SolarPanelBlockEntity::getSolarPanelBattery));
  }

  private void registerPacketHandler(RegisterPayloadHandlerEvent event) {
    var registrar = event.registrar(ID);
    registrar.play(UpdateSolarPanel.ID, UpdateSolarPanel::read, handler ->
        handler.client(UpdateSolarPanel::handle));
  }
}
