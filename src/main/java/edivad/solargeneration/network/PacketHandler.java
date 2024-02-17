package edivad.solargeneration.network;

import edivad.edivadlib.network.BasePacketHandler;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.network.packet.UpdateSolarPanel;
import net.neoforged.bus.api.IEventBus;

public class PacketHandler extends BasePacketHandler {

  public PacketHandler(IEventBus modEventBus) {
    super(modEventBus, SolarGeneration.ID, "1");
  }

  @Override
  protected void registerServerToClient(PacketRegistrar packetRegistrar) {
    packetRegistrar.play(UpdateSolarPanel.ID, UpdateSolarPanel::read);
  }
}
