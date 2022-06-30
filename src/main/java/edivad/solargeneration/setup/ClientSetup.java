package edivad.solargeneration.setup;

import edivad.edivadlib.setup.UpdateChecker;
import edivad.solargeneration.Main;
import edivad.solargeneration.client.screen.SolarPanelScreen;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
        //Version checker
        MinecraftForge.EVENT_BUS.register(new UpdateChecker(Main.MODID));

        //GUI
        for(SolarPanelLevel level : SolarPanelLevel.values())
            MenuScreens.register(Registration.SOLAR_PANEL_CONTAINER.get(level).get(), SolarPanelScreen::new);
    }
}