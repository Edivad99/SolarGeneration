package edivad.solargeneration.tools;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class ModelCustomArmour extends BipedModel<LivingEntity> {

	public ModelRenderer shape15;

	public ModelCustomArmour()
	{
		super(0.0F, 0.0F, 64, 32);
		this.textureWidth = 128;
		this.textureHeight = 128;

		this.shape15 = new ModelRenderer(this, 82, 0);
		this.shape15.setRotationPoint(-4.0F, -8.0F, -4.0F);
		//this.shape15.addBox(-1.0F, -5.0F, -1.0F, 10, 5, 12, 0.0F);
		this.shape15.func_228301_a_(-1.0F, -5.0F, -1.0F, 10, 5, 12, 0.0F);

		this.bipedHead.addChild(shape15);
	}
	
	//	public void render(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5)
	@Override
	public void func_225597_a_(LivingEntity entity, float f, float f1, float f2, float f3, float f4) {
		super.func_225597_a_(entity, f, f1, f2, f3, f4);
	}


	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
