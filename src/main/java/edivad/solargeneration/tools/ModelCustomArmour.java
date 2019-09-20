package edivad.solargeneration.tools;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;

public class ModelCustomArmour extends BipedModel<LivingEntity> {

	public RendererModel shape15;

	public ModelCustomArmour()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;

		this.shape15 = new RendererModel(this, 82, 0);
		this.shape15.setRotationPoint(-4.0F, -8.0F, -4.0F);
		this.shape15.addBox(-1.0F, -5.0F, -1.0F, 10, 5, 12, 0.0F);

		this.bipedHead.addChild(shape15);
	}
	
	@Override
	public void render(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
	}

	public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
