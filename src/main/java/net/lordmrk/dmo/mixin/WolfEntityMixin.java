package net.lordmrk.dmo.mixin;

import net.lordmrk.dmo.DoggoEntities;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfEntityMixin extends TamableAnimal {

    protected WolfEntityMixin(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"
            , at = @At(value = "INVOKE"
                , target = "Lnet/minecraft/world/entity/animal/Wolf;tame(Lnet/minecraft/world/entity/player/Player;)V")
            , cancellable = true)
    private void interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> ci) {
        DoggoEntity doggoEntity = DoggoEntities.DOGGO_ENTITY.spawn((ServerLevel)this.level(), (CompoundTag)null/*, this.getCustomName()*/, null, this.blockPosition(), MobSpawnType.CONVERSION, true, false);
        doggoEntity.tame(player);
        this.level().broadcastEntityEvent(doggoEntity, (byte)7);
        this.remove(RemovalReason.DISCARDED);

        ci.cancel();
        ci.setReturnValue(InteractionResult.SUCCESS);
    }
}
