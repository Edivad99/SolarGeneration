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
        this.renderTooltip(mStack, mouseX, mouseY);
        if(mouseX > leftPos + 7 && mouseX < leftPos + 29 && mouseY > topPos + 10 && mouseY < topPos + 77)
            this.renderTooltip(mStack, new StringTextComponent("Energy: " + getPercent() + "%"), mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack mStack, int mouseX, int mouseY)
    {
        String energy = new TranslationTextComponent("gui." + Main.MODID + ".stored_energy").append(" " + getEnergyFormatted(tile.energyClient)).getString();
        this.font.draw(mStack, energy, (imageWidth / 2 - font.width(energy) / 2) + 14, 20, 4210752);

        String maxEnergy = new TranslationTextComponent("gui." + Main.MODID + ".max_capacity").append(" " + getEnergyFormatted(tile.maxEnergy)).getString();
        this.font.draw(mStack, maxEnergy, (imageWidth / 2 - font.width(maxEnergy) / 2) + 14, 30, 4210752);

        String generation = new TranslationTextComponent("gui." + Main.MODID + ".generation").append(" " + tile.energyProductionClient + " FE/t").getString();
        this.font.draw(mStack, generation, (imageWidth / 2 - font.width(generation) / 2) + 14, 40, 4210752);
    }

    @Override
    protected void renderBg(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)
    {
        this.minecraft.getTextureManager().bind(TEXTURES);
        this.blit(mStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Energy
        int y = this.getEnergyScaled(60);
        this.blit(mStack, this.leftPos + 10, this.topPos + 12 + y, this.imageWidth, 0, 16, 60 - y);
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
