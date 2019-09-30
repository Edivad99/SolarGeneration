package edivad.solargeneration.blocks.containers;

import edivad.solargeneration.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SolarPanelUltimateContainer extends SolarPanelContainer {

	public SolarPanelUltimateContainer(int windowId, World world, BlockPos pos, PlayerEntity player)
	{
		super(ModBlocks.solarPanelUltimateContainer, windowId, world, pos, player);
	}
}
