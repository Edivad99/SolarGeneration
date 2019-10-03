package edivad.solargeneration.gui;

import java.util.Collections;

import com.mojang.blaze3d.platform.GlStateManager;

import edivad.solargeneration.Main;
import edivad.solargeneration.blocks.containers.SolarPanelContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SolarPanelScreen extends ContainerScreen<SolarPanelContainer> {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID, "textures/gui/solar_panel.png");
	private final SolarPanelContainer container;

	public SolarPanelScreen(SolarPanelContainer container, PlayerInventory inv, ITextComponent name)
	{
		super(container, inv, name);
		this.container = container;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		if(mouseX > guiLeft + 7 && mouseX < guiLeft + 29 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
			this.renderTooltip(Collections.singletonList("Energy: " + String.valueOf(getPercent()) + " %"), mouseX, mouseY, font);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String clientEnergy = "Stored enery: " + this.container.getEnergy() + " FE";
		this.font.drawString(clientEnergy, (this.xSize / 2 - this.font.getStringWidth(clientEnergy) / 2) + 14, 20, 4210752);

		String maxEnergy = "Max capacity: " + this.container.getMaxEnergy() + " FE";
		this.font.drawString(maxEnergy, (this.xSize / 2 - this.font.getStringWidth(maxEnergy) / 2) + 14, 30, 4210752);

		String generation = "Generation: " + this.container.getCurrentAmountEnergyProduced() + " FE/t";
		this.font.drawString(generation, (this.xSize / 2 - this.font.getStringWidth(generation) / 2) + 14, 40, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager().bindTexture(TEXTURES);
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		// Energy
		int y = this.getEnergyScaled(60);
		this.blit(this.guiLeft + 10, this.guiTop + 12 + y, 176, 0, 16, 60 - y);

	}

	private int getEnergyScaled(int pixels)
	{
		double percent = getPercent();
		return pixels - (int) ((int) pixels * percent / 100);
	}

	private double getPercent()
	{
		int currentEnergy = this.container.getEnergy();
		int maxEnergy = this.container.getMaxEnergy();

		double result = currentEnergy * 100.0 / maxEnergy;
		return result;
	}

}
