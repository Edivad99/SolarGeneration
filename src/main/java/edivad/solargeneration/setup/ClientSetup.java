package edivad.solargeneration.setup;

import edivad.solargeneration.client.screen.SolarPanelScreen;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event)
    {
        //Version checker
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);

        //GUI
        for(SolarPanelLevel level : SolarPanelLevel.values())
            ScreenManager.register(Registration.SOLAR_PANEL_CONTAINER.get(level).get(), SolarPanelScreen::new);
    }
}