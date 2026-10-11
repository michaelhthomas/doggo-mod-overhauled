package net.lordmrk.dmo.entity.ai.goal;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoGoalData;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import java.util.EnumSet;

public class DoggoPlayInSnowGoal extends Goal {

    private int actionTick;
    private final DoggoEntity doggoEntity;
    private final DoggoGoalData goalData;

    private static final DoggoAction ACTION = DoggoAction.PLAY_IN_SNOW;

    public DoggoPlayInSnowGoal(DoggoEntity doggoEntity) {
        this.doggoEntity = doggoEntity;
        this.goalData = doggoEntity.getGoalData(ACTION);
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.goalData.canStart()) {
            return false;
        }

        if (!this.doggoEntity.shouldGoalStart()) {
            return false;
        }

        if (this.doggoEntity.hasStackInMouth()) {
            return false;
        }

        if (!isInSnow()) {
            return false;
        }

        return goalData.shouldStart();
    }

    @Override
    public boolean isInterruptable() {
        if (this.actionTick <= 0) {
            return true;
        }

        if (this.doggoEntity.shouldGoalStop()) {
            return true;
        }

        return false;
    }

    private boolean isInSnow() {
        if (this.doggoEntity.level().getBlockState(this.doggoEntity.blockPosition()).getBlock() == Blocks.SNOW) {
            return true;
        }

        if (this.doggoEntity.level().getBlockState(this.doggoEntity.blockPosition().below()).getBlock() == Blocks.SNOW_BLOCK) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !isInterruptable();
    }

    @Override
    public void start() {
        this.doggoEntity.getNavigation().stop();
        this.doggoEntity.setAction(ACTION);
        this.doggoEntity.setActionTicking(true);

        this.actionTick = this.doggoEntity.getRandom().nextBoolean() ? 45 : 68;

        this.goalData.start();
    }

    @Override
    public void stop() {
        this.doggoEntity.setAction(DoggoAction.NEUTRAL);
        this.doggoEntity.setActionTicking(false);

        this.goalData.stop();
    }

    @Override
    public void tick() {
        this.actionTick--;
    }
}
