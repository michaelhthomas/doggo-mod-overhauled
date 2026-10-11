package net.lordmrk.dmo.entity.projectile.thrown;

import net.lordmrk.dmo.DoggoEntities;
import net.lordmrk.dmo.DoggoItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class YellowTennisBallEntity extends AbstractTennisBallEntity {

    public YellowTennisBallEntity(EntityType<? extends AbstractTennisBallEntity> entityType, Level world) {
        super(entityType, world);
    }

    public YellowTennisBallEntity(Level world, LivingEntity owner) {
        super(DoggoEntities.TENNIS_BALL_YELLOW_ENTITY, world, owner);
    }

    @Override
    protected Item getDefaultItem() {
        return DoggoItems.getTennisBall(DyeColor.YELLOW);
    }
}
