package edivad.solargeneration.network;

import edivad.solargeneration.Main;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MODID, "net"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(id++, UpdateSolarPanel.class, UpdateSolarPanel::toBytes, UpdateSolarPanel::new, UpdateSolarPanel::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
