package edivad.solargeneration.items;

import edivad.solargeneration.setup.ModSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class Wrench extends Item {

	public Wrench()
	{
		super(new Properties().group(ModSetup.solarGenerationTab));
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
	{
		return true;
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}
}
