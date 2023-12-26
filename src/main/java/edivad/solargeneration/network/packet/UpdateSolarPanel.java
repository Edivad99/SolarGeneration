package edivad.solargeneration.network.packet;

import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record UpdateSolarPanel(BlockPos pos, int currentEnergy, int currentProduction) {

  public static UpdateSolarPanel decode(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var currentEnergy = buf.readInt();
    var currentProduction = buf.readInt();
    return new UpdateSolarPanel(pos, currentEnergy, currentProduction);
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    buf.writeInt(currentEnergy);
    buf.writeInt(currentProduction);
  }

  public boolean handle(NetworkEvent.Context ctx) {
    UpdateSolarPanelClient.handle(this, ctx);
    return true;
  }

  public static class UpdateSolarPanelClient {

    public static void handle(UpdateSolarPanel packet, NetworkEvent.Context ctx) {
      var level = Minecraft.getInstance().level;
      if (level != null && level.isLoaded(packet.pos)) {
        if (level.getBlockEntity(packet.pos) instanceof SolarPanelBlockEntity solar) {
          solar.energyClient = packet.currentEnergy;
          solar.energyProductionClient = packet.currentProduction;
        }
      }
    }
  }
}
