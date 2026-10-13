package net.lordmrk.dmo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.lordmrk.dmo.client.gui.screen.ingame.DogBowlScreen;
import net.lordmrk.dmo.client.render.block.entity.DogBowlBlockEntityRenderer;
import net.lordmrk.dmo.client.render.entity.DoggoEntityRenderer;
import net.lordmrk.dmo.client.render.entity.DoggoVariantTints;
import net.lordmrk.dmo.client.render.entity.model.DoggoEntityModel;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class DoggoModOverhauledClient implements ClientModInitializer {

    public static final ModelLayerLocation DOGGO = new ModelLayerLocation(Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "doggo"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(DoggoEntities.DOGGO_ENTITY, DoggoEntityRenderer::new);
        ModelLayerRegistry.registerModelLayer(DOGGO, DoggoEntityModel::getTexturedModelData);

        BlockEntityRendererRegistry.register(DoggoModOverhauled.DOG_BOWL_ENTITY, DogBowlBlockEntityRenderer::new);
        MenuScreens.register(DoggoModOverhauled.DOG_BOWL_SCREEN_HANDLER, DogBowlScreen::new);

        EntityRendererRegistry.register(DoggoEntities.TENNIS_BALL_RED_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(DoggoEntities.TENNIS_BALL_YELLOW_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(DoggoEntities.TENNIS_BALL_LIME_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(DoggoEntities.TENNIS_BALL_LIGHT_BLUE_ENTITY, ThrownItemRenderer::new);

        // Drop cached fur tints when resource packs reload, so they follow a restyled wolf texture.
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            if (client.getResourceManager() instanceof ReloadableResourceManager manager) {
                ResourceManagerReloadListener listener = resourceManager -> DoggoVariantTints.clearCache();
                manager.registerReloadListener(listener);
            }
        });
    }
}
