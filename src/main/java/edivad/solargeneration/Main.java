package edivad.solargeneration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edivad.solargeneration.network.PacketHandler;
import edivad.solargeneration.setup.ClientSetup;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.setup.proxy.IProxy;
import edivad.solargeneration.setup.proxy.Proxy;
import edivad.solargeneration.setup.proxy.ProxyClient;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {

	public static final String MODID = "solargeneration";
	public static final String MODNAME = "Solar Generation";

	public static IProxy proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> Proxy::new);

	public static final Logger logger = LogManager.getLogger();

	public Main()
	{
		Registration.init();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		PacketHandler.init();
	}
}
