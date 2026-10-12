package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.client.render.entity.DoggoRenderState;
import net.minecraft.client.model.geom.ModelPart;

public class DoggoModelReadyPlayTime extends DoggoEntityModel {

    public DoggoModelReadyPlayTime(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(DoggoRenderState state) {
        playTime();
        super.setupAnim(state);
    }

    private void playTime() {
        this.head.setPos(-1.0F, 14.0F, -7.0F);
        this.head.zRot = 0f;
        this.leftFrontLeg.setPos(0.5F, 18.0F, -4.0F);
        this.leftFrontLeg.xRot = -DEGREES_50;
        this.leftFrontLeg.yRot = -DEGREES_30;
        this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
        this.leftHindLeg.xRot = 0f;
        this.leftHindLeg.yRot = 0f;
        this.neck.setPos(-1.0F, 15.5F, -2.5F);
        this.neck.xRot = DEGREES_90;
        this.neck.yRot = 0f;
        this.rightFrontLeg.setPos(-2.5F, 18.0F, -4.0F);
        this.rightFrontLeg.xRot = -DEGREES_50;
        this.rightFrontLeg.yRot = DEGREES_30;
        this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
        this.rightHindLeg.xRot = 0f;
        this.rightHindLeg.yRot = 0f;
        this.tail.setPos(-1.0F, 12.0F, 8.0F);
        this.torso.setPos(0.0F, 14.0F, 2.0F);
        this.torso.xRot = DEGREES_100;
    }
}
