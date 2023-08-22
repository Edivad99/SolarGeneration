package edivad.solargeneration;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.edivadlib.setup.UpdateChecker;
import edivad.solargeneration.client.screen.SolarPanelScreen;
import edivad.solargeneration.datagen.AdvancementProvider;
import edivad.solargeneration.datagen.Lang;
import edivad.solargeneration.datagen.LootTables;
import edivad.solargeneration.datagen.Recipes;
import edivad.solargeneration.datagen.SolarPanelBlockTagsProvider;
import edivad.solargeneration.datagen.SolarPanelItemTagsProvider;
import edivad.solargeneration.network.PacketHandler;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.setup.SolarGenerationCreativeModeTabs;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SolarGeneration.ID)
public class SolarGeneration {

  public static final String ID = "solargeneration";
  public static final String MODNAME = "SolarGeneration";

  public static final Logger LOGGER = LogUtils.getLogger();

  public SolarGeneration() {
    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleGatherData);
    Registration.init(modEventBus);
    SolarGenerationCreativeModeTabs.register(modEventBus);
    PacketHandler.init();
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    MinecraftForge.EVENT_BUS.register(new UpdateChecker(SolarGeneration.ID));

    for (var level : SolarPanelLevel.values()) {
      var menu = Registration.SOLAR_PANEL_CONTAINER.get(level).get();
      MenuScreens.register(menu, SolarPanelScreen::new);
    }
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var fileHelper = event.getExistingFileHelper();

    generator.addProvider(event.includeServer(),
        (DataProvider.Factory<LootTableProvider>) LootTables::create);
    var blockTags = new SolarPanelBlockTagsProvider(packOutput, lookupProvider, fileHelper);
    var blockTagsLookup = blockTags.contentsGetter();
    generator.addProvider(event.includeServer(), blockTags);
    generator.addProvider(event.includeServer(),
        new SolarPanelItemTagsProvider(packOutput, lookupProvider, blockTagsLookup, fileHelper));
    generator.addProvider(event.includeServer(),
        new AdvancementProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(), new Recipes(packOutput));
    generator.addProvider(event.includeClient(), new Lang(packOutput));
  }
}
