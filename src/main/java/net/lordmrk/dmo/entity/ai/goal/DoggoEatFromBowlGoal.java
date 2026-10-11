package net.lordmrk.dmo.entity.ai.goal;

import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoFeeling;
import net.lordmrk.dmo.block.DogBowl;
import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class DoggoEatFromBowlGoal extends Goal {

	private final DoggoEntity doggoEntity;
	private boolean failed;
	private Optional<BlockPos> bowlPos;
	private int eating;
	private int waitIfFailed;
	private int whineTime;

	public DoggoEatFromBowlGoal(DoggoEntity doggoEntity) {
		this.doggoEntity = doggoEntity;
		this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canContinueToUse() {
		return !failed && waitIfFailed == 0 && this.doggoEntity.getHealth() < this.doggoEntity.getMaxHealth() && !this.doggoEntity.isPathFinding() && isNextToBowl() || eating > 0 || whineTime > 0;
	}

	@Override
	public boolean canUse() {
		if(!this.doggoEntity.isTame()) {
			return false;
		} else if(this.doggoEntity.isInWaterOrBubble()) {
			return false;
		} else if(!this.doggoEntity.onGround()) {
			return false;
		} else if(this.doggoEntity.hasStackInMouth()) {
			return false;
		} else if(this.doggoEntity.isAngry()) {
			return false;
		} else if(this.doggoEntity.getHealth() == this.doggoEntity.getMaxHealth()){
			return false;
		} else if(this.waitIfFailed > 0){
			return this.doggoEntity.getRandom().nextFloat() < 0.02F;
		}
		
		return true;
	}
	
	@Override
	public boolean isInterruptable() {
		return this.doggoEntity.hasBeenHurt() || this.doggoEntity.getHealth() == this.doggoEntity.getMaxHealth();
	}

	@Override
	public void start() {
		if(waitIfFailed > 0) {
			waitIfFailed--;
			return;
		}
		
		this.failed = false;
		this.eating = 0;
		this.whineTime = 0;
		this.bowlPos = BlockPos.findClosestMatch(
				this.doggoEntity.blockPosition(),
				16, 8,
				(b) -> DogBowl.canEatFromBowl(b, doggoEntity));
		
		try {
			Vec3 closestSide = getClosestSide(new Vec3(bowlPos.get().getX(), bowlPos.get().getY(), bowlPos.get().getZ()));
			
			this.doggoEntity.getNavigation().moveTo(closestSide.x(), closestSide.y(), closestSide.z(), 1);
		} catch(NoSuchElementException ex) {
			fail();
		}
	}

	@Override
	public void stop() {
		this.doggoEntity.setAction(DoggoAction.NEUTRAL);
		this.doggoEntity.setActionTicking(false);
		this.doggoEntity.setFeeling(DoggoFeeling.NEUTRAL);
	}
	
	@Override
	public void tick() {
		if(!failed) {
			if(this.doggoEntity.isAction(DoggoAction.EATING_FROM_BOWL)) {
				this.doggoEntity.getLookControl().setLookAt(
						this.doggoEntity.getBowlPos().getX() + 0.5,
						0,
						this.doggoEntity.getBowlPos().getZ() + 0.5, 10, 10);
				
				if(eating == 0) {
					DogBowlEntity dogBowlEntity = (DogBowlEntity) this.doggoEntity.level().getBlockEntity(this.doggoEntity.getBowlPos());

					if(dogBowlEntity != null && dogBowlEntity.hasFood()) {
						eating = dogBowlEntity.getFoodHunger();
						dogBowlEntity.foodEaten();
					} else if((int) this.doggoEntity.getHealth() + 1 <= this.doggoEntity.getMaxHealth()) {
						failAndWhine();
						return;
					}
				}
				
				if(eating % 2 == 0) {
					if(eating % 4 == 0) {
						if((int) this.doggoEntity.getHealth() + 1 <= this.doggoEntity.getMaxHealth()) {
							this.doggoEntity.heal(1);
						}
					}

					this.doggoEntity.level().playSound(null,
							this.doggoEntity.getX(), this.doggoEntity.getY(), this.doggoEntity.getZ(),
							SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.4f, 1f);
				}
				
				if(eating > 0) {
					eating--;
				}
			} else if(this.bowlPos.isPresent() && isNextToBowl()) {
				if(this.doggoEntity.isPathFinding()) {
					this.doggoEntity.getNavigation().stop();
				}

				this.doggoEntity.setBowlPos(bowlPos.get());
				this.doggoEntity.setAction(DoggoAction.EATING_FROM_BOWL);
				this.doggoEntity.setActionTicking(true);
				this.doggoEntity.setFeeling(DoggoFeeling.HAPPY);
				this.doggoEntity.dropStackInMouth();
			} else if(!this.doggoEntity.isPathFinding()) {
				fail();
			}
		} else if(whineTime > 0) {
			whineTime--;
		}
	}
	
	private void fail() {
		this.failed = true;
		this.waitIfFailed = 3;
	}
	
	private void failAndWhine() {
		fail();
		this.doggoEntity.level().playSound(
				null,
				this.doggoEntity.getX(), this.doggoEntity.getY(), this.doggoEntity.getZ(),
				SoundEvents.WOLF_WHINE, SoundSource.NEUTRAL, 0.4f, 1.05f);
		whineTime = 40;
	}
	
	private Vec3 getClosestSide(Vec3 position) {
		Vec3[] sides = new Vec3[] {new Vec3(0.4, 0, 0), new Vec3(-0.4, 0, 0), new Vec3(0, 0, 0.4), new Vec3(0, 0, -0.4)};
		
		Vec3 closestSide = position.add(sides[0]).add(0.5, 0, 0.5);
		double closest = position.add(sides[0]).add(0.5, 0, 0.5).distanceTo(this.doggoEntity.position());
		
		for(int i = 1; i < sides.length; i++) {
			double close = position.add(sides[i]).add(0.5, 0, 0.5).distanceTo(this.doggoEntity.position());
			
			if(close < closest) {
				closest = close;
				closestSide = position.add(sides[i]).add(0.5, 0, 0.5);
			}
		}
		
		return closestSide;
	}
	
	private boolean isNextToBowl() {
		try {
			if(this.doggoEntity.blockPosition().closerThan(this.bowlPos.get(), 1.05)) {
				return true;
			}
			
			return false;
		} catch(NoSuchElementException ex) {
			return false;
		}
	}
}
