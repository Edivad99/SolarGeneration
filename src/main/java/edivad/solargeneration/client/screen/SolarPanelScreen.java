package edivad.solargeneration.client.screen;

import java.util.List;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import edivad.solargeneration.menu.SolarPanelMenu;
import edivad.solargeneration.tools.Translations;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu> {

  private static final Identifier TEXTURE =
      SolarGeneration.id("textures/gui/solar_panel.png");

  private final SolarPanelBlockEntity solarPanelBlockEntity;

  public SolarPanelScreen(SolarPanelMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.solarPanelBlockEntity = menu.solarPanelBlockEntity;
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    super.extractRenderState(graphics, mouseX, mouseY, a);
    if (mouseX > leftPos + 7 && mouseX < leftPos + 29 && mouseY > topPos + 10
        && mouseY < topPos + 77) {
      var component = Component.translatable(Translations.ENERGY, getPercent());
      var clienttooltipcomponent = ClientTooltipComponent.create(component.getVisualOrderText());
      graphics.tooltip(
          font,
          List.of(clienttooltipcomponent),
          mouseX, mouseY,
          DefaultTooltipPositioner.INSTANCE,
          null
      );
    }
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    var energy = Component.translatable(Translations.STORED_ENERGY,
        getEnergyFormatted(solarPanelBlockEntity.energyClient));
    graphics.text(font, energy, (imageWidth / 2 - font.width(energy) / 2) + 14, 20,
        0xFF333333, false);
    var maxEnergy = Component.translatable(Translations.CAPACITY,
        getEnergyFormatted(solarPanelBlockEntity.getLevelSolarPanel().getCapacity()));
    graphics.text(font, maxEnergy, (imageWidth / 2 - font.width(maxEnergy) / 2) + 14, 30,
        0xFF333333, false);
    var generation = Component.translatable(Translations.GENERATION,
        solarPanelBlockEntity.energyProductionClient);
    graphics.text(font, generation, (imageWidth / 2 - font.width(generation) / 2) + 14, 40,
        0xFF333333, false);
  }


  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    super.extractBackground(graphics, mouseX, mouseY, a);
    graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0,
        this.imageWidth, this.imageHeight, 256, 256);

    // Energy
    int y = this.getEnergyScaled(60);
    graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + 10, this.topPos + 12 + y,
        this.imageWidth, 0, 16, 60 - y, 256, 256);
  }

  private String getEnergyFormatted(int energy) {
    if (energy >= 1000000) {
      return (energy / 1000) + " kFE";
    } else {
      return energy + " FE";
    }
  }

  private int getEnergyScaled(int pixels) {
    return pixels - (pixels * getPercent() / 100);
  }

  private int getPercent() {
    long currentEnergy = solarPanelBlockEntity.energyClient;
    int maxEnergy = solarPanelBlockEntity.getLevelSolarPanel().getCapacity();

    long result = currentEnergy * 100 / maxEnergy;

    return (int) result;
  }
}
