package edivad.solargeneration.network;

import java.util.Optional;

import edivad.solargeneration.Main;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
                                new ResourceLocation(Main.MODID, "net"),
                                () -> PROTOCOL_VERSION,
                                PROTOCOL_VERSION::equals,
                                PROTOCOL_VERSION::equals);

    public static void init()
    {
        int id = 0;
        INSTANCE.registerMessage(id++, UpdateSolarPanel.class, UpdateSolarPanel::toBytes, UpdateSolarPanel::new, UpdateSolarPanel::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    };
}
