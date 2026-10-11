package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DoggoModelScratching extends DoggoEntityModel<DoggoEntity> {
	
	public DoggoModelScratching(ModelPart root) {
		super(root);
	}

	@Override
	public void prepareMobModel(DoggoEntity doggoEntity, float f, float g, float h) {
		if(doggoEntity.getScratchingSide() == 0) {
			scratchingLeft(doggoEntity);
		} else {
			scratchingRight(doggoEntity);
		}
	}
	
	private void scratchingLeft(DoggoEntity doggoEntity) {
		this.head.setPos(4.0F, 14F, -3.8F);
		this.head.yRot = DEGREES_10;
		this.head.xRot = -DEGREES_10 * (4f + Mth.sin(doggoEntity.getAnimationTick() * 2f) * 0.1f);
		this.head.zRot = DEGREES_10 * (5f + Mth.sin(doggoEntity.getAnimationTick() * 2f) * 0.1f);
		this.neck.setPos(-0.6F, 16.5F, -1.8F);
		this.neck.xRot = DEGREES_50;
		this.neck.yRot = -DEGREES_10;
		this.neck.zRot = DEGREES_20;
		this.torso.setPos(0.0F, 18.0F, 0.0F);
		this.torso.xRot = DEGREES_45;
		this.tail.setPos(-0.2F, 21.0F, 4.5F);
		this.tail.yRot = DEGREES_50;
		this.tail.xRot = DEGREES_75;

		this.leftFrontLeg.setPos(0.51F, 17.0F, -3.5F);
		this.leftFrontLeg.yRot = -DEGREES_30;
		this.leftFrontLeg.xRot = DEGREES_333;
		this.rightFrontLeg.setPos(-2.49F, 17.0F, -4.2F);
		this.rightFrontLeg.yRot = DEGREES_10;
		this.rightFrontLeg.xRot = DEGREES_333;

		this.leftHindLeg.setPos(1.1F, 22F, 1.6F);
		this.leftHindLeg.yRot = -DEGREES_50;
		this.leftHindLeg.xRot = Mth.cos(doggoEntity.getAnimationTick() * 2.5f) * 0.15f - DEGREES_130;
		this.rightHindLeg.setPos(-2.5F, 22.7F, 2.0F);
		this.rightHindLeg.yRot = -DEGREES_10;
		this.rightHindLeg.xRot = DEGREES_270;
	}
	
	private void scratchingRight(DoggoEntity doggoWolf) {
		this.head.setPos(-5.0F, 15F, -4.5F);
		this.head.yRot = -DEGREES_20;
		this.head.xRot = -DEGREES_10 * (4f + Mth.sin(doggoWolf.getAnimationTick() * 2f) * 0.1f);
		this.head.zRot = -DEGREES_10 * (5f + Mth.sin(doggoWolf.getAnimationTick() * 2f) * 0.1f);
		this.neck.setPos(-1.4F, 17.0F, -1.4F);
		this.neck.xRot = DEGREES_50;
		this.neck.yRot = DEGREES_10;
		this.neck.zRot = -DEGREES_20;
		this.torso.setPos(0.0F, 18.0F, 0.0F);
		this.torso.xRot = DEGREES_45;
		this.tail.setPos(-0.2F, 21.0F, 4.5F);
		this.tail.yRot = -DEGREES_50;
		this.tail.xRot = DEGREES_75;

		this.leftFrontLeg.setPos(0.51F, 17.0F, -4.5F);
		this.leftFrontLeg.yRot = -DEGREES_10;
		this.leftFrontLeg.xRot = DEGREES_333;
		this.rightFrontLeg.setPos(-2.49F, 17.0F, -3.0F);
		this.rightFrontLeg.yRot = DEGREES_30;
		this.rightFrontLeg.xRot = DEGREES_333;

		this.leftHindLeg.setPos(0.5F, 22.7F, 2.0F);
		this.leftHindLeg.yRot = DEGREES_20;
		this.leftHindLeg.xRot = DEGREES_270;
		this.rightHindLeg.setPos(-2.5F, 22.4F, 1.8F);
		this.rightHindLeg.yRot = DEGREES_50;
		this.rightHindLeg.xRot = Mth.cos(doggoWolf.getAnimationTick() * 2.5f) * 0.15f - DEGREES_130;
	}
}
