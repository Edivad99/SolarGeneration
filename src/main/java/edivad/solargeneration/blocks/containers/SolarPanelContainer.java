package edivad.solargeneration.blocks.containers;

import edivad.solargeneration.tile.TileEntitySolarPanel;
import edivad.solargeneration.tools.inter.ISolarPanelStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SolarPanelContainer extends Container implements ISolarPanelStateContainer {

	private final TileEntitySolarPanel tileEntitySolarPanel;
	
	public SolarPanelContainer(ContainerType<?> type, int windowId, World world, BlockPos pos)
	{
		super(type, windowId);
		tileEntitySolarPanel = (TileEntitySolarPanel) world.getTileEntity(pos);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(IWorldPosCallable.of(tileEntitySolarPanel.getWorld(), tileEntitySolarPanel.getPos()), playerIn, tileEntitySolarPanel.getBlockState().getBlock());
	}

	/*@Override
	public void detectAndSendChanges()
	{
		// For sync variable from client and server
		super.detectAndSendChanges();
		if(!this.tileEntitySolarPanel.getWorld().isRemote)
		{
			if(tileEntitySolarPanel.getEnergy() != tileEntitySolarPanel.getClientEnergy() || tileEntitySolarPanel.currentAmountEnergyProduced() != tileEntitySolarPanel.getClientCurrentAmountEnergyProduced())
			{
				tileEntitySolarPanel.setClientEnergy(tileEntitySolarPanel.getEnergy());
				tileEntitySolarPanel.setClientCurrentAmountEnergyProduced(tileEntitySolarPanel.currentAmountEnergyProduced());

				for(IContainerListener listener : listeners)
				{
					if(listener instanceof PlayerEntity)
					{
						PlayerEntity player = (PlayerEntity) listener;
						Messages.INSTANCE.sendTo(new PacketSyncMachineState(this.tileEntitySolarPanel.getEnergy(), this.tileEntitySolarPanel.currentAmountEnergyProduced()), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
					}
				}
			}
		}
	}*/

	@Override
	public void sync(int energy, int energyProducing)
	{
		this.tileEntitySolarPanel.setClientEnergy(energy);
		this.tileEntitySolarPanel.setClientCurrentAmountEnergyProduced(energyProducing);
	}
}
