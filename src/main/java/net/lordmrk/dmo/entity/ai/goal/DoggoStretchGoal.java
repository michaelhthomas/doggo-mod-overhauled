package net.lordmrk.dmo.entity.ai.goal;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoGoalData;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class DoggoStretchGoal extends Goal {

    private int actionTick;
    private final DoggoEntity doggoEntity;
    private final DoggoGoalData goalData;

    private static final DoggoAction ACTION = DoggoAction.STRETCHING;

    public DoggoStretchGoal(DoggoEntity doggoEntity) {
        this.doggoEntity = doggoEntity;
        this.goalData = doggoEntity.getGoalData(ACTION);
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canContinueToUse() {
        return !isInterruptable();
    }

    @Override
    public boolean canUse() {
        if(!this.goalData.canStart()) {
            return false;
        }

        if(!this.doggoEntity.shouldGoalStart()) {
            return false;
        }

        if(this.doggoEntity.hasStackInMouth()) {
            return false;
        }

        if(this.doggoEntity.hasJustStretched()) {
            this.doggoEntity.setJustStretched(false);
            return false;
        }

        return goalData.shouldStart();
    }

    @Override
    public boolean isInterruptable() {
        if(this.actionTick <= 0) {
            return true;
        }

        if(this.doggoEntity.shouldGoalStop()) {
            return true;
        }

        return false;
    }

    @Override
    public void start() {
        this.doggoEntity.getNavigation().stop();
        this.doggoEntity.setAction(ACTION);
        this.doggoEntity.setActionTicking(true);

        this.actionTick = 180;

        this.goalData.start();
    }

    @Override
    public void stop() {
        this.doggoEntity.setAction(DoggoAction.NEUTRAL);
        this.doggoEntity.setActionTicking(false);
        this.doggoEntity.setJustStretched(true);

        this.goalData.stop();
    }

    @Override
    public void tick() {
        this.actionTick--;
    }
}
