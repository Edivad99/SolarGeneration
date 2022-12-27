package edivad.solargeneration;

import com.mojang.logging.LogUtils;
import edivad.solargeneration.network.PacketHandler;
import edivad.solargeneration.setup.ClientSetup;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "solargeneration";
    public static final String MODNAME = "Solar Generation";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        Registration.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ClientSetup::init);
        modEventBus.addListener(Main::onCreativeModeTabRegister);
        PacketHandler.init();
    }

    private static void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MODID, "tab"),
                builder -> builder
                        .icon(() -> new ItemStack(Registration.SOLAR_PANEL_ITEM.get(SolarPanelLevel.ADVANCED).get()))
                        .title(Component.literal(MODNAME))
                        .displayItems((features, output, hasPermissions) -> {
                            for (var value : SolarPanelLevel.values()) {
                                output.accept(new ItemStack(Registration.SOLAR_PANEL_ITEM.get(value).get()));
                            }
                            for (var value : SolarPanelLevel.values()) {
                                output.accept(new ItemStack(Registration.HELMET.get(value).get()));
                            }
                            for (var value : SolarPanelLevel.values()) {
                                output.accept(new ItemStack(Registration.CORE.get(value).get()));
                            }
                            output.accept(new ItemStack(Registration.LAPIS_SHARD.get()));
                            output.accept(new ItemStack(Registration.PHOTOVOLTAIC_CELL.get()));
                        }));
    }
}
