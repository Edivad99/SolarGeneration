package edivad.solargeneration.setup;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup solarGenerationTab = new ItemGroup(Main.MODID + "_tab") {

        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(Registration.SOLAR_PANEL_BLOCK.get(SolarPanelLevel.ADVANCED).get());
        }
    };
}
