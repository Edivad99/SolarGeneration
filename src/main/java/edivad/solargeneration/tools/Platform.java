package edivad.solargeneration.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class Platform {

	public static boolean isWrench(final EntityPlayer player, final ItemStack eq, final BlockPos pos)
	{
		if(!eq.isEmpty())
		{
			try
			{
				if(eq.getItem() instanceof cofh.api.item.IToolHammer)
				{
					return ((cofh.api.item.IToolHammer) eq.getItem()).isUsable(eq, player, pos);
				}
			}
			catch(Exception ex)
			{

			}
		}
		return false;
	}
}
