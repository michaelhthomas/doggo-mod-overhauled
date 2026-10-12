package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.client.render.entity.DoggoRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DoggoModelListening extends DoggoEntityModel {

    public DoggoModelListening(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(DoggoRenderState state) {
        float anim = (float) (-(1 / (1 + Math.pow(2.7182818F, -(Mth.sin(state.animationTick / 8f) * 64f))) * 1.4f - 0.7f));

        if(state.inSittingPose) {
            sitting(anim);
        } else {
            standing(anim, state.walkAnimationPos, state.walkAnimationSpeed);
        }

        super.setupAnim(state);
    }

    private void standing(float anim, float f, float g) {
        this.head.setPos(-1.0F, 13.5F - anim / 2f, -7.0F);
        this.head.zRot = anim;
        this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);
        this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
        this.leftFrontLeg.yRot = 0f;
        this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
        this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + DEGREES_180) * 1.4F * g;
        this.leftHindLeg.yRot = 0f;
        this.neck.setPos(-1.0F, 14.0F, -3.0F);
        this.neck.xRot = DEGREES_90;
        this.neck.yRot = 0f;
        this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
        this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + DEGREES_180) * 1.4F * g;
        this.rightFrontLeg.yRot = 0f;
        this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
        this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
        this.rightHindLeg.yRot = 0f;
        this.tail.setPos(-1.0F, 12.0F, 8.0F);
        this.torso.setPos(0.0F, 14.0F, 2.0F);
        this.torso.xRot = DEGREES_90;
    }

    private void sitting(float anim) {
        this.head.setPos(-1.0F, 14.0F - anim / 2f, -7.3F);
        this.head.zRot = anim;
        this.leftFrontLeg.setPos(0.51F, 17.0F, -4.0F);
        this.leftFrontLeg.yRot = 0f;
        this.leftFrontLeg.xRot = DEGREES_333;
        this.leftHindLeg.setPos(0.5F, 22.7F, 2.0F);
        this.leftHindLeg.yRot = 0f;
        this.leftHindLeg.xRot = DEGREES_270;
        this.neck.setPos(-1.0F, 16.0F, -3.0F);
        this.neck.xRot = 1.2566371F;
        this.neck.yRot = 0.0F;
        this.rightHindLeg.setPos(-2.5F, 22.7F, 2.0F);
        this.rightHindLeg.yRot = 0f;
        this.rightHindLeg.xRot = DEGREES_270;
        this.rightFrontLeg.setPos(-2.49F, 17.0F, -4.0F);
        this.rightFrontLeg.yRot = 0f;
        this.rightFrontLeg.xRot = DEGREES_333;
        this.tail.setPos(-1.0F, 21.0F, 6.0F);
        this.torso.setPos(0.0F, 18.0F, 0.0F);
        this.torso.xRot = DEGREES_45;
    }
}
