package edivad.solargeneration.network.packet;

import edivad.edivadlib.network.EdivadLibPacket;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record UpdateSolarPanel(
    BlockPos pos, int currentEnergy, int currentProduction) implements EdivadLibPacket {

  public static final ResourceLocation ID = SolarGeneration.rl("update_solar_panel");

  public static UpdateSolarPanel read(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var currentEnergy = buf.readVarInt();
    var currentProduction = buf.readVarInt();
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
    ctx.level().ifPresent(level -> {
      if (level.isLoaded(pos)) {
        if (level.getBlockEntity(pos) instanceof SolarPanelBlockEntity solar) {
          solar.energyClient = currentEnergy;
          solar.energyProductionClient = currentProduction;
        }
      }
    });
  }
}
