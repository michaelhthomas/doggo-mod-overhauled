package net.lordmrk.dmo.entity.ai.goal;

import net.lordmrk.dmo.*;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import java.util.EnumSet;
import java.util.List;

public class DoggoReadyPlayTimeGoal extends Goal {

    private final DoggoEntity doggoEntity;
    private int mouthDelay = 0;

    public DoggoReadyPlayTimeGoal(DoggoEntity doggoEntity) {
        this.doggoEntity = doggoEntity;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return !isInterruptable();
    }

    @Override
    public boolean canUse() {
        if(!this.doggoEntity.isTame()) {
            return false;
        }

        if(this.doggoEntity.isInWaterOrBubble()) {
            return false;
        }

        if(!this.doggoEntity.onGround()) {
            return false;
        }

        if(this.doggoEntity.isInSittingPose()) {
            return false;
        }

        if(this.doggoEntity.hasStackInMouth()) {
            return false;
        }

        if(this.doggoEntity.isAngry()) {
            return false;
        }

        LivingEntity livingEntity = this.doggoEntity.getOwner();

        if(livingEntity == null) {
            return false;
        }

        if(livingEntity.getLastHurtByMob() != null) {
            return false;
        }

        if(livingEntity.getItemInHand(InteractionHand.MAIN_HAND) == null) {
            return false;
        }

        if(!livingEntity.getItemInHand(InteractionHand.MAIN_HAND).is(DoggoItemTags.TENNIS_BALL)) {
            return false;
        }

        if(!this.doggoEntity.isEntityClose(livingEntity, 16.0D)) {
            return false;
        }

        return this.doggoEntity.getRandom().nextFloat() < 0.2F;
    }

    @Override
    public boolean isInterruptable() {
        if(this.doggoEntity.hasBeenHurt()) {
            return true;
        }

        if(this.doggoEntity.isInWaterOrBubble()) {
            return true;
        }

        if(!this.doggoEntity.onGround()) {
            return true;
        }

        LivingEntity livingEntity = this.doggoEntity.getOwner();

        if(livingEntity == null) {
            return true;
        }

        if(livingEntity.getLastHurtByMob() != null) {
            return true;
        }

        if(livingEntity.getItemInHand(InteractionHand.MAIN_HAND) == null) {
            return true;
        }

        if(!livingEntity.getItemInHand(InteractionHand.MAIN_HAND).is(DoggoItemTags.TENNIS_BALL)) {
            return true;
        }

        if(!this.doggoEntity.isEntityClose(livingEntity, 16.0D)) {
            return true;
        }

        List<ThrowableItemProjectile> list = this.doggoEntity.level().getEntitiesOfClass(
                ThrowableItemProjectile.class,
                this.doggoEntity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
                DoggoEntity.FOLLOWABLE_DROP_FILTER);

        return list.size() > 0;
    }

    @Override
    public void start() {
        this.doggoEntity.getNavigation().stop();
        this.doggoEntity.setAction(DoggoAction.PLAY_TIME);
        this.doggoEntity.setActionTicking(true);
        this.doggoEntity.setFeeling(DoggoFeeling.HAPPY);
        this.doggoEntity.setReadyToPlay(true);
    }

    @Override
    public void stop() {
        this.doggoEntity.setAction(DoggoAction.NEUTRAL);
        this.doggoEntity.setActionTicking(false);
        this.doggoEntity.setFeeling(DoggoFeeling.NEUTRAL);
    }

    @Override
    public void tick() {
        if(mouthDelay > 0) {
            mouthDelay--;
        } else if(mouthDelay == 0) {
            this.doggoEntity.setMouthOpened(true);
            mouthDelay = 60;
        }

        LivingEntity livingEntity = this.doggoEntity.getOwner();

        if(livingEntity == null) {
            return;
        }

        this.doggoEntity.getLookControl().setLookAt(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
    }
}
