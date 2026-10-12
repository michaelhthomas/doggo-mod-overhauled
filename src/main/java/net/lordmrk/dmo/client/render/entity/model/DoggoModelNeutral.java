package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.client.render.entity.DoggoRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DoggoModelNeutral extends DoggoEntityModel {
	
	public DoggoModelNeutral(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(DoggoRenderState state) {
		if(state.inLieDownPose) {
			lieDown();
		} else if(state.inSittingPose) {
			sitting();
		} else {
			standing(state.walkAnimationPos, state.walkAnimationSpeed);
		}
		
		shakingAndBegging(state);
		super.setupAnim(state);
	}

	private void lieDown() {
		this.head.setPos(-1.0F, 18.0F, -7.0F);
		this.head.zRot = 0f;
		this.torso.setPos(0.0F, 20.0F, 2.0F);
		this.torso.xRot = DEGREES_90;
		this.neck.setPos(-1.0F, 20.5F, -3.0F);
		this.neck.yRot = 0f;
		this.neck.xRot = this.torso.xRot;
		this.tail.setPos(-1.0F, 18.0F, 8.0F);

		this.leftFrontLeg.setPos(0.5F, 23.0F, -4.0F);
		this.leftFrontLeg.yRot = -DEGREES_15;
		this.leftFrontLeg.xRot = -DEGREES_90;
		this.rightFrontLeg.setPos(-2.5F, 23.0F, -4.0F);
		this.rightFrontLeg.yRot = DEGREES_15;
		this.rightFrontLeg.xRot = -DEGREES_90;

		this.leftHindLeg.setPos(0.5F, 23.0F, 7.0F);
		this.leftHindLeg.yRot = -DEGREES_25;
		this.leftHindLeg.xRot = -DEGREES_90;
		this.rightHindLeg.setPos(-2.5F, 23.0F, 7.0F);
		this.rightHindLeg.yRot = DEGREES_25;
		this.rightHindLeg.xRot = -DEGREES_90;
	}
	
	private void shakingAndBegging(DoggoRenderState state) {
		this.realHead.zRot = state.getBegAnimationProgress() + state.getShakeAnimationProgress(0.0F);
		this.neck.zRot = state.getShakeAnimationProgress(-0.08F);
		this.torso.zRot = state.getShakeAnimationProgress(-0.16F);
		this.realTail.zRot = state.getShakeAnimationProgress(-0.2F);
	}

	private void sitting() {
		this.head.setPos(-1.0F, 14.0F, -7.3F);
		this.head.zRot = 0f;
		this.neck.setPos(-1.0F, 16.0F, -3.0F);
		this.neck.xRot = DEGREES_72;
		this.neck.yRot = 0.0F;
		this.torso.setPos(0.0F, 18.0F, 0.0F);
		this.torso.xRot = DEGREES_45;
		this.tail.setPos(-1.0F, 21.0F, 6.0F);
		this.rightHindLeg.setPos(-2.5F, 22.7F, 2.0F);
		this.rightHindLeg.yRot = 0f;
		this.rightHindLeg.xRot = DEGREES_270;
		this.leftHindLeg.setPos(0.5F, 22.7F, 2.0F);
		this.leftHindLeg.yRot = 0f;
		this.leftHindLeg.xRot = DEGREES_270;
		this.rightFrontLeg.setPos(-2.49F, 17.0F, -4.0F);
		this.rightFrontLeg.yRot = 0f;
		this.rightFrontLeg.xRot = DEGREES_333;
		this.leftFrontLeg.setPos(0.51F, 17.0F, -4.0F);
		this.leftFrontLeg.yRot = 0f;
		this.leftFrontLeg.xRot = DEGREES_333;
	}
	
	private void standing(float f, float g) {
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
	}
}
