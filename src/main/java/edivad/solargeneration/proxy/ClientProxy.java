package edivad.solargeneration.proxy;

import edivad.solargeneration.ModBlocks;
import edivad.solargeneration.gui.SolarPanelGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IProxy {

	@Override
	public void setup(FMLCommonSetupEvent event)
	{
		ScreenManager.registerFactory(ModBlocks.solarPanelAdvancedContainer, SolarPanelGui::new);
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
