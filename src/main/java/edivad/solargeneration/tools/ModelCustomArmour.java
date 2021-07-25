//package edivad.solargeneration.tools;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//
//import net.minecraft.client.model.HumanoidModel;
//import net.minecraft.client.model.geom.ModelPart;
//import net.minecraft.world.entity.player.Player;
//
//public class ModelCustomArmour extends HumanoidModel<Player> {
//
//    public ModelPart shape15;
//
//    public ModelCustomArmour()
//    {
//        super(0.0F, 0.0F, 64, 32);
//        this.texWidth = 128;
//        this.texHeight = 128;
//
//        this.shape15 = new ModelPart(this, 82, 0);
//        this.shape15.setPos(-4.0F, -8.0F, -4.0F);
//        this.shape15.addBox(-1.0F, -5.0F, -1.0F, 10, 5, 12, 0.0F);
//
//        this.head.addChild(shape15);
//    }
//
//    @Override
//    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
//    {
//        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//    }
//}
