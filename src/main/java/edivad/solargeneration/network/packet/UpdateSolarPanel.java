package edivad.solargeneration.network.packet;

import edivad.solargeneration.blockentity.BlockEntitySolarPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateSolarPanel {

    private BlockPos pos;
    private int currentEnergy;
    private int currentProduction;

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
            Level level = Minecraft.getInstance().level;
            if(level.isLoaded(packet.pos)) {
                BlockEntity te = level.getBlockEntity(packet.pos);
                if(te instanceof BlockEntitySolarPanel solar) {
                    solar.energyClient = packet.currentEnergy;
                    solar.energyProductionClient = packet.currentProduction;
                }
            }
        }


    }

}