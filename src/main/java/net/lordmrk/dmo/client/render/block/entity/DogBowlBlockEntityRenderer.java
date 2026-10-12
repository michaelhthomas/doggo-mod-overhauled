package net.lordmrk.dmo.client.render.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class DogBowlBlockEntityRenderer implements BlockEntityRenderer<DogBowlEntity, DogBowlRenderState> {

	private final ItemModelResolver itemModelResolver;
	private final Font font;

	public DogBowlBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.itemModelResolver = ctx.itemModelResolver();
		this.font = ctx.font();
	}

	@Override
	public DogBowlRenderState createRenderState() {
		return new DogBowlRenderState();
	}

	@Override
	public void extractRenderState(DogBowlEntity entity, DogBowlRenderState state, float partialTicks, Vec3 cameraPosition, @Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);

		state.hasCustomName = entity.hasCustomName();
		state.customName = entity.hasCustomName() ? entity.getCustomName().getString() : "";
		state.amount = entity.hasFood() ? Mth.ceil(entity.getItem(0).getCount() / 16D) : 0;

		this.itemModelResolver.updateForTopItem(state.food, entity.getItem(0), ItemDisplayContext.FIXED, entity.getLevel(), null, 0);
	}

	@Override
	public void submit(DogBowlRenderState state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if(state.hasCustomName) {
			String name = this.font.plainSubstrByWidth(state.customName, 66);
			float centerX = (66f - this.font.width(name)) / 2f;
			float centerY = 5.5f;
			FormattedCharSequence sequence = FormattedCharSequence.forward(name, Style.EMPTY);

			matrixStack.pushPose();
			matrixStack.translate(0.84, 0.25, 0.1561);
			matrixStack.scale(0.010416667F, 0.010416667F, 0.010416667F);
			matrixStack.rotateDegrees(Axis.ZP, 180f);

			for(int i = 0; i < 4; i++) {
				matrixStack.rotateDegrees(Axis.YP, 90f);
				matrixStack.translate(-65.8, 0, -0.37);
				submitNodeCollector.submitText(matrixStack, centerX, centerY, sequence, false, Font.DisplayMode.NORMAL, state.lightCoords, 0xFFFFFF, 0, 0);
			}

			matrixStack.popPose();
		}

		if(state.amount > 0 && !state.food.isEmpty()) {
			matrixStack.pushPose();
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			matrixStack.rotateDegrees(Axis.XP, 90f);
			matrixStack.translate(1, 1, -0.15);

			for(int i = 0; i < state.amount; i++) {
				matrixStack.rotateDegrees(Axis.ZP, i * 80f);
				state.food.submit(matrixStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
				matrixStack.translate(0, 0, -0.05);
			}

			matrixStack.popPose();
		}
	}
}
