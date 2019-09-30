package edivad.solargeneration.items;

import edivad.solargeneration.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class Wrench extends Item {

	public Wrench()
	{
		super(new Properties().group(Main.solarGenerationTab).maxStackSize(1));
		setRegistryName(new ResourceLocation(Main.MODID, "wrench"));
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
	{
		return true;
	}
}
