package net.lordmrk.dmo.mixin;

import net.lordmrk.dmo.DoggoEntities;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Wolf.class)
public abstract class WolfEntityMixin extends TamableAnimal {

    protected WolfEntityMixin(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "tryToTame(Lnet/minecraft/world/entity/player/Player;)V"
            , at = @At(value = "INVOKE"
                , target = "Lnet/minecraft/world/entity/animal/wolf/Wolf;tame(Lnet/minecraft/world/entity/player/Player;)V")
            , cancellable = true)
    private void convertToDoggo(Player player, CallbackInfo ci) {
        DoggoEntity doggoEntity = DoggoEntities.DOGGO_ENTITY.spawn((ServerLevel)this.level(), (ItemStack)null, null, this.blockPosition(), EntitySpawnReason.CONVERSION, true, false);
        doggoEntity.tame(player);
        this.level().broadcastEntityEvent(doggoEntity, (byte)7);
        this.discard();

        ci.cancel();
    }
}
