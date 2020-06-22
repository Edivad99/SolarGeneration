package edivad.solargeneration.blocks.containers;

import edivad.solargeneration.tile.TileEntitySolarPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SolarPanelContainer extends Container {

	public final TileEntitySolarPanel tile;
	private final PlayerEntity player;

	public SolarPanelContainer(ContainerType<?> type, int windowId, World world, BlockPos pos, PlayerEntity player)
	{
		super(type, windowId);
		this.tile = (TileEntitySolarPanel) world.getTileEntity(pos);
		this.player = player;
	}
	

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), this.player, tile.getBlockState().getBlock());
	}
}
