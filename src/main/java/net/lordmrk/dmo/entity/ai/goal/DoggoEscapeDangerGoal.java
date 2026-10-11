package net.lordmrk.dmo.entity.ai.goal;

import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class DoggoEscapeDangerGoal extends PanicGoal {

    public DoggoEscapeDangerGoal(DoggoEntity doggoEntity, double speed) {
        super(doggoEntity, speed);
    }

    protected boolean shouldPanic() {
        return this.mob.isFreezing() || this.mob.isOnFire();
    }
}
