package net.lordmrk.dmo.client.render.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.client.render.entity.DoggoRenderState;
import net.lordmrk.dmo.client.render.entity.model.DoggoEntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class DoggoHeldItemFeatureRenderer extends RenderLayer<DoggoRenderState, DoggoEntityModel> {

	public DoggoHeldItemFeatureRenderer(RenderLayerParent<DoggoRenderState, DoggoEntityModel> featureRendererContext) {
		super(featureRendererContext);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, DoggoRenderState state, float yRot, float xRot) {
		ItemStackRenderState item = state.heldItem;

		if(!item.isEmpty()) {
			boolean bl = state.isBaby;
			poseStack.pushPose();

			if(bl) {
				poseStack.scale(0.75F, 0.75F, 0.75F);
				poseStack.translate(0.0D, 0.5D, 0.20937499403953552D);
			}

			poseStack.translate(this.getParentModel().head.x / 16.0F, this.getParentModel().head.y / 16.0F, this.getParentModel().head.z / 16.0F);
			float n = state.getBegAnimationProgress() + state.getShakeAnimationProgress(0.0F);
			poseStack.rotate(Axis.ZP, n);
			poseStack.rotateDegrees(Axis.YP, yRot);
			poseStack.rotateDegrees(Axis.XP, xRot);

			if(state.isBaby) {
				poseStack.translate(0.05999999865889549D, 0.25999999046325684D, -0.5D);
			} else if(state.action == DoggoAction.NAPPING) {
				poseStack.translate(-0.28, 0.155, -0.3);
				poseStack.rotateDegrees(Axis.YP, 50.0F);
			} else {
				poseStack.translate(-n * 0.1, 0.155, -0.38);
			}

			poseStack.rotateDegrees(Axis.XP, 90.0F);
			poseStack.rotateDegrees(Axis.ZP, -30.0F);

			item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
			poseStack.popPose();
		}
	}
}
