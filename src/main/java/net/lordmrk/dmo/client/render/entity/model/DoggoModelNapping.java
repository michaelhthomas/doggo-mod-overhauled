package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.model.geom.ModelPart;

public class DoggoModelNapping extends DoggoEntityModel<DoggoEntity> {
	
	public DoggoModelNapping(ModelPart root) {
		super(root);
	}

	@Override
	public void prepareMobModel(DoggoEntity doggoEntity, float f, float g, float h) {
		if(doggoEntity.isBaby()) {
			this.head.setPos(-2.5F, 16.0F, -6.0F);
		} else {
			this.head.setPos(-2.5F, 21.0F, -6.0F);
		}
		this.head.yRot = DEGREES_40;
		this.head.xRot = 0f;
		this.head.zRot = 0f;
		this.torso.setPos(0.0F, 21.0F, 2.0F);
		this.torso.xRot = DEGREES_90;
		this.neck.setPos(-1.4F, 21.5F, -2.2F);
		this.neck.yRot = DEGREES_10;
		this.neck.xRot = this.torso.xRot;
		this.neck.zRot = 0f;
		this.tail.setPos(-1.0F, 21.0F, 8.0F);
		this.tail.yRot = -DEGREES_30;
		this.tail.xRot = DEGREES_70;
		this.rightFrontLeg.setPos(-2.5F, 23.0F, -2.0F);
		this.rightFrontLeg.yRot = DEGREES_60;
		this.rightFrontLeg.xRot = -DEGREES_90;
		this.leftFrontLeg.setPos(0.5F, 23.0F, -4.0F);
		this.leftFrontLeg.yRot = DEGREES_10;
		this.leftFrontLeg.xRot = -DEGREES_90;
		this.rightHindLeg.setPos(-2.5F, 23.0F, 7.0F);
		this.rightHindLeg.yRot = DEGREES_60;
		this.rightHindLeg.xRot = -DEGREES_90;
		this.leftHindLeg.setPos(0.5F, 23.0F, 7.0F);
		this.leftHindLeg.yRot = DEGREES_50;
		this.leftHindLeg.xRot = -DEGREES_90;
	}
}
