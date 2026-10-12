package net.lordmrk.dmo.entity.ai.goal;

import java.util.EnumSet;

import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class DoggoBegGoal extends Goal {

	private final DoggoEntity doggoEntity;
	private Player begFrom;
	private final Level world;
	private final float begDistance;
	private int timer;
	private final TargetingConditions validPlayerPredicate;

	public DoggoBegGoal(DoggoEntity doggoEntity, float begDistance) {
	      this.doggoEntity = doggoEntity;
	      this.world = doggoEntity.level();
	      this.begDistance = begDistance;
	      this.validPlayerPredicate = TargetingConditions.forNonCombat().range(begDistance);
	      this.setFlags(EnumSet.of(Goal.Flag.LOOK));
	}

	public boolean canUse() {
		if(this.doggoEntity.hasStackInMouth()) {
			return false;
		}

		if(!(this.world instanceof ServerLevel level)) {
			return false;
		}

		this.begFrom = this.world.getNearestPlayer(this.doggoEntity.getX(), this.doggoEntity.getY(), this.doggoEntity.getZ(), this.begDistance, entity -> entity instanceof Player player && this.isAttractive(player) && this.validPlayerPredicate.test(level, player, this.doggoEntity));
		return this.begFrom != null && this.isAttractive(this.begFrom);
	}

	public boolean canContinueToUse() {
		if(!this.begFrom.isAlive()) {
			return false;
		} else if(this.doggoEntity.distanceToSqr(this.begFrom) > (double) (this.begDistance * this.begDistance)) {
			return false;
		} else if(this.doggoEntity.hasStackInMouth()) {
			return false;
		} else if(this.doggoEntity.isAngry()) {
			return false;
		} else {
			return this.timer > 0 && this.isAttractive(this.begFrom);
		}
	}

	public void start() {
		this.doggoEntity.setBegging(true);
		this.timer = 40 + this.doggoEntity.getRandom().nextInt(40);
	}

	public void stop() {
		this.doggoEntity.setBegging(false);
		this.begFrom = null;
	}

	public void tick() {
		this.doggoEntity.getLookControl().setLookAt(this.begFrom.getX(), this.begFrom.getEyeY(), this.begFrom.getZ(), 10.0F, (float) this.doggoEntity.getMaxHeadXRot());
		--this.timer;
	}

	private boolean isAttractive(Player player) {
		for (InteractionHand hand : InteractionHand.values()) {
			ItemStack itemStack = player.getItemInHand(hand);

			if (this.doggoEntity.isTame() && itemStack.getItem() == Items.BONE) {
				return true;
			}

			if (this.doggoEntity.isFood(itemStack)) {
				return true;
			}
		}

		return false;
	}
}
