package edivad.solargeneration.menu;

import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.blockentity.BlockEntitySolarPanel;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class SolarPanelMenu extends AbstractContainerMenu {

    public final BlockEntitySolarPanel tile;
    private final Player player;

    public SolarPanelMenu(int windowId, Player player, BlockEntitySolarPanel tile, SolarPanelLevel level) {
        super(Registration.SOLAR_PANEL_CONTAINER.get(level).get(), windowId);
        this.tile = tile;
        this.player = player;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), player, tile.getBlockState().getBlock());
    }
}
