package net.lordmrk.dmo.client.render.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.client.render.entity.DoggoRenderState;
import net.lordmrk.dmo.client.render.entity.model.DoggoEntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class DoggoMouthLayer extends RenderLayer<DoggoRenderState, DoggoEntityModel> {

    private static final Identifier MOUTH = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_mouth.png");
    private static final Identifier TONGUE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_mouth_tongue.png");

    public DoggoMouthLayer(RenderLayerParent<DoggoRenderState, DoggoEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, DoggoRenderState state, float yRot, float xRot) {
        if (state.isInvisible) {
            return;
        }

        submitNodeCollector.order(1)
                .submitModel(this.getParentModel(), state, poseStack, RenderTypes.entityCutout(MOUTH), lightCoords, OverlayTexture.NO_OVERLAY, state.jawTint, null, state.outlineColor);
        submitNodeCollector.order(1)
                .submitModel(this.getParentModel(), state, poseStack, RenderTypes.entityCutout(TONGUE), lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor);
    }
}
