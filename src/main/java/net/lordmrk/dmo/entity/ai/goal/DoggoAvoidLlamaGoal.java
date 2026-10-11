package net.lordmrk.dmo.entity.ai.goal;

import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.equine.Llama;

public class DoggoAvoidLlamaGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

    private final DoggoEntity doggoEntity;

    public DoggoAvoidLlamaGoal(DoggoEntity doggoEntity, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
        super(doggoEntity, fleeFromType, distance, slowSpeed, fastSpeed);
        this.doggoEntity = doggoEntity;
    }

    public boolean canUse() {
        if (super.canUse() && this.toAvoid instanceof Llama) {
            return !this.doggoEntity.isTame() && this.isScaredOf((Llama)this.toAvoid);
        } else {
            return false;
        }
    }

    private boolean isScaredOf(Llama llama) {
        return llama.getStrength() >= doggoEntity.getRandom().nextInt(5);
    }

    public void start() {
        this.doggoEntity.setTarget(null);
        super.start();
    }

    public void tick() {
        this.doggoEntity.setTarget(null);
        super.tick();
    }
}
