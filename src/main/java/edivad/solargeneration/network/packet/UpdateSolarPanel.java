package edivad.solargeneration.network.packet;

import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateSolarPanel(
    BlockPos pos, int currentEnergy, int currentProduction) implements CustomPacketPayload {

  public static final Type<UpdateSolarPanel> TYPE =
      new Type<>(SolarGeneration.rl("update_solar_panel"));

  public static final StreamCodec<FriendlyByteBuf, UpdateSolarPanel> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, UpdateSolarPanel::pos,
          ByteBufCodecs.VAR_INT, UpdateSolarPanel::currentEnergy,
          ByteBufCodecs.VAR_INT, UpdateSolarPanel::currentProduction,
          UpdateSolarPanel::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(UpdateSolarPanel message, IPayloadContext ctx) {
    var level = ctx.player().level();
    if (level.isLoaded(message.pos)) {
      if (level.getBlockEntity(message.pos) instanceof SolarPanelBlockEntity solar) {
        solar.energyClient = message.currentEnergy;
        solar.energyProductionClient = message.currentProduction;
      }
    }
  }
}
