package com.edivad.solargeneration.gui;

import java.util.Collections;

import com.edivad.solargeneration.Main;
import com.edivad.solargeneration.blocks.containers.SolarPanelContainer;
import com.edivad.solargeneration.tile.TileEntitySolarPanel;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class SolarPanelGui extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID + ":textures/gui/solar_panel.png");
	private final TileEntitySolarPanel tileEntitySolarPanel;

	public SolarPanelGui(TileEntitySolarPanel tileEntitySolarPanel, SolarPanelContainer container) {
		super(container);
		this.tileEntitySolarPanel = tileEntitySolarPanel;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		if (mouseX > guiLeft + 7 && mouseX < guiLeft + 29 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
			drawHoveringText(Collections.singletonList("Energy: " + String.valueOf(getPercent()) + " %"), mouseX,
					mouseY, fontRenderer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		String clientEnergy = "Stored enery: " + this.tileEntitySolarPanel.getClientEnergy() + " FE";
		this.fontRenderer.drawString(clientEnergy,
				(this.xSize / 2 - this.fontRenderer.getStringWidth(clientEnergy) / 2) + 14, 20, 4210752);

		String maxEnergy = "Max capacity: " + this.tileEntitySolarPanel.getMaxEnergy() + " FE";
		this.fontRenderer.drawString(maxEnergy, (this.xSize / 2 - this.fontRenderer.getStringWidth(maxEnergy) / 2) + 14,
				30, 4210752);

		String generation = "Generation: " + this.tileEntitySolarPanel.getClientCurrentAmountEnergyProduced() + " FE/t";
		this.fontRenderer.drawString(generation,
				(this.xSize / 2 - this.fontRenderer.getStringWidth(generation) / 2) + 14, 40, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		// Energy
		int y = this.getEnergyScaled(60);
		this.drawTexturedModalRect(this.guiLeft + 10, this.guiTop + 12 + y, 176, 0, 16, 60 - y);

	}

	private int getEnergyScaled(int pixels) {
		double percent = getPercent();
		return pixels - (int) ((int) pixels * percent / 100);
	}

	private double getPercent() {
		int currentEnergy = this.tileEntitySolarPanel.getClientEnergy();
		int maxEnergy = this.tileEntitySolarPanel.getMaxEnergy();

		double result = currentEnergy * 100.0 / maxEnergy;
		return result;
	}

}
