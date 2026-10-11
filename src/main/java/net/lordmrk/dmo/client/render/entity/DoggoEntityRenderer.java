package net.lordmrk.dmo.client.render.entity;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.DoggoModOverhauledClient;
import net.lordmrk.dmo.client.render.entity.feature.DoggoCollarFeatureRenderer;
import net.lordmrk.dmo.client.render.entity.feature.DoggoHeldItemFeatureRenderer;
import net.lordmrk.dmo.client.render.entity.model.*;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Map;

public class DoggoEntityRenderer extends MobRenderer<DoggoEntity, DoggoEntityModel<DoggoEntity>> {

    private static final Identifier WILD_TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo.png");
    private static final Identifier TAMED_TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_tame.png");
    private static final Identifier ANGRY_TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_angry.png");
    private static final Identifier SLEEPING_TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_sleeping.png");

    private final Map<DoggoAction, DoggoEntityModel<DoggoEntity>> doggoModels = new HashMap<>();

    public DoggoEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DoggoEntityModel<>(context.bakeLayer(DoggoModOverhauledClient.DOGGO)), 0.5F);
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

    protected float getBob(DoggoEntity doggoEntity, float f) {
        return doggoEntity.getTailAngle();
    }

    public void render(DoggoEntity doggoEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        this.model = this.doggoModels.get(doggoEntity.getAction());

        if (doggoEntity.isFurWet()) {
            float h = doggoEntity.getFurWetBrightnessMultiplier(g);
            this.model.setColor(h, h, h);
        }

        super.render(doggoEntity, f, g, matrixStack, vertexConsumerProvider, i);
        if (doggoEntity.isFurWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }

    }

    public Identifier getTextureLocation(DoggoEntity doggoEntity) {
        if (doggoEntity.isTame()) {
            if (doggoEntity.isAction(DoggoAction.NAPPING) ||
                    doggoEntity.isAction(DoggoAction.STRETCHING) && doggoEntity.getAnimationTick() > 60 && doggoEntity.getAnimationTick() < 130) {
                return SLEEPING_TEXTURE;
            }

            return TAMED_TEXTURE;
        } else {
            return doggoEntity.isAngry() ? ANGRY_TEXTURE : WILD_TEXTURE;
        }
    }
}
