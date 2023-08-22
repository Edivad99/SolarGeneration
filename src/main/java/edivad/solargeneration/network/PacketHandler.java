package edivad.solargeneration.network;

import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

  private static final String PROTOCOL_VERSION = "1";
  public static SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
      .named(new ResourceLocation(SolarGeneration.ID, "network"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();

  public static void init() {
    INSTANCE
        .messageBuilder(UpdateSolarPanel.class, 0, NetworkDirection.PLAY_TO_CLIENT)
        .encoder(UpdateSolarPanel::encode)
        .decoder(UpdateSolarPanel::decode)
        .consumerMainThread(UpdateSolarPanel::handle)
        .add();
  }
}
