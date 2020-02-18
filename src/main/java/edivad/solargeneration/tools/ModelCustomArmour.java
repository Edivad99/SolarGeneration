package edivad.solargeneration.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;

public class ModelCustomArmour extends BipedModel<PlayerEntity> {

	public ModelRenderer shape15;

	public ModelCustomArmour()
	{
		super(0.0F, 0.0F, 64, 32);
		this.textureWidth = 128;
		this.textureHeight = 128;

		this.shape15 = new ModelRenderer(this, 82, 0);
		this.shape15.setRotationPoint(-4.0F, -8.0F, -4.0F);
		this.shape15.addBox(-1.0F, -5.0F, -1.0F, 10, 5, 12, 0.0F);

		this.bipedHead.addChild(shape15);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
