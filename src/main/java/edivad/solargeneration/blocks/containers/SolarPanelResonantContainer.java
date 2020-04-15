package edivad.solargeneration.blocks.containers;

import edivad.solargeneration.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SolarPanelResonantContainer extends SolarPanelContainer {

	public SolarPanelResonantContainer(int windowId, World world, BlockPos pos, PlayerEntity player)
	{
		super(Registration.RESONANT_CONTAINER.get(), windowId, world, pos, player);
	}
}
