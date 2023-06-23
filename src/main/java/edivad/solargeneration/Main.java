package edivad.solargeneration;

import com.mojang.logging.LogUtils;
import edivad.solargeneration.network.PacketHandler;
import edivad.solargeneration.setup.ClientSetup;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.setup.SolarGenerationCreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "solargeneration";
    public static final String MODNAME = "Solar Generation";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        Registration.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ClientSetup::init);
        SolarGenerationCreativeModeTabs.register(modEventBus);
        PacketHandler.init();
    }
}
