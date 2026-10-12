package net.lordmrk.dmo.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.DoggoModOverhauledClient;
import net.lordmrk.dmo.client.render.entity.feature.DoggoCollarFeatureRenderer;
import net.lordmrk.dmo.client.render.entity.feature.DoggoHeldItemFeatureRenderer;
import net.lordmrk.dmo.client.render.entity.model.*;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.HashMap;
import java.util.Map;

public class DoggoEntityRenderer extends MobRenderer<DoggoEntity, DoggoRenderState, DoggoEntityModel> {

    private static final Identifier WILD_TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo.png");
    private static final Identifier TAMED_TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_tame.png");
    private static final Identifier ANGRY_TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_angry.png");
    private static final Identifier SLEEPING_TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_sleeping.png");

    private final ItemModelResolver itemModelResolver;
    private final Map<DoggoAction, DoggoEntityModel> doggoModels = new HashMap<>();
    private final DoggoEntityModel baseModel;

    public DoggoEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DoggoEntityModel(context.bakeLayer(DoggoModOverhauledClient.DOGGO)), 0.5F);
        this.itemModelResolver = context.getItemModelResolver();
        this.baseModel = this.model;

        this.addLayer(new DoggoCollarFeatureRenderer(this));
        this.addLayer(new DoggoHeldItemFeatureRenderer(this));

        this.doggoModels.put(DoggoAction.DIGGING, new DoggoModelDigging(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.EATING, new DoggoModelNeutral(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.EATING_FROM_BOWL, new DoggoModelEatingFromBowl(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.LISTENING, new DoggoModelListening(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.NAPPING, new DoggoModelNapping(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.NEUTRAL, new DoggoModelNeutral(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.PLAY_IN_SNOW, new DoggoModelPlayInSnow(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.PLAY_TIME, new DoggoModelReadyPlayTime(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.SCRATCHING, new DoggoModelScratching(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.SNIFFING, new DoggoModelSniffing(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
        this.doggoModels.put(DoggoAction.STRETCHING, new DoggoModelStretching(context.bakeLayer(DoggoModOverhauledClient.DOGGO)));
    }

    @Override
    public DoggoRenderState createRenderState() {
        return new DoggoRenderState();
    }

    @Override
    public void extractRenderState(DoggoEntity entity, DoggoRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        state.action = entity.getAction();
        state.feeling = entity.getFeeling();
        state.hasMouthOpened = entity.hasMouthOpened();
        state.hasStackInMouth = entity.hasStackInMouth();
        state.inSittingPose = entity.isInSittingPose();
        state.inLieDownPose = entity.isInLieDownPose();
        state.furWet = entity.isFurWet();
        state.angry = entity.isAngry();
        state.tamed = entity.isTame();
        state.animationTick = entity.getAnimationTick();
        state.animation = entity.getAnimation();
        state.scratchingSide = entity.getScratchingSide();
        state.tailAngle = entity.getTailAngle();
        state.begAnimationProgress = entity.getBegAnimationProgress(partialTicks);
        state.shakeProgress = entity.getShakeProgress(partialTicks);
        state.collarColor = entity.isTame() ? entity.getCollarColor() : null;
        state.texture = this.getTexture(entity);

        this.itemModelResolver.updateForLiving(state.heldItem, entity.getStackInMouth(), ItemDisplayContext.GROUND, entity);
    }

    @Override
    public void submit(DoggoRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        this.model = this.doggoModels.getOrDefault(state.action, this.baseModel);
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    protected int getModelTint(DoggoRenderState state) {
        if (state.furWet) {
            float h = Math.min(0.5F + state.shakeProgress / 2.0F * 0.5F, 1.0F);
            return ARGB.colorFromFloat(1.0F, h, h, h);
        }

        return -1;
    }

    @Override
    public Identifier getTextureLocation(DoggoRenderState state) {
        return state.texture;
    }

    private Identifier getTexture(DoggoEntity entity) {
        if (entity.isTame()) {
            if (entity.isAction(DoggoAction.NAPPING) ||
                    entity.isAction(DoggoAction.STRETCHING) && entity.getAnimationTick() > 60 && entity.getAnimationTick() < 130) {
                return SLEEPING_TEXTURE;
            }

            return TAMED_TEXTURE;
        } else {
            return entity.isAngry() ? ANGRY_TEXTURE : WILD_TEXTURE;
        }
    }
}
