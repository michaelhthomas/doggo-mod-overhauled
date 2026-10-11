package net.lordmrk.dmo.client.render.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DogBowlBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<DogBowlEntity> {

	private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
	private final Font textRenderer = Minecraft.getInstance().font;

	public DogBowlBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {

	}

	@Override
	public void render(DogBowlEntity entity, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		if(entity.hasCustomName()) {
			String name = textRenderer.plainSubstrByWidth(entity.getCustomName().getString(), 66);
			float centerX = (66f - textRenderer.width(name)) / 2f;
			float centerY = 5.5f;

			matrixStack.pushPose();
			matrixStack.translate(0.84, 0.25, 0.1561);
			matrixStack.scale(0.010416667F, 0.010416667F, 0.010416667F);
			matrixStack.mulPose(Axis.ZP.rotationDegrees(180f));
			for(int i = 0; i < 4; i++) {
				matrixStack.mulPose(Axis.YP.rotationDegrees(90f));
				matrixStack.translate(-65.8, 0, -0.37);
				this.textRenderer.drawInBatch(name, centerX, centerY, 0xFFFFFF, false,
						matrixStack.last().pose(), vertexConsumerProvider, Font.DisplayMode.NORMAL, 0, light);
			}
			matrixStack.popPose();
		}

		if(entity.hasFood()) {
			int amount = Mth.ceil(entity.getItem(0).getCount() / 16D);

			matrixStack.pushPose();
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			matrixStack.mulPose(Axis.XP.rotationDegrees(90f));
			matrixStack.translate(1, 1, -0.15);

			for(int i = 0; i < amount; i++) {
				matrixStack.mulPose(Axis.ZP.rotationDegrees(i * 80f));
				this.itemRenderer.renderStatic(entity.getItem(0), ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider, entity.getLevel(), 0);
				matrixStack.translate(0, 0, -0.05);
			}
			matrixStack.popPose();
		}
	}
}
