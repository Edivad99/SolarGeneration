package edivad.solargeneration.proxy;

import edivad.solargeneration.ModBlocks;
import edivad.solargeneration.gui.SolarPanelScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IProxy {

	@Override
	public void init()
	{
		ScreenManager.registerFactory(ModBlocks.solarPanelAdvancedContainer, SolarPanelScreen::new);
		ScreenManager.registerFactory(ModBlocks.solarPanelHardenedContainer, SolarPanelScreen::new);
		ScreenManager.registerFactory(ModBlocks.solarPanelLeadstoneContainer, SolarPanelScreen::new);
		ScreenManager.registerFactory(ModBlocks.solarPanelRedstoneContainer, SolarPanelScreen::new);
		ScreenManager.registerFactory(ModBlocks.solarPanelResonantContainer, SolarPanelScreen::new);
		ScreenManager.registerFactory(ModBlocks.solarPanelSignalumContainer, SolarPanelScreen::new);
		ScreenManager.registerFactory(ModBlocks.solarPanelUltimateContainer, SolarPanelScreen::new);

		MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
	}

	@Override
	public PlayerEntity getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}

	@Override
	public World getClientWorld()
	{
		return Minecraft.getInstance().world;
	}
}
