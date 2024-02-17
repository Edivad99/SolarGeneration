package edivad.solargeneration.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import edivad.solargeneration.SolarGeneration;
import edivad.solargeneration.blockentity.SolarPanelBlockEntity;
import edivad.solargeneration.menu.SolarPanelMenu;
import edivad.solargeneration.tools.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu> {

  private static final ResourceLocation TEXTURE =
      SolarGeneration.rl("textures/gui/solar_panel.png");

  private final SolarPanelBlockEntity solarPanelBlockEntity;

  public SolarPanelScreen(SolarPanelMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.solarPanelBlockEntity = menu.solarPanelBlockEntity;
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
    if (mouseX > leftPos + 7 && mouseX < leftPos + 29 && mouseY > topPos + 10
        && mouseY < topPos + 77) {
      guiGraphics.renderTooltip(this.font,
          Component.translatable(Translations.ENERGY, getPercent()), mouseX, mouseY);
    }
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    var energy = Component.translatable(Translations.STORED_ENERGY,
        getEnergyFormatted(solarPanelBlockEntity.energyClient));
    guiGraphics.drawString(font, energy, (imageWidth / 2 - font.width(energy) / 2) + 14, 20,
        4210752, false);
    var maxEnergy = Component.translatable(Translations.CAPACITY,
        getEnergyFormatted(solarPanelBlockEntity.getLevelSolarPanel().getCapacity()));
    guiGraphics.drawString(font, maxEnergy, (imageWidth / 2 - font.width(maxEnergy) / 2) + 14, 30,
        4210752, false);
    var generation = Component.translatable(Translations.GENERATION,
        solarPanelBlockEntity.energyProductionClient);
    guiGraphics.drawString(font, generation, (imageWidth / 2 - font.width(generation) / 2) + 14, 40,
        4210752, false);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);
    guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    // Energy
    int y = this.getEnergyScaled(60);
    guiGraphics
        .blit(TEXTURE, this.leftPos + 10, this.topPos + 12 + y, this.imageWidth, 0, 16, 60 - y);
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
