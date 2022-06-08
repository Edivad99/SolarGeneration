package edivad.solargeneration.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import edivad.solargeneration.Main;
import edivad.solargeneration.blockentity.BlockEntitySolarPanel;
import edivad.solargeneration.menu.SolarPanelMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu> {

    private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID, "textures/gui/solar_panel.png");
    private final BlockEntitySolarPanel tile;

    public SolarPanelScreen(SolarPanelMenu container, Inventory inv, Component name) {
        super(container, inv, name);
        this.tile = container.tile;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        if(mouseX > leftPos + 7 && mouseX < leftPos + 29 && mouseY > topPos + 10 && mouseY < topPos + 77)
            this.renderTooltip(poseStack, Component.literal("Energy: " + getPercent() + "%"), mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        Component energy = Component.translatable("gui." + Main.MODID + ".stored_energy").append(" " + getEnergyFormatted(tile.energyClient));
        this.font.draw(poseStack, energy, (imageWidth / 2 - font.width(energy) / 2) + 14, 20, 4210752);

        Component maxEnergy = Component.translatable("gui." + Main.MODID + ".max_capacity").append(" " + getEnergyFormatted(tile.getLevelSolarPanel().getCapacity()));
        this.font.draw(poseStack, maxEnergy, (imageWidth / 2 - font.width(maxEnergy) / 2) + 14, 30, 4210752);

        Component generation = Component.translatable("gui." + Main.MODID + ".generation").append(" " + tile.energyProductionClient + " FE/t");
        this.font.draw(poseStack, generation, (imageWidth / 2 - font.width(generation) / 2) + 14, 40, 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURES);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Energy
        int y = this.getEnergyScaled(60);
        this.blit(poseStack, this.leftPos + 10, this.topPos + 12 + y, this.imageWidth, 0, 16, 60 - y);
    }

    private String getEnergyFormatted(int energy) {
        if(energy >= 1000000)
            return (energy / 1000) + " kFE";
        else
            return energy + " FE";
    }

    private int getEnergyScaled(int pixels) {
        return pixels - (pixels * getPercent() / 100);
    }

    private int getPercent() {
        long currentEnergy = tile.energyClient;
        int maxEnergy = tile.getLevelSolarPanel().getCapacity();

        long result = currentEnergy * 100 / maxEnergy;

        return (int) result;
    }
}
