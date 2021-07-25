package edivad.solargeneration.setup;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final CreativeModeTab solarGenerationTab = new CreativeModeTab(Main.MODID + "_tab") {

        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(Registration.SOLAR_PANEL_BLOCK.get(SolarPanelLevel.ADVANCED).get());
        }
    };
}
