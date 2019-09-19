package edivad.solargeneration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edivad.solargeneration.network.Messages;
import edivad.solargeneration.proxy.ClientProxy;
import edivad.solargeneration.proxy.EventHandler;
import edivad.solargeneration.proxy.GuiHandler;
import edivad.solargeneration.proxy.IProxy;
import edivad.solargeneration.proxy.ServerProxy;
import edivad.solargeneration.tabs.SolarGenerationTab;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

	public static final String MODID = "solargeneration";
	public static final String MODNAME = "Solar Generation";

	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public static final ItemGroup solarGenerationTab = new SolarGenerationTab("solargeneration_tab");

	public static final Logger logger = LogManager.getLogger();

	public Main()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::getClientGuiElement);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		Messages.registerMessages("main_channel");
		MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
		proxy.setup(event);
	}

	@SubscribeEvent
	public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event)
	{
		ModBlocks.registerTiles(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		ModBlocks.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ModItems.register(event.getRegistry());
	}
}
