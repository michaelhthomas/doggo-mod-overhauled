package net.lordmrk.dmo.entity.ai.goal;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoFeeling;
import net.lordmrk.dmo.DoggoGoalData;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.EnumSet;

public class DoggoSniffingGoal extends Goal {

    private boolean canDig;
    private boolean canStop;
    private final DoggoEntity doggoEntity;
    private final DoggoGoalData goalData;
    private int minSniffs;
    private int minDigTime;
    private boolean navigateBack;
    private double startX;
    private double startY;
    private double startZ;

    private static final DoggoAction ACTION = DoggoAction.SNIFFING;

    public DoggoSniffingGoal(DoggoEntity doggoEntity) {
        this.doggoEntity = doggoEntity;
        this.goalData = doggoEntity.getGoalData(ACTION);
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Flag.MOVE, Goal.Flag.LOOK));
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

        return goalData.shouldStart();
    }

    @Override
    public boolean isInterruptable() {
        if(this.canStop) {
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
        this.doggoEntity.setAction(DoggoAction.SNIFFING);
        this.doggoEntity.setActionTicking(true);
        this.doggoEntity.setFeeling(DoggoFeeling.HAPPY);

        this.startX = this.doggoEntity.getX();
        this.startY = this.doggoEntity.getY();
        this.startZ = this.doggoEntity.getZ();
        this.minSniffs = 5 + this.doggoEntity.getRandom().nextInt(3);
        this.minDigTime = 60 + this.doggoEntity.getRandom().nextInt(5) * 10;

        this.goalData.start();
    }

    @Override
    public void stop() {
        this.doggoEntity.setAction(DoggoAction.NEUTRAL);
        this.doggoEntity.setActionTicking(false);
        this.doggoEntity.setFeeling(DoggoFeeling.NEUTRAL);
        this.canStop = false;
        this.canDig = false;
        this.navigateBack = false;

        this.goalData.stop();
    }

    @Override
    public void tick() {
        if(this.doggoEntity.getNavigation().isDone()) {
            if(this.canDig) {
                this.doggoEntity.setAction(DoggoAction.DIGGING);

                if(this.minDigTime == 0 && this.doggoEntity.getRandom().nextFloat() < 0.1f) {
                    if(this.doggoEntity.getRandom().nextInt(4) == 0) {
                        double x = Mth.sin(-this.doggoEntity.yHeadRot % 360 / 58) * 0.5f;
                        double z = Mth.cos(this.doggoEntity.yHeadRot % 360 / 58) * 0.5f;

                        ItemStack item;

                        if (this.doggoEntity.getRandom().nextInt(2) == 0) {
                            item = Items.IRON_NUGGET.getDefaultInstance();
                        } else {
                            item = Items.GOLD_NUGGET.getDefaultInstance();
                        }

                        Containers.dropItemStack(this.doggoEntity.level(), this.doggoEntity.getX() + x, this.doggoEntity.getY(), this.doggoEntity.getZ() + z, item);
                    }

                    this.canStop = true;
                    return;
                }

                if(this.minDigTime > 0) {
                    this.minDigTime--;
                }

                return;
            }

            if(this.minSniffs == 0 && this.doggoEntity.getRandom().nextFloat() < 0.1f) {
                this.canDig = true;
                this.canStop = this.doggoEntity.getRandom().nextFloat() < 0.5f;

                if(!this.doggoEntity.canStartDigging()) {
                    this.canStop = true;
                }
                return;
            }

            if(this.minSniffs > 0) {
                this.minSniffs--;
            }

            if(this.navigateBack) {
                this.doggoEntity.getNavigation().moveTo(startX, startY, startZ, 0.8);
            } else {
                float x = this.doggoEntity.getRandom().nextFloat() * 5 - this.doggoEntity.getRandom().nextFloat() * 5;
                float z = this.doggoEntity.getRandom().nextFloat() * 5 - this.doggoEntity.getRandom().nextFloat() * 5;
                this.doggoEntity.getNavigation().moveTo(startX + x, startY, startZ + z, 0.8);
            }

            this.navigateBack = !this.navigateBack;
        }
    }
}
