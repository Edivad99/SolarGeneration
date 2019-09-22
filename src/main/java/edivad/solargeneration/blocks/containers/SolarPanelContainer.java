package edivad.solargeneration.blocks.containers;

import edivad.solargeneration.network.Messages;
import edivad.solargeneration.network.PacketSyncMachineState;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import edivad.solargeneration.tools.inter.ISolarPanelStateContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;

public class SolarPanelContainer extends Container implements ISolarPanelStateContainer {

	private final TileEntitySolarPanel tileEntitySolarPanel;

	public SolarPanelContainer(TileEntitySolarPanel tileEntitySolarPanel)
	{
		this.tileEntitySolarPanel = tileEntitySolarPanel;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileEntitySolarPanel.canInteractWith(playerIn);
	}

	@Override
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

				for (IContainerListener listener : listeners)
				{
					if(listener instanceof EntityPlayerMP)
					{
						EntityPlayerMP player = (EntityPlayerMP) listener;
						Messages.INSTANCE.sendTo(new PacketSyncMachineState(this.tileEntitySolarPanel.getEnergy(), this.tileEntitySolarPanel.currentAmountEnergyProduced()), player);
					}
				}
			}
		}
	}

	@Override
	public void sync(int energy, int energyProducing)
	{
		this.tileEntitySolarPanel.setClientEnergy(energy);
		this.tileEntitySolarPanel.setClientCurrentAmountEnergyProduced(energyProducing);
	}

}
