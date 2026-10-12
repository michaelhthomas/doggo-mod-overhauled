package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.client.render.entity.DoggoRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DoggoModelEatingFromBowl extends DoggoEntityModel {
	
	public DoggoModelEatingFromBowl(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(DoggoRenderState state) {
		float f = state.walkAnimationPos;
		float g = state.walkAnimationSpeed;

		this.head.setPos(-1.0F, 13.5F, -7.0F);
		this.torso.xRot = DEGREES_90;
		this.neck.setPos(-1.0F, 14.0F, -3.0F);
		this.neck.xRot = this.torso.xRot;
		this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
		this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);
		this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
		this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + DEGREES_180) * 1.4F * g;
		this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + DEGREES_180) * 1.4F * g;
		this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
		this.neck.yRot = 0f;
		this.head.zRot = 0f;
		this.torso.setPos(0.0F, 14.0F, 2.0F);
		this.tail.setPos(-1.0F, 12.0F, 8.0F);
		this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
		this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
		this.rightHindLeg.yRot = 0f;
		this.leftHindLeg.yRot = 0f;
		this.rightFrontLeg.yRot = 0f;
		this.leftFrontLeg.yRot = 0f;

		super.setupAnim(state);
	}
}
