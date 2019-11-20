package edivad.solargeneration.blocks.containers;

import edivad.solargeneration.tile.TileEntitySolarPanel;
import edivad.solargeneration.tools.MyEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class SolarPanelContainer extends TrackedContainer {

	private final TileEntitySolarPanel tileEntitySolarPanel;
	private final PlayerEntity player;

	public SolarPanelContainer(ContainerType<?> type, int windowId, World world, BlockPos pos, PlayerEntity player)
	{
		super(type, windowId);
		this.tileEntitySolarPanel = (TileEntitySolarPanel) world.getTileEntity(pos);
		this.player = player;

		addTracker(new IntReferenceHolder() {

			@Override
			public int get()
			{
				return getEnergy();
			}

			@Override
			public void set(int value)
			{
				tileEntitySolarPanel.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((MyEnergyStorage) h).setEnergy(value));
			}
		});
	}

	public int getEnergy()
	{
		return tileEntitySolarPanel.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
	}

	public int getMaxEnergy()
	{
		return tileEntitySolarPanel.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
	}

	public int getCurrentAmountEnergyProduced()
	{
		if(getEnergy() != getMaxEnergy())
			return tileEntitySolarPanel.currentAmountEnergyProduced();
		else
			return 0;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(IWorldPosCallable.of(tileEntitySolarPanel.getWorld(), tileEntitySolarPanel.getPos()), this.player, tileEntitySolarPanel.getBlockState().getBlock());
	}
}
