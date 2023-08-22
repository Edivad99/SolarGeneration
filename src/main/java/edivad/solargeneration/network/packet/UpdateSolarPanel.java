package edivad.solargeneration.network.packet;

import java.util.function.Supplier;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    UpdateSolarPanelClient.handle(this, ctx);
    ctx.get().setPacketHandled(true);
  }

  public static class UpdateSolarPanelClient {

    public static void handle(UpdateSolarPanel packet, Supplier<NetworkEvent.Context> ctx) {
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
