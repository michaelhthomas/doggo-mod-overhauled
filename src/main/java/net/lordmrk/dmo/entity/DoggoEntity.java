package net.lordmrk.dmo.entity;

import com.google.common.collect.Sets;
import net.lordmrk.dmo.*;
import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.lordmrk.dmo.entity.ai.goal.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class DoggoEntity extends TamableAnimal implements NeutralMob {

    private static final EntityDataAccessor<DoggoAction> ACTION;
    private static final EntityDataAccessor<Boolean> ACTION_TICKING;
    private static final EntityDataAccessor<Integer> ANGER_TIME;
    private static final EntityDataAccessor<Boolean> BEGGING;
    private static final EntityDataAccessor<BlockPos> BOWL_POS;
    private static final EntityDataAccessor<Integer> COLLAR_COLOR;
    private static final EntityDataAccessor<DoggoFeeling> FEELING;
    private static final EntityDataAccessor<Boolean> IN_LIE_DOWN_POSE;
    private static final EntityDataAccessor<Boolean> MOUTH_OPENED;
    private static final EntityDataAccessor<ItemStack> MOUTH_STACK;
    private static final EntityDataAccessor<Integer> SCRATCHING_SIDE;

    private static final Set<Block> DIGGABLE_BLOCKS = Sets.newHashSet(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND, Blocks.RED_SAND, Blocks.GRAVEL, Blocks.SNOW_BLOCK);
    private float begAnimationProgress;
    private float lastBegAnimationProgress;
    private boolean furWet;
    private boolean canShakeWaterOff;
    private float shakeProgress;
    private float lastShakeProgress;
    private static final UniformInt ANGER_TIME_RANGE;
    @Nullable
    private UUID angryAt;

    public static final Predicate<ItemEntity> PICKABLE_DROP_FILTER;
    public static final Predicate<Entity> FOLLOWABLE_DROP_FILTER;

    public static final double MAX_DISTANCE_FROM_DOGGO = 99.0D;
    public static final double DEFAULT_DISTANCE_FROM_DOGGO = 36.0D;

    //Client sided only
    private float animationTick;
    private int animation;

    //Server sided only
    private boolean justStretched;
    private int mouthOpenedTick;
    private boolean readyToPlay = false;
    private Map<DoggoAction, DoggoGoalData> goalDataMap;

    public DoggoEntity(EntityType<? extends DoggoEntity> entityType, Level world) {
        super(entityType, world);
        this.setTame(true);
        this.setOrderedToSit(true);
        this.setInSittingPose(true);
        this.setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_POWDER_SNOW, -1.0F);
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof AbstractHorse) {
            AbstractHorse horseEntity = (AbstractHorse) target;

            if (horseEntity.isTamed()) {
                return false;
            }

            return true;
        }

        if (target instanceof Creeper) {
            return false;
        }

        if (target instanceof Ghast) {
            return false;
        }

        if (target instanceof Wolf) {
            Wolf wolfEntity = (Wolf) target;

            if (!wolfEntity.isTame()) {
                return true;
            }

            if (wolfEntity.getOwner() == owner) {
                return false;
            }

            return true;
        }

        if (target instanceof TamableAnimal) {
            TamableAnimal tameableEntity = (TamableAnimal) target;

            if (tameableEntity.isTame()) {
                return false;
            }

            return true;
        }

        if (target instanceof Player && owner instanceof Player) {
            Player targetPlayer = (Player) target;
            Player ownerPlayer = (Player) owner;

            if (!ownerPlayer.canHarmPlayer(targetPlayer)) {
                return false;
            }

            return true;
        }

        return false;
    }

    public boolean canBeLeashed(Player player) {
        return !this.isAngry() && super.canBeLeashed(player);
    }

    public boolean canStartDigging() {
        return DIGGABLE_BLOCKS.contains(this.level().getBlockState(this.blockPosition().below()).getBlock());
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    public static AttributeSupplier.Builder createDoggoAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        DoggoEntity doggoEntity = DoggoEntities.DOGGO_ENTITY.create(world);
        UUID uuid = this.getOwnerUUID();

        if (uuid != null) {
            doggoEntity.setOwnerUUID(uuid);
        }

        return doggoEntity;
    }

    public boolean hurt(DamageSource source, float amount) {
        dropStackInMouth();
        return super.hurt(source, amount);
    }

    public void dropStackInMouth() {
        if(hasStackInMouth()) {
            Containers.dropItemStack(this.level(), this.getX(), this.getY(), this.getZ(), getStackInMouth());
            setStackInMouth(null);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (getAction() == DoggoAction.NAPPING) {
            return null;
        }

        if (this.isAngry()) {
            return SoundEvents.WOLF_GROWL;
        }

        if (this.random.nextInt(2) == 0) {
            return null;
        }

        if (this.isTame() && this.random.nextInt(3) == 0) {
            if(this.getHealth() < 10.0F) {
                return SoundEvents.WOLF_WHINE;
            }

            if(!this.hasStackInMouth() && !this.level().isClientSide) {
                this.setMouthOpened(true);
                return SoundEvents.WOLF_PANT;
            }
        }

        return SoundEvents.WOLF_AMBIENT;
    }

    public int getAnimation() {
        return animation;
    }

    public float getAnimationTick() {
        return animationTick;
    }

    public DoggoAction getAction() {
        return this.entityData.get(ACTION);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(ANGER_TIME);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    public float getBegAnimationProgress(float tickDelta) {
        return Mth.lerp(tickDelta, this.lastBegAnimationProgress, this.begAnimationProgress) * 0.15F * 3.1415927F;
    }

    public BlockPos getBowlPos() {
        return this.entityData.get(BOWL_POS);
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(COLLAR_COLOR));
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    public DoggoFeeling getFeeling() {
        return this.entityData.get(FEELING);
    }

    public float getFurWetBrightnessMultiplier(float tickDelta) {
        return Math.min(0.5F + Mth.lerp(tickDelta, this.lastShakeProgress, this.shakeProgress) / 2.0F * 0.5F, 1.0F);
    }

    public DoggoGoalData getGoalData(DoggoAction action) {
        return goalDataMap.get(action);
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WOLF_HURT;
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public int getScratchingSide() {
        return this.entityData.get(SCRATCHING_SIDE);
    }

    public float getShakeAnimationProgress(float tickDelta, float f) {
        float g = (Mth.lerp(tickDelta, this.lastShakeProgress, this.shakeProgress) + f) / 1.8F;
        if (g < 0.0F) {
            g = 0.0F;
        } else if (g > 1.0F) {
            g = 1.0F;
        }

        return Mth.sin(g * 3.1415927F) * Mth.sin(g * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public ItemStack getStackInMouth() {
        return this.entityData.get(MOUTH_STACK);
    }

    public float getTailAngle() {
        if (this.isAngry()) {
            return 1.5393804F;
        } else {
            return this.isTame() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * 3.1415927F : 0.62831855F;
        }
    }

    public void handleEntityEvent(byte status) {
        if (status == 8) {
            this.canShakeWaterOff = true;
            this.shakeProgress = 0.0F;
            this.lastShakeProgress = 0.0F;
        } else if (status == 56) {
            this.resetShake();
        } else {
            super.handleEntityEvent(status);
        }
    }

    public boolean hasBeenHurt() {
        return hurtTime > 0;
    }

    public boolean hasJustStretched() {
        return justStretched;
    }

    public boolean hasMouthOpened() {
        return this.entityData.get(MOUTH_OPENED);
    }

    public boolean hasStackInMouth() {
        return getStackInMouth() != null && getStackInMouth().getItem() != Items.AIR;
    }

    public boolean hasStackInMouth(ItemStack stack) {
        return hasStackInMouth(stack.getItem());
    }

    public boolean hasStackInMouth(Item item) {
        return hasStackInMouth() && getStackInMouth().getItem() == item;
    }

    public boolean hasStackInMouth(TagKey<Item> tag) {
        return hasStackInMouth() && getStackInMouth().is(tag);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (this.level().isClientSide) {
            boolean bl = this.isOwnedBy(player) || this.isTame() || itemStack.is(Items.BONE) && !this.isTame() && !this.isAngry();
            return bl ? InteractionResult.CONSUME : InteractionResult.PASS;
        }

        if (!this.isTame()) {
            return super.mobInteract(player, hand);
        }

        if (this.interactMobCollar(player, itemStack)) {
            return InteractionResult.SUCCESS;
        }

        if (this.isBaby()) {
            if (this.interactMobHealBreedingItem(player, itemStack)) {
                return InteractionResult.SUCCESS;
            }

            return interactMobOther(player, hand);
        }

        if (player.isShiftKeyDown()) {
            return interactMobOther(player, hand);
        }

        if (this.interactMobGiveBreedingItem(itemStack)) {
            return InteractionResult.SUCCESS;
        }

        this.interactMobSit();
        return InteractionResult.SUCCESS;
    }

    private boolean interactMobCollar(Player player, ItemStack itemStackInHand) {
        Item item = itemStackInHand.getItem();

        if (!(item instanceof DyeItem)) {
            return false;
        }

        DyeColor dyeColor = ((DyeItem)item).getDyeColor();

        if (dyeColor == this.getCollarColor()) {
            return false;
        }

        this.setCollarColor(dyeColor);
        PlayerUtils.decrementItemStackIfNotCreative(player, itemStackInHand);

        return true;
    }

    private boolean interactMobGiveBreedingItem(ItemStack itemStackInHand) {
        if (!isAction(DoggoAction.NEUTRAL)) {
            return false;
        }

        if (this.hasStackInMouth()) {
            return false;
        }

        if(!this.isFood(itemStackInHand)) {
            return false;
        }

        this.setStackInMouth(itemStackInHand.split(1));
        return true;
    }

    private boolean interactMobHealBreedingItem(Player player, ItemStack itemStackInHand) {
        Item item = itemStackInHand.getItem();

        if (!isAction(DoggoAction.NEUTRAL)) {
            return false;
        }

        if (this.hasStackInMouth()) {
            return false;
        }

        if(!this.isFood(itemStackInHand)) {
            return false;
        }

        if(this.getHealth() >= this.getMaxHealth()) {
            return false;
        }

        this.heal((float)item.getFoodProperties().getNutrition());
        PlayerUtils.decrementItemStackIfNotCreative(player, itemStackInHand);

        return true;
    }

    private InteractionResult interactMobOther(Player player, InteractionHand hand) {
        InteractionResult actionResult = super.mobInteract(player, hand);

        if (!this.isOwnedBy(player)) {
            return actionResult;
        }

        if (actionResult.consumesAction()) {
            return actionResult;
        }

        this.interactMobSit();
        return InteractionResult.SUCCESS;
    }

    private void interactMobSit() {
        this.setOrderedToSit(!this.isOrderedToSit());
        this.jumping = false;
        this.navigation.stop();
        this.setTarget(null);
    }

    public boolean isBreedingItem(Item item) {
        return item.isEdible() && item.getFoodProperties().isMeat();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return isBreedingItem(stack.getItem());
    }

    public boolean isFurWet() {
        return this.furWet;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ACTION, DoggoAction.NEUTRAL);
        this.entityData.define(ACTION_TICKING, false);
        this.entityData.define(ANGER_TIME, 0);
        this.entityData.define(BEGGING, false);
        this.entityData.define(BOWL_POS, new BlockPos(0, 0, 0));
        this.entityData.define(COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.define(FEELING, DoggoFeeling.NEUTRAL);
        this.entityData.define(IN_LIE_DOWN_POSE, false);
        this.entityData.define(MOUTH_OPENED, false);
        this.entityData.define(MOUTH_STACK, new ItemStack(Items.AIR));
        this.entityData.define(SCRATCHING_SIDE, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalDataMap = new HashMap<>();
        this.goalDataMap.put(DoggoAction.LISTENING, new DoggoGoalData(this.getRandom(), 400, 40));
        this.goalDataMap.put(DoggoAction.PLAY_IN_SNOW, new DoggoGoalData(this.getRandom(), 200, 20));
        this.goalDataMap.put(DoggoAction.SCRATCHING, new DoggoGoalData(this.getRandom(), 200, 20));
        this.goalDataMap.put(DoggoAction.SNIFFING, new DoggoGoalData(this.getRandom(), 200, 20));
        this.goalDataMap.put(DoggoAction.STRETCHING, new DoggoGoalData(this.getRandom(), 800, 80));

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new DoggoEscapeDangerGoal(this, 1.5D));
        this.goalSelector.addGoal(1, new DoggoAvoidLlamaGoal<>(this, Llama.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(2, new DoggoNapGoal(this));
        this.goalSelector.addGoal(3, new DoggoEatGoal(this));
        this.goalSelector.addGoal(3, new DoggoLieDownGoal(this));
        this.goalSelector.addGoal(3, new DoggoListeningGoal(this));
        this.goalSelector.addGoal(3, new DoggoScratchGoal(this));
        this.goalSelector.addGoal(4, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new DoggoEatFromBowlGoal(this));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new DoggoBegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new DoggoPlayTennisBallGoal(this));
        this.goalSelector.addGoal(11, new DoggoReadyPlayTimeGoal(this));
        this.goalSelector.addGoal(12, new DoggoPlayInSnowGoal(this));
        this.goalSelector.addGoal(12, new DoggoSniffingGoal(this));
        this.goalSelector.addGoal(12, new DoggoStretchGoal(this));
        this.goalSelector.addGoal(13, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(14, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(15, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(15, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        //this.targetSelector.add(4, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        //this.targetSelector.add(5, new UntamedActiveTargetGoal(this, AnimalEntity.class, false, FOLLOW_TAMED_PREDICATE));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public boolean isAction(DoggoAction doggoAction) {
        return getAction() == doggoAction;
    }

    public boolean isActionTicking() {
        return this.entityData.get(ACTION_TICKING);
    }

    public boolean isBegging() {
        return this.entityData.get(BEGGING);
    }

    public boolean isEntityClose(Entity entity) {
        return isEntityClose(entity, DEFAULT_DISTANCE_FROM_DOGGO);
    }

    public boolean isEntityClose(Entity entity, double distance) {
        return !entity.isSpectator() && this.distanceToSqr(entity) < distance;
    }

    public boolean isInLieDownPose() {
        return this.entityData.get(IN_LIE_DOWN_POSE);
    }

    public boolean isOwnerClose() {
        return isOwnerClose(DEFAULT_DISTANCE_FROM_DOGGO);
    }

    public boolean isOwnerClose(double distance) {
        LivingEntity livingEntity = this.getOwner();

        if(livingEntity == null) {
            return false;
        }

        return isEntityClose(livingEntity, distance);
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    public boolean isStackInMouthMeat() {
        return hasStackInMouth() && getStackInMouth().isEdible() && getStackInMouth().getItem().getFoodProperties().isMeat();
    }

    public void die(DamageSource damageSource) {
        this.furWet = false;
        this.canShakeWaterOff = false;
        this.lastShakeProgress = 0.0F;
        this.shakeProgress = 0.0F;
        super.die(damageSource);
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(nbt.getInt("CollarColor")));
        }

        this.readPersistentAngerSaveData(this.level(), nbt);
    }

    private void resetShake() {
        this.canShakeWaterOff = false;
        this.shakeProgress = 0.0F;
        this.lastShakeProgress = 0.0F;
    }

    public void setAction(DoggoAction doggoAction) {
        this.entityData.set(ACTION, doggoAction);
    }

    public void setActionTicking(boolean actionTicking) {
        this.entityData.set(ACTION_TICKING, actionTicking);

        if(!actionTicking) {
            this.animationTick = 0;
        }
    }

    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.entityData.set(ANGER_TIME, angerTime);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    public void setBegging(boolean begging) {
        this.entityData.set(BEGGING, begging);
    }

    public void setBowlPos(BlockPos bowlPos) {
        this.entityData.set(BOWL_POS, bowlPos);
    }

    public void setCollarColor(DyeColor color) {
        this.entityData.set(COLLAR_COLOR, color.getId());
    }

    public void setFeeling(DoggoFeeling doggoFeeling) {
        this.entityData.set(FEELING, doggoFeeling);
    }

    public void setInLieDownPose(boolean inLieDownPose) {
        this.entityData.set(IN_LIE_DOWN_POSE, inLieDownPose);
    }

    public void setJustStretched(boolean justStretched) {
        this.justStretched = justStretched;
    }

    public void setMouthOpened(boolean mouthOpened) {
        this.entityData.set(MOUTH_OPENED, mouthOpened);
    }

    public void setReadyToPlay(boolean readyToPlay) {
        this.readyToPlay = readyToPlay;
    }

    public void setScratchingSide(int scratchingSide) {
        this.entityData.set(SCRATCHING_SIDE, scratchingSide);
    }

    public void setStackInMouth(ItemStack itemStack) {
        if(itemStack == null) {
            itemStack = new ItemStack(Items.AIR);
        }

        this.entityData.set(MOUTH_STACK, itemStack);
    }

    public boolean shouldGoalStart() {
        if(!this.isTame()) {
            return false;
        }

        if (this.isBaby()) {
            return false;
        }

        if (this.isAngry()) {
            return false;
        }

        if (this.hasBeenHurt()) {
           return false;
        }

        if (this.isFurWet()) {
            return false;
        }

        if (this.isInLove()) {
            return false;
        }

        if(!this.onGround()) {
            return false;
        }

        if(this.isInWaterOrBubble()) {
            return false;
        }

        LivingEntity livingEntity = this.getOwner();

        if(livingEntity == null) {
            return false;
        }

        if(livingEntity.getLastHurtByMob() != null) {
            return false;
        }

        if(!this.isEntityClose(livingEntity, DoggoEntity.MAX_DISTANCE_FROM_DOGGO)) {
            return false;
        }

        return true;
    }

    public boolean shouldGoalStop() {
        if(this.isAngry()) {
            return true;
        }

        if (this.hasBeenHurt()) {
            return false;
        }

        if (this.isFurWet()) {
            return false;
        }

        if (this.isInLove()) {
            return true;
        }

        if(!this.onGround()) {
            return true;
        }

        if(this.isInWaterOrBubble()) {
            return true;
        }

        LivingEntity livingEntity = this.getOwner();

        if(livingEntity == null) {
            return true;
        }

        if(livingEntity.getLastHurtByMob() != null) {
            return true;
        }

        if(!this.isEntityClose(livingEntity, DoggoEntity.MAX_DISTANCE_FROM_DOGGO)) {
            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAlive()) {
            this.lastBegAnimationProgress = this.begAnimationProgress;
            if (this.isBegging()) {
                this.begAnimationProgress += (1.0F - this.begAnimationProgress) * 0.4F;
            } else {
                this.begAnimationProgress += (0.0F - this.begAnimationProgress) * 0.4F;
            }

            if(this.getAction() == DoggoAction.NEUTRAL) {
                if (this.isInWaterRainOrBubble()) {
                    this.furWet = true;
                    if (this.canShakeWaterOff && !this.level().isClientSide) {
                        this.level().broadcastEntityEvent(this, (byte)56);
                        this.resetShake();
                    }
                } else if (this.furWet || this.canShakeWaterOff) {
                    if (this.shakeProgress == 0.0F) {
                        this.playSound(SoundEvents.WOLF_SHAKE, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                        this.gameEvent(GameEvent.ENTITY_ACTION);
                    }

                    this.lastShakeProgress = this.shakeProgress;
                    this.shakeProgress += 0.05F;
                    if (this.lastShakeProgress >= 2.0F) {
                        this.furWet = false;
                        this.canShakeWaterOff = false;
                        this.lastShakeProgress = 0.0F;
                        this.shakeProgress = 0.0F;
                    }

                    if (this.shakeProgress > 0.4F) {
                        float f = (float)this.getY();
                        int i = (int)(Mth.sin((this.shakeProgress - 0.4F) * 3.1415927F) * 7.0F);
                        Vec3 vec3d = this.getDeltaMovement();

                        for(int j = 0; j < i; ++j) {
                            float g = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                            float h = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                            this.level().addParticle(ParticleTypes.SPLASH, this.getX() + (double)g, (double)(f + 0.8F), this.getZ() + (double)h, vec3d.x, vec3d.y, vec3d.z);
                        }
                    }
                }
            }

            if (!this.level().isClientSide) {
                for(DoggoGoalData data : this.goalDataMap.values()) {
                    data.tick();
                }
            }
        }

        if(this.level().isClientSide) {
            if(isAction(DoggoAction.NEUTRAL)) {
                animation = 0;
            } else {
                animation++;
            }

            if(isActionTicking()) {
                this.animationTick = (this.animationTick + 0.5f) % 360;
            } else if(this.animationTick > 0) {
                this.animationTick = 0f;
            }

            double x = Mth.sin(-this.yHeadRot % 360 / 58) * 0.5f;
            double z = Mth.cos(this.yHeadRot % 360 / 58) * 0.5f;

            switch(getAction()) {
                case DIGGING:
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.level().getBlockState(this.blockPosition().below())), this.getX() + x, this.getY(), this.getZ() + z, 0.1, 0.1, 0.1);
                    break;
                case EATING:
                    this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, getStackInMouth()), this.getX() + x * (1.6 + ((double) this.random.nextInt(5) / 10)), this.getY() + 0.55, this.getZ() + z * (1.6 + ((double) this.random.nextInt(5) / 10)), 0, 0, 0);
                    break;
                case EATING_FROM_BOWL:
                    if(getBowlPos() != null && this.level().getBlockEntity(getBowlPos()) != null) {
                        ItemStack item = ((DogBowlEntity) this.level().getBlockEntity(getBowlPos())).getItem(0);

                        if(item != null && !item.isEmpty()) {
                            this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, item), getBowlPos().getX() + 0.4 + ((double) this.random.nextInt(3) / 10), getBowlPos().getY() + 0.2, getBowlPos().getZ() + 0.4 + ((double) this.random.nextInt(3) / 10), 0, 0, 0);
                        }
                    }
                    break;
                case PLAY_IN_SNOW:
                    if (this.animation % 45 == 32) {
                        for (int i = 0; i < 10; i++) {
                            this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SNOW_BLOCK.defaultBlockState()), this.getX() + x, this.getY(), this.getZ() + z, 0.2, 0.1, 0.2);
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            if(hasMouthOpened()) {
                this.mouthOpenedTick++;

                if(this.mouthOpenedTick > 20) {
                    this.setMouthOpened(false);
                    this.mouthOpenedTick = 0;
                }
            }

            switch(getAction()) {
                case DIGGING:
                    if(this.tickCount % 4 == 0) {
                        Block block = this.level().getBlockState(this.blockPosition().below()).getBlock();
                        SoundEvent sound = null;

                        if(block == Blocks.GRASS_BLOCK || block == Blocks.DIRT) {
                            sound = SoundEvents.GRASS_HIT;
                        } else if(block == Blocks.SAND || block == Blocks.RED_SAND) {
                            sound = SoundEvents.SAND_HIT;
                        } else if(block == Blocks.GRAVEL) {
                            sound = SoundEvents.GRAVEL_HIT;
                        } else if(block == Blocks.SNOW_BLOCK) {
                            sound = SoundEvents.SNOW_HIT;
                        }

                        if(sound != null) {
                            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, 0.2f, 0f);
                        }
                    }
                    break;
                case PLAY_IN_SNOW:
                    if (this.animation % 45 == 32) {
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 0.5f, 0f);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.furWet && !this.canShakeWaterOff && !this.isPathFinding() && this.onGround()) {
            this.canShakeWaterOff = true;
            this.shakeProgress = 0.0F;
            this.lastShakeProgress = 0.0F;
            this.level().broadcastEntityEvent(this, (byte)8);
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }

    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putByte("CollarColor", (byte)this.getCollarColor().getId());
        this.addPersistentAngerSaveData(nbt);
    }

    static {
        ACTION = SynchedEntityData.defineId(DoggoEntity.class, TrackedDoggoData.DOGGO_ACTION);
        ACTION_TICKING = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.BOOLEAN);
        ANGER_TIME = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.INT);
        BEGGING = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.BOOLEAN);
        BOWL_POS = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.BLOCK_POS);
        COLLAR_COLOR = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.INT);
        FEELING = SynchedEntityData.defineId(DoggoEntity.class, TrackedDoggoData.DOGGO_FEELING);
        IN_LIE_DOWN_POSE = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.BOOLEAN);
        MOUTH_OPENED = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.BOOLEAN);
        MOUTH_STACK = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.ITEM_STACK);
        SCRATCHING_SIDE = SynchedEntityData.defineId(DoggoEntity.class, EntityDataSerializers.INT);

        PICKABLE_DROP_FILTER = (itemEntity) -> !itemEntity.hasPickUpDelay() && itemEntity.isAlive();
        FOLLOWABLE_DROP_FILTER = (entity) -> entity.isAlive();
        ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    }
}
