package edivad.solargeneration.menu;

import edivad.solargeneration.blockentity.BlockEntitySolarPanel;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class SolarPanelMenu extends AbstractContainerMenu {

    public final BlockEntitySolarPanel tile;

    public SolarPanelMenu(int windowId, BlockEntitySolarPanel tile, SolarPanelLevel level) {
        super(Registration.SOLAR_PANEL_CONTAINER.get(level).get(), windowId);
        this.tile = tile;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), player, tile.getBlockState().getBlock());
    }
}
