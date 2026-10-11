package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DoggoModelPlayInSnow extends DoggoEntityModel<DoggoEntity> {

    public DoggoModelPlayInSnow(ModelPart root) {
        super(root);
    }

    @Override
    public void prepareMobModel(DoggoEntity doggoEntity, float f, float g, float h) {
        playTime(doggoEntity);
    }

    private void playTime(DoggoEntity doggoEntity) {
        int animation = doggoEntity.getAnimation() % 45;
        float anim;
        float animHead = 0;

        if (animation > PI_13) {
            animHead = 0;
        } else if (animation > PI_11) {
            animHead = Mth.sin(animation / 2f) * 1.25f - 1.25f;
        } else if (animation > PI_10) {
            animHead = Mth.sin(animation / 2f) * 2.5f;
        }

        if (animation > PI_49 / 4f) {
            anim = 0;
        } else if (animation > PI_43 / 4f) {
            anim = Mth.sin((animation * 2f + PI) / 3f) - 1f;
        } else if (animation > PI_10) {
            anim = Mth.sin((animation * 2f + PI) / 3f) * 2f;
        } else if (animation > PI_8) {
            anim = Mth.cos(animation / 4f) * 5f;
            animHead = anim;
        } else if (animation > PI_3) {
            anim = Mth.cos((animation + PI_2) / 5f) * 3f + 2f;
            animHead = anim;
        } else {
            anim = Mth.cos(animation / 3f) / 2f - 0.5f;
            animHead = anim;
        }

        float frontLeg = Mth.clamp(anim, 0, anim);

        this.head.setPos(-1.0F, 13.5F - animHead, -7.0F + Mth.abs(anim) / 1.2f);
        this.head.xRot = DEGREES_60 - frontLeg * DEGREES_4;
        this.head.zRot = 0f;
        this.leftFrontLeg.setPos(0.5F, 16.0F - frontLeg, -4.0F + Mth.abs(frontLeg) / 1.1f);
        this.leftFrontLeg.xRot = -frontLeg * DEGREES_4;
        this.leftFrontLeg.yRot = 0f;
        this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
        this.leftHindLeg.xRot = 0f;
        this.leftHindLeg.yRot = 0f;
        this.neck.setPos(-1.0F, 14.0F - anim, -3.0F + Mth.abs(anim) * 1.05f);
        this.neck.xRot = DEGREES_90 - anim * DEGREES_6;
        this.neck.yRot = 0f;
        this.rightFrontLeg.setPos(-2.5F, 16.0F - frontLeg, -4.0F + Mth.abs(frontLeg) / 1.1f);
        this.rightFrontLeg.xRot = -frontLeg * DEGREES_4;
        this.rightFrontLeg.yRot = 0f;
        this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
        this.rightHindLeg.xRot = 0f;
        this.rightHindLeg.yRot = 0f;
        this.tail.setPos(-1.0F, 12.0F + anim / 1.25f, 8.0F + Mth.abs(anim) / 1.5f);
        this.torso.setPos(0.0F, 14.0F - anim / 2.5f, 2.0F + Mth.abs(anim) / 1.5f);
        this.torso.xRot = DEGREES_90 - anim * DEGREES_10;
    }
}
