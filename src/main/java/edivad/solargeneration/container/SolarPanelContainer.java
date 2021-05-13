package edivad.solargeneration.container;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.IWorldPosCallable;

public class SolarPanelContainer extends Container {

    public final TileEntitySolarPanel tile;
    private final PlayerEntity player;

    public SolarPanelContainer(int windowId, PlayerEntity player, TileEntitySolarPanel tile, SolarPanelLevel level)
    {
        super(Registration.SOLAR_PANEL_CONTAINER.get(level).get(), windowId);
        this.tile = tile;
        this.player = player;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn)
    {
        return stillValid(IWorldPosCallable.create(tile.getLevel(), tile.getBlockPos()), player, tile.getBlockState().getBlock());
    }
}
