package net.lordmrk.dmo.client.render.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.client.render.entity.model.DoggoEntityModel;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DoggoHeldItemFeatureRenderer extends RenderLayer<DoggoEntity, DoggoEntityModel<DoggoEntity>> {

	public DoggoHeldItemFeatureRenderer(RenderLayerParent<DoggoEntity, DoggoEntityModel<DoggoEntity>> featureRendererContext) {
		super(featureRendererContext);
	}

	public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, DoggoEntity doggoEntity, float f, float g, float h, float j, float k, float l) {
		if(doggoEntity.hasStackInMouth()) {
			boolean bl = doggoEntity.isBaby();
			matrixStack.pushPose();
			float n = 0f;
			if(bl) {
				n = 0.75F;
				matrixStack.scale(0.75F, 0.75F, 0.75F);
				matrixStack.translate(0.0D, 0.5D, 0.20937499403953552D);
			}

			matrixStack.translate(this.getParentModel().head.x / 16.0F, this.getParentModel().head.y / 16.0F, this.getParentModel().head.z / 16.0F);
			n = doggoEntity.getBegAnimationProgress(h) + doggoEntity.getShakeAnimationProgress(h, 0.0F);
			matrixStack.mulPose(Axis.ZP.rotation(n));
			matrixStack.mulPose(Axis.YP.rotationDegrees(k));
			matrixStack.mulPose(Axis.XP.rotationDegrees(l));
			if(doggoEntity.isBaby()) {
				matrixStack.translate(0.05999999865889549D, 0.25999999046325684D, -0.5D);
			} else if(doggoEntity.isAction(DoggoAction.NAPPING)) {
				matrixStack.translate(-0.28, 0.155, -0.3);
				matrixStack.mulPose(Axis.YP.rotationDegrees(50.0F));
			} else {
				matrixStack.translate(-n * 0.1, 0.155, -0.38);
			}

			matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			matrixStack.mulPose(Axis.ZP.rotationDegrees(-30.0F));

			ItemStack itemStack = doggoEntity.getStackInMouth();
			Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, i, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider, doggoEntity.level(), doggoEntity.getId());
			matrixStack.popPose();
		}
	}
}
