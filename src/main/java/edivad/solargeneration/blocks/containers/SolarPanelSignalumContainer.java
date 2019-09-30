package edivad.solargeneration.blocks.containers;

import edivad.solargeneration.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SolarPanelSignalumContainer extends SolarPanelContainer {

	public SolarPanelSignalumContainer(int windowId, World world, BlockPos pos, PlayerEntity player)
	{
		super(ModBlocks.solarPanelSignalumContainer, windowId, world, pos, player);
	}
}
