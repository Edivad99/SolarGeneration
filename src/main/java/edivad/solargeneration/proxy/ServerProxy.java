package edivad.solargeneration.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {

	@Override
	public void init()
	{

	}

	@Override
	public PlayerEntity getClientPlayer()
	{
		throw new IllegalStateException("This should only be called from client side");
	}

	@Override
	public World getClientWorld()
	{
		throw new IllegalStateException("This should only be called from client side");
	}

}
