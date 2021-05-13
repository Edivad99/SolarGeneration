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
        this.texWidth = 128;
        this.texHeight = 128;

        this.shape15 = new ModelRenderer(this, 82, 0);
        this.shape15.setPos(-4.0F, -8.0F, -4.0F);
        this.shape15.addBox(-1.0F, -5.0F, -1.0F, 10, 5, 12, 0.0F);

        this.head.addChild(shape15);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
