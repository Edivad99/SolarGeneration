package edivad.solargeneration.network.packet;

import edivad.solargeneration.Main;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
        ctx.get().enqueueWork(() -> {
            Level world = Main.proxy.getClientWorld();
            if(world.isLoaded(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if(te instanceof TileEntitySolarPanel solar) {
                    solar.energyClient = currentEnergy;
                    solar.energyProductionClient = currentProduction;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
