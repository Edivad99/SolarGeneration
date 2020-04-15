package edivad.solargeneration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edivad.solargeneration.message.IntTrackerContainerMessage;
import edivad.solargeneration.setup.ClientSetup;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.setup.proxy.IProxy;
import edivad.solargeneration.setup.proxy.Proxy;
import edivad.solargeneration.setup.proxy.ProxyClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Main.MODID)
public class Main {

	public static final String MODID = "solargeneration";
	public static final String MODNAME = "Solar Generation";
	public static final String PROTOCOL = "1";

	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ProxyClient(), () -> () -> new Proxy());

	public static final Logger logger = LogManager.getLogger();

	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "network"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

	public Main()
	{
		Registration.init();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
		{
			FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		});
		NETWORK.registerMessage(0, IntTrackerContainerMessage.class, IntTrackerContainerMessage::encode, IntTrackerContainerMessage::decode, IntTrackerContainerMessage.Handler::handle);
	}
}
