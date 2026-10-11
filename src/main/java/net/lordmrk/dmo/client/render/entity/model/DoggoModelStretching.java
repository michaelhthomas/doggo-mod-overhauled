package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DoggoModelStretching extends DoggoEntityModel<DoggoEntity> {

	public DoggoModelStretching(ModelPart root) {
		super(root);
	}

	@Override
	public void prepareMobModel(DoggoEntity doggoEntity, float f, float g, float h) {
		float anim = (float) (-(1 / (1 + Math.pow(2.7182818F, -(Mth.sin((doggoEntity.getAnimationTick() - 20) / 40F) * 12))) * 7));

		this.head.setPos(-1.0F, 13.5F - anim / 2.5F, -7.0F - anim / 3.8F);
		this.head.xRot = anim * DEGREES_5;
		this.head.zRot = 0f;
		this.torso.setPos(0.0F, 14.0F - anim / 2.2F, 2.0F);
		this.torso.xRot = DEGREES_90 - anim * DEGREES_5;
		this.neck.setPos(-1.0F, 14.0F - anim / 1.5F, -3.0F - anim / 3.8F);
		this.neck.yRot = 0f;
		this.neck.xRot = DEGREES_90 - anim * DEGREES_1;
		this.tail.setPos(-1.0F, 12.0F, 8.0F + anim / 3F);
		this.realTail.xRot = -anim * DEGREES_10;

		this.leftFrontLeg.setPos(0.5F, 16.0F - anim / 1.5F, -4.0F);
		this.leftFrontLeg.yRot = 0f;
		this.leftFrontLeg.xRot = anim * DEGREES_10;
		this.rightFrontLeg.setPos(-2.5F, 16.0F - anim / 1.5F, -4.0F);
		this.rightFrontLeg.yRot = 0f;
		this.rightFrontLeg.xRot = anim * DEGREES_10;

		this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
		this.leftHindLeg.yRot = 0f;
		this.leftHindLeg.xRot = -anim * (DEGREES_1 / 2);
		this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
		this.rightHindLeg.yRot = 0f;
		this.rightHindLeg.xRot = -anim * (DEGREES_1 / 2);
	}
}
