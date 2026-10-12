package net.lordmrk.dmo.entity.projectile.thrown;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractTennisBallEntity extends ThrowableItemProjectile {

    private static final Set<Block> BREAKABLE_BLOCKS = new HashSet<>();

    public AbstractTennisBallEntity(EntityType<? extends AbstractTennisBallEntity> entityType, Level world) {
        super(entityType, world);
    }

    public AbstractTennisBallEntity(EntityType<? extends AbstractTennisBallEntity> entityType, Level world, LivingEntity owner) {
        super(entityType, owner, world, ItemStack.EMPTY);
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if(!this.level().isClientSide()) {
			if(hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = new BlockPos((int) hitResult.getLocation().x(), (int) hitResult.getLocation().y(), (int) hitResult.getLocation().z());
				BlockState state = this.level().getBlockState(pos);

				if(BREAKABLE_BLOCKS.contains(state.getBlock())) {
					this.level().destroyBlock(pos, false);
				}
			}

            Containers.dropItemStack(this.level(), this.getX(), this.getY(), this.getZ(), this.getDefaultItem().getDefaultInstance());

            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    static {
        BREAKABLE_BLOCKS.add(Blocks.GLASS_PANE);

        for(DyeColor color : DyeColor.values()) {
            BREAKABLE_BLOCKS.add(Blocks.STAINED_GLASS_PANE.pick(color));
        }
    }
}
