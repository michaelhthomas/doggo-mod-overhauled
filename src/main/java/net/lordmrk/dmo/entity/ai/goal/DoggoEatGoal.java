package net.lordmrk.dmo.entity.ai.goal;

import java.util.EnumSet;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoFeeling;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;

public class DoggoEatGoal extends Goal {

	private final DoggoEntity doggoEntity;
	private int eating;
	
	public DoggoEatGoal(DoggoEntity doggoEntity) {
		this.doggoEntity = doggoEntity;
		this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canContinueToUse() {
		return eating > 0;
	}

	@Override
	public boolean canUse() {
		if(this.doggoEntity.isInWaterOrBubble()) {
			return false;
		} else if(!this.doggoEntity.onGround()) {
			return false;
		} else if(this.doggoEntity.isAngry()) {
			return false;
		}
		
		return doggoEntity.isStackInMouthMeat() && (doggoEntity.getHealth() < doggoEntity.getMaxHealth() || doggoEntity.getRandom().nextFloat() < 0.01F);
	}
	
	@Override
	public boolean isInterruptable() {
		return this.doggoEntity.hasBeenHurt() || eating == 0;
	}

	@Override
	public void start() {
		eating = doggoEntity.getStackInMouth().getItem().getFoodProperties().getNutrition() * 4;
		
		if(doggoEntity.isOrderedToSit()) {
			doggoEntity.setInSittingPose(true);
		}

		doggoEntity.setAction(DoggoAction.EATING);
		doggoEntity.setActionTicking(true);
		doggoEntity.setFeeling(DoggoFeeling.HAPPY);
	}
	
	@Override
	public void stop() {
		doggoEntity.setStackInMouth(null);
		
		if(!doggoEntity.isOrderedToSit()) {
			doggoEntity.setInSittingPose(false);
		}

		doggoEntity.setAction(DoggoAction.NEUTRAL);
		doggoEntity.setActionTicking(false);
		doggoEntity.setFeeling(DoggoFeeling.NEUTRAL);
	}
	
	@Override
	public void tick() {
		if(eating % 2 == 0) {
			if(eating % 4 == 0) {
				if((int) doggoEntity.getHealth() + 1 <= doggoEntity.getMaxHealth()) {
					doggoEntity.heal(1);
				}
			}

			doggoEntity.level().playSound(null,
					doggoEntity.getX(), doggoEntity.getY(), doggoEntity.getZ(),
					SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.4f, 1f);
		}
		
		eating--;
	}
}
