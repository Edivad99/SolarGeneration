package edivad.solargeneration.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerProxy implements IProxy {

	@Override
	public void setup(FMLCommonSetupEvent event)
	{

	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		throw new IllegalStateException("This should only be called from client side");
	}

	@Override
	public World getClientWorld()
	{
		throw new IllegalStateException("This should only be called from client side");
	}

}
