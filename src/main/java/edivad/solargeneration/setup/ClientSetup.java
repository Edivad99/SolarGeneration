package edivad.solargeneration.setup;

import edivad.solargeneration.gui.SolarPanelScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event)
    {
        //Version checker
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);

        //GUI
        ScreenManager.registerFactory(Registration.LEADSTONE_CONTAINER.get(), SolarPanelScreen::new);
        ScreenManager.registerFactory(Registration.HARDENED_CONTAINER.get(), SolarPanelScreen::new);
        ScreenManager.registerFactory(Registration.REDSTONE_CONTAINER.get(), SolarPanelScreen::new);
        ScreenManager.registerFactory(Registration.SIGNALUM_CONTAINER.get(), SolarPanelScreen::new);
        ScreenManager.registerFactory(Registration.RESONANT_CONTAINER.get(), SolarPanelScreen::new);
        ScreenManager.registerFactory(Registration.ADVANCED_CONTAINER.get(), SolarPanelScreen::new);
        ScreenManager.registerFactory(Registration.ULTIMATE_CONTAINER.get(), SolarPanelScreen::new);
    }
}