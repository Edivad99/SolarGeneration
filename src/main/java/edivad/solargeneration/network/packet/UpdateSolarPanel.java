package edivad.solargeneration.network.packet;

import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record UpdateSolarPanel(
    BlockPos pos, int currentEnergy, int currentProduction) implements CustomPacketPayload {

  public static final ResourceLocation ID =
      new ResourceLocation(SolarGeneration.ID, "update_solar_panel");

  public static UpdateSolarPanel read(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var currentEnergy = buf.readInt();
    var currentProduction = buf.readInt();
    return new UpdateSolarPanel(pos, currentEnergy, currentProduction);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    buf.writeVarInt(currentEnergy);
    buf.writeVarInt(currentProduction);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext ctx) {
    UpdateSolarPanelClient.handle(this, ctx);
  }

  private static class UpdateSolarPanelClient {

    static void handle(UpdateSolarPanel packet, PlayPayloadContext ctx) {
      ctx.level().ifPresent(level -> {
        if (level.isLoaded(packet.pos)) {
          if (level.getBlockEntity(packet.pos) instanceof SolarPanelBlockEntity solar) {
            solar.energyClient = packet.currentEnergy;
            solar.energyProductionClient = packet.currentProduction;
          }
        }
      });
    }
  }
}
