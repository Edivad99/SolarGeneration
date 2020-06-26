package edivad.solargeneration.gui;

import java.util.Collections;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

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
	
	//render
	@Override
	public void func_230430_a_(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
	{
		this.func_230446_a_(mStack);//this.renderBackground();
		super.func_230430_a_(mStack, mouseX, mouseY, partialTicks);
		this.func_230459_a_(mStack, mouseX, mouseY);//this.renderHoveredToolTip(mouseX, mouseY);
		if(mouseX > guiLeft + 7 && mouseX < guiLeft + 29 && mouseY > guiTop + 10 && mouseY < guiTop + 77)
			this.func_238654_b_(mStack, Collections.singletonList(new StringTextComponent("Energy: " + getPercent() + "%")), mouseX, mouseY, field_230712_o_);
	}
	
	//drawGuiContainerForegroundLayer
	@Override
	protected void func_230451_b_(MatrixStack mStack, int mouseX, int mouseY)
	{
		StringTextComponent energy = new StringTextComponent("Stored energy: " + getEnergyFormatted(tile.energyClient));
		this.field_230712_o_.func_238422_b_(mStack, energy, (xSize / 2 - field_230712_o_.getStringWidth(energy.getString()) / 2) + 14, 20, 4210752);

		StringTextComponent maxEnergy = new StringTextComponent("Max capacity: " + getEnergyFormatted(tile.maxEnergy));
		this.field_230712_o_.func_238422_b_(mStack, maxEnergy, (xSize / 2 - field_230712_o_.getStringWidth(maxEnergy.getString()) / 2) + 14, 30, 4210752);//this.font.drawstring

		StringTextComponent generation = new StringTextComponent("Generation: " + tile.energyProductionClient + " FE/t");
		this.field_230712_o_.func_238422_b_(mStack, generation, (xSize / 2 - field_230712_o_.getStringWidth(generation.getString()) / 2) + 14, 40, 4210752);
	}
	
	
	//drawGuiContainerBackgroundLayer
	@Override
	protected void func_230450_a_(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.field_230706_i_.getTextureManager().bindTexture(TEXTURES);
		this.func_238474_b_(mStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		// Energy
		int y = this.getEnergyScaled(60);
		this.func_238474_b_(mStack, this.guiLeft + 10, this.guiTop + 12 + y, 176, 0, 16, 60 - y);

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
