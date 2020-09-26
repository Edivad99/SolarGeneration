package edivad.solargeneration.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import edivad.solargeneration.Main;
import edivad.solargeneration.container.SolarPanelContainer;
import edivad.solargeneration.tile.TileEntitySolarPanel;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
        this.renderHoveredTooltip(mStack, mouseX, mouseY);
        if(mouseX > guiLeft + 7 && mouseX < guiLeft + 29 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
            this.renderTooltip(mStack, new StringTextComponent("Energy: " + getPercent() + "%"), mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack mStack, int mouseX, int mouseY)
    {
        String energy = new TranslationTextComponent("gui." + Main.MODID + ".stored_energy").appendString(" " + getEnergyFormatted(tile.energyClient)).getString();
        this.font.drawString(mStack, energy, (xSize / 2 - font.getStringWidth(energy) / 2) + 14, 20, 4210752);

        String maxEnergy = new TranslationTextComponent("gui." + Main.MODID + ".max_capacity").appendString(" " + getEnergyFormatted(tile.maxEnergy)).getString();
        this.font.drawString(mStack, maxEnergy, (xSize / 2 - font.getStringWidth(maxEnergy) / 2) + 14, 30, 4210752);

        String generation = new TranslationTextComponent("gui." + Main.MODID + ".generation").appendString(" " + tile.energyProductionClient + " FE/t").getString();
        this.font.drawString(mStack, generation, (xSize / 2 - font.getStringWidth(generation) / 2) + 14, 40, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)
    {
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        this.blit(mStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Energy
        int y = this.getEnergyScaled(60);
        this.blit(mStack, this.guiLeft + 10, this.guiTop + 12 + y, this.xSize, 0, 16, 60 - y);
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
