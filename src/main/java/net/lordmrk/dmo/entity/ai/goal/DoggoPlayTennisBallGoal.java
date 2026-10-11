package net.lordmrk.dmo.entity.ai.goal;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoItemTags;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import java.util.EnumSet;
import java.util.List;

public class DoggoPlayTennisBallGoal extends Goal {

    private final DoggoEntity doggoEntity;
    private ThrowableItemProjectile thrownItemEntity;
    private ItemEntity itemEntity;
    private boolean failed;

    public DoggoPlayTennisBallGoal(DoggoEntity doggoEntity) {
        this.doggoEntity = doggoEntity;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canContinueToUse() {
        return !isInterruptable();
    }

    @Override
    public boolean canUse() {
        if(failed) {
            return false;
        }

        if(!this.doggoEntity.isTame()) {
            return false;
        }

        if(this.doggoEntity.isBaby()) {
            return false;
        }

        if(this.doggoEntity.isInWaterOrBubble()) {
            return false;
        }

        if(!this.doggoEntity.onGround()) {
            return false;
        }

        if(this.doggoEntity.hasStackInMouth()) {
            return false;
        }

        if(this.doggoEntity.isAngry()) {
            return false;
        }

        if(this.doggoEntity.getOwner() == null) {
            return false;
        }

        if(!this.doggoEntity.isReadyToPlay()) {
            return false;
        }

        List<ThrowableItemProjectile> list = this.doggoEntity.level().getEntitiesOfClass(
                ThrowableItemProjectile.class,
                this.doggoEntity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
                DoggoEntity.FOLLOWABLE_DROP_FILTER);

        if(list.size() == 0) {
            return false;
        }

        this.thrownItemEntity = list.get(0);
        return this.thrownItemEntity != null;
    }

    @Override
    public boolean isInterruptable() {
        if(failed) {
            return true;
        }

        if(this.doggoEntity.hasBeenHurt()) {
            return true;
        }

        if(this.thrownItemEntity != null || this.itemEntity != null || this.doggoEntity.hasStackInMouth(DoggoItemTags.TENNIS_BALL)) {
            return false;
        }

        return true;
    }

    @Override
    public void start() {
        this.doggoEntity.getNavigation().stop();
        this.doggoEntity.getNavigation().moveTo(thrownItemEntity, 1.5);
    }

    @Override
    public void stop() {
        this.doggoEntity.setAction(DoggoAction.NEUTRAL);
        this.doggoEntity.setReadyToPlay(false);
        this.thrownItemEntity = null;
        this.itemEntity = null;
        this.failed = false;
    }

    @Override
    public void tick() {
        if(this.doggoEntity.isInWater() && this.doggoEntity.getFluidHeight(FluidTags.WATER) > this.doggoEntity.getFluidJumpThreshold() || this.doggoEntity.isInLava()) {
            if (this.doggoEntity.getRandom().nextFloat() < 0.8F) {
                this.doggoEntity.getJumpControl().jump();
            }
        }

        if(this.thrownItemEntity != null) {
            if(this.thrownItemEntity.isRemoved()) {
                List<ItemEntity> list = this.doggoEntity.level().getEntitiesOfClass(
                        ItemEntity.class,
                        this.doggoEntity.getBoundingBox().inflate(20.0D, 8.0D, 20.0D),
                        DoggoEntity.PICKABLE_DROP_FILTER);

                for(ItemEntity itemEntity : list) {
                    if(itemEntity.getItem().is(DoggoItemTags.TENNIS_BALL)) {
                        this.itemEntity = itemEntity;
                        break;
                    }
                }

                if(this.itemEntity == null) {
                    fail();
                }

                this.thrownItemEntity = null;
                return;
            }

            if(this.doggoEntity.getNavigation().isDone()) {
                this.doggoEntity.getNavigation().moveTo(thrownItemEntity, 1.3);
            }
            return;
        }

        if(this.itemEntity != null) {
            if(this.itemEntity.isRemoved()) {
                if(!this.doggoEntity.hasStackInMouth(DoggoItemTags.TENNIS_BALL)) {
                    fail();
                }

                this.itemEntity = null;
                return;
            }

            if(this.doggoEntity.distanceToSqr(itemEntity) < 3) {
                this.doggoEntity.getNavigation().stop();

                if(this.doggoEntity.hasStackInMouth()) {
                    this.doggoEntity.dropStackInMouth();
                }

                this.doggoEntity.setStackInMouth(this.itemEntity.getItem());
                this.itemEntity.remove(Entity.RemovalReason.KILLED);
                this.doggoEntity.getNavigation().moveTo(this.doggoEntity.getOwner(), 1);
                return;
            }

            if(this.doggoEntity.getNavigation().isDone()) {
                this.doggoEntity.getNavigation().moveTo(itemEntity, 1.5);
            }
            return;
        }

        if(this.doggoEntity.hasStackInMouth(DoggoItemTags.TENNIS_BALL)) {
            if(this.doggoEntity.getNavigation().isDone()) {
                this.doggoEntity.getNavigation().moveTo(this.doggoEntity.getOwner(), 1);
            }

            if(this.doggoEntity.distanceToSqr(this.doggoEntity.getOwner()) < 3) {
                this.doggoEntity.getNavigation().stop();
                this.doggoEntity.dropStackInMouth();

                this.itemEntity = null;
                this.thrownItemEntity = null;
            }

            return;
        }
    }

    private void fail() {
        this.failed = true;
        this.doggoEntity.level().playSound(
                null,
                this.doggoEntity.getX(), this.doggoEntity.getY(), this.doggoEntity.getZ(),
                SoundEvents.WOLF_WHINE, SoundSource.NEUTRAL, 0.4f, 1.05f);
    }
}
