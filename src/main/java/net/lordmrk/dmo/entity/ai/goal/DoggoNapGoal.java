package net.lordmrk.dmo.entity.ai.goal;

import java.util.EnumSet;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class DoggoNapGoal extends Goal {

	private final DoggoEntity doggoEntity;

	public DoggoNapGoal(DoggoEntity doggoEntity) {
		this.doggoEntity = doggoEntity;
		this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canContinueToUse() {
		return !this.doggoEntity.isInWaterOrBubble() && this.doggoEntity.onGround() && !this.doggoEntity.isOwnerClose();
	}

	@Override
	public boolean canUse() {
		if(!this.doggoEntity.isTame()) {
			return false;
		} else if(this.doggoEntity.isInWaterOrBubble()) {
			return false;
		} else if(!this.doggoEntity.onGround()) {
			return false;
		} else if(!this.doggoEntity.isInSittingPose()) {
			return false;
		} else if(this.doggoEntity.hasStackInMouth()) {
			return false;
		} else if(this.doggoEntity.isOwnerClose()) {
			return false;
		} else if(this.doggoEntity.isAngry()) {
			return false;
		} else {
			LivingEntity livingEntity = this.doggoEntity.getOwner();
			if(livingEntity == null) {
				return true;
			} else {
				return livingEntity.getLastHurtByMob() != null ? false : this.doggoEntity.getRandom().nextFloat() < 0.01F;
			}
		}
	}
	
	@Override
	public boolean isInterruptable() {
		return this.doggoEntity.hasBeenHurt() || this.doggoEntity.isOwnerClose();
	}

	@Override
	public void start() {
		this.doggoEntity.getNavigation().stop();
		this.doggoEntity.setAction(DoggoAction.NAPPING);
	}

	@Override
	public void stop() {
		this.doggoEntity.setAction(DoggoAction.NEUTRAL);
	}
	
	@Override
	public void tick() {
		
	}
}
