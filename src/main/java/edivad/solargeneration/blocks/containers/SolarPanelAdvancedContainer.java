package edivad.solargeneration.blocks.containers;

import edivad.solargeneration.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SolarPanelAdvancedContainer extends SolarPanelContainer {

	public SolarPanelAdvancedContainer(int windowId, World world, BlockPos pos, PlayerEntity player)
	{
		super(Registration.ADVANCED_CONTAINER.get(), windowId, world, pos, player);
	}
}
