package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DoggoModelSniffing extends DoggoEntityModel<DoggoEntity> {

    public DoggoModelSniffing(ModelPart root) {
        super(root);
    }

    @Override
    public void prepareMobModel(DoggoEntity doggoEntity, float f, float g, float h) {
        this.head.setPos(-1.0F, 16.5F, -5.5F);
        this.head.xRot = DEGREES_70;
        this.head.zRot = 0f;
        this.torso.setPos(0.0F, 14.0F, 2.0F);
        this.torso.xRot = DEGREES_90;
        this.neck.setPos(-1.0F, 15.0F, -1.5F);
        this.neck.yRot = 0f;
        this.neck.xRot = DEGREES_110;
        this.tail.setPos(-1.0F, 12.0F, 8.0F);
        this.realTail.xRot = 0f;

        this.leftFrontLeg.setPos(0.5F, 16.0F, -3.0F);
        this.leftFrontLeg.yRot = 0f;
        this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
        this.rightFrontLeg.setPos(-2.5F, 16.0F, -3.0F);
        this.rightFrontLeg.yRot = 0f;
        this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + DEGREES_180) * 1.4F * g;

        this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
        this.leftHindLeg.yRot = 0f;
        this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + DEGREES_180) * 1.4F * g;
        this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
        this.rightHindLeg.yRot = 0f;
        this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
    }
}
