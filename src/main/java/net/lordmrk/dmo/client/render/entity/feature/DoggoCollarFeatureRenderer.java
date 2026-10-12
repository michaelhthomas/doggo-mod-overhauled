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
import net.minecraft.world.item.DyeColor;

public class DoggoCollarFeatureRenderer extends RenderLayer<DoggoRenderState, DoggoEntityModel> {

    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_collar.png");

    public DoggoCollarFeatureRenderer(RenderLayerParent<DoggoRenderState, DoggoEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, DoggoRenderState state, float yRot, float xRot) {
        DyeColor collarColor = state.collarColor;

        if (state.tamed && collarColor != null && !state.isInvisible) {
            int color = collarColor.getTextureDiffuseColor();
            submitNodeCollector.order(1)
                    .submitModel(this.getParentModel(), state, poseStack, RenderTypes.entityCutout(SKIN), lightCoords, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor);
        }
    }
}
