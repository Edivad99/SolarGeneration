package edivad.solargeneration.gui;

import java.util.Collections;

import com.mojang.blaze3d.matrix.MatrixStack;

import edivad.solargeneration.Main;
import edivad.solargeneration.blocks.containers.SolarPanelContainer;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class SolarPanelScreen extends ContainerScreen<SolarPanelContainer> {

    private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID, "textures/gui/solar_panel.png");
    private final TileEntitySolarPanel tile;

    public SolarPanelScreen(SolarPanelContainer container, PlayerInventory inv, ITextComponent name)
    {
        super(container, inv, name);
        this.tile = container.tile;
    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(mStack);
        super.render(mStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(mStack, mouseX, mouseY);//this.renderHoveredToolTip(mouseX, mouseY);
        if(mouseX > guiLeft + 7 && mouseX < guiLeft + 29 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
            this.renderTooltip(mStack, Collections.singletonList(new StringTextComponent("Energy: " + getPercent() + "%")), mouseX, mouseY, font);
    }

    @Override
    protected void func_230451_b_(MatrixStack mStack, int mouseX, int mouseY)//drawGuiContainerForegroundLayer
    {
        String energy = "Stored energy: " + getEnergyFormatted(tile.energyClient);
        this.font.drawString(mStack, energy, (xSize / 2 - font.getStringWidth(energy) / 2) + 14, 20, 4210752);

        String maxEnergy = "Max capacity: " + getEnergyFormatted(tile.maxEnergy);
        this.font.drawString(mStack, maxEnergy, (xSize / 2 - font.getStringWidth(maxEnergy) / 2) + 14, 30, 4210752);

        String generation = "Generation: " + tile.energyProductionClient + " FE/t";
        this.font.drawString(mStack, generation, (xSize / 2 - font.getStringWidth(generation) / 2) + 14, 40, 4210752);
    }

    @Override
    protected void func_230450_a_(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)//drawGuiContainerBackgroundLayer
    {
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        this.blit(mStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Energy
        int y = this.getEnergyScaled(60);
        this.blit(mStack, this.guiLeft + 10, this.guiTop + 12 + y, 176, 0, 16, 60 - y);

    }

    private String getEnergyFormatted(int energy)
    {
        if(energy >= 1000000)
            return (energy / 1000) + " kFE";
        else
            return energy + " FE";
    }

    private int getEnergyScaled(int pixels)
    {
        return pixels - (pixels * getPercent() / 100);
    }

    private int getPercent()
    {
        Long currentEnergy = new Long(tile.energyClient);
        int maxEnergy = tile.maxEnergy;

        long result = currentEnergy * 100 / maxEnergy;

        return (int) result;
    }
}
