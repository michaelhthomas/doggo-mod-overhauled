package net.lordmrk.dmo.client.render.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.client.render.entity.model.DoggoEntityModel;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;

public class DoggoCollarFeatureRenderer extends RenderLayer<DoggoEntity, DoggoEntityModel<DoggoEntity>> {

    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/entity/doggo/doggo_collar.png");

    public DoggoCollarFeatureRenderer(RenderLayerParent<DoggoEntity, DoggoEntityModel<DoggoEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, DoggoEntity doggoEntity, float f, float g, float h, float j, float k, float l) {
        if (doggoEntity.isTame() && !doggoEntity.isInvisible()) {
            float[] fs = doggoEntity.getCollarColor().getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), SKIN, matrixStack, vertexConsumerProvider, i, doggoEntity, fs[0], fs[1], fs[2]);
        }
    }
}
