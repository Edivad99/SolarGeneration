package edivad.solargeneration.menu;

import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import edivad.solargeneration.setup.Registration;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class SolarPanelMenu extends AbstractContainerMenu {

  public final SolarPanelBlockEntity solarPanelBlockEntity;

  public SolarPanelMenu(int containerId, SolarPanelBlockEntity solarPanelBlockEntity,
      SolarPanelLevel level) {
    super(Registration.SOLAR_PANEL_CONTAINER.get(level).get(), containerId);
    this.solarPanelBlockEntity = solarPanelBlockEntity;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotId) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(ContainerLevelAccess.create(solarPanelBlockEntity.getLevel(),
            solarPanelBlockEntity.getBlockPos()), player,
        solarPanelBlockEntity.getBlockState().getBlock());
  }
}
