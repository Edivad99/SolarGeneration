package edivad.solargeneration.setup;

import edivad.solargeneration.Main;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

	public static final ItemGroup solarGenerationTab = new ItemGroup(Main.MODID + "_tab") {

		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(Registration.ADVANCED.get());
		}
	};
}
