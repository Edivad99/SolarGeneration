package edivad.solargeneration.network.packet;

import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class UpdateSolarPanel {

    private final BlockPos pos;
    private final int currentEnergy;
    private final int currentProduction;

    public UpdateSolarPanel(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        currentEnergy = buf.readInt();
        currentProduction = buf.readInt();
    }

    public UpdateSolarPanel(BlockPos pos, int currentEnergy, int currentProduction) {
        this.pos = pos;
        this.currentEnergy = currentEnergy;
        this.currentProduction = currentProduction;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(currentEnergy);
        buf.writeInt(currentProduction);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            UpdateSolarPanelClient.handle(this, ctx);
        ctx.get().setPacketHandled(true);
    }

    public static class UpdateSolarPanelClient {
        public static void handle(UpdateSolarPanel packet, Supplier<NetworkEvent.Context> ctx) {
            var level = Minecraft.getInstance().level;
            if(level != null && level.isLoaded(packet.pos)) {
                if(level.getBlockEntity(packet.pos) instanceof SolarPanelBlockEntity solar) {
                    solar.energyClient = packet.currentEnergy;
                    solar.energyProductionClient = packet.currentProduction;
                }
            }
        }
    }
}
