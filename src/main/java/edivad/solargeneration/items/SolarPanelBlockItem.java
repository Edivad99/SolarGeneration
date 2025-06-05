package edivad.solargeneration.items;

import java.util.function.Consumer;
import edivad.solargeneration.blocks.SolarPanelBlock;
import edivad.solargeneration.tools.SolarGenerationDataComponents;
import edivad.solargeneration.tools.SolarPanelLevel;
import edivad.solargeneration.tools.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class SolarPanelBlockItem extends BlockItem {

  private final SolarPanelLevel level;

  public SolarPanelBlockItem(SolarPanelBlock block, SolarPanelLevel level, Properties properties) {
    super(block, properties);
    this.level = level;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    int energy = stack.getOrDefault(SolarGenerationDataComponents.ENERGY_COMPONENT.get(), 0);
    if (energy > 0) {
      tooltipAdder.accept(Tooltip.showInfoCtrl(energy));
    }
    Tooltip.showInfoShift(this.level, tooltipAdder);
  }
}
