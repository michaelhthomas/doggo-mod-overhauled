package net.lordmrk.dmo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lordmrk.dmo.client.gui.screen.ingame.DogBowlScreen;
import net.lordmrk.dmo.client.render.block.entity.DogBowlBlockEntityRenderer;
import net.lordmrk.dmo.client.render.entity.DoggoEntityRenderer;
import net.lordmrk.dmo.client.render.entity.model.DoggoEntityModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public class DoggoModOverhauledClient implements ClientModInitializer {

    public static final ModelLayerLocation DOGGO = new ModelLayerLocation(new ResourceLocation(DoggoModOverhauled.MODID, "doggo"), "main");
    public static final ResourceLocation PacketID = new ResourceLocation(DoggoModOverhauled.MODID, "spawn_packet");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(DoggoEntities.DOGGO_ENTITY, DoggoEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(DOGGO, DoggoEntityModel::getTexturedModelData);

        BlockEntityRenderers.register(DoggoModOverhauled.DOG_BOWL_ENTITY, DogBowlBlockEntityRenderer::new);
        MenuScreens.register(DoggoModOverhauled.DOG_BOWL_SCREEN_HANDLER, DogBowlScreen::new);

        EntityRendererRegistry.register(DoggoEntities.TENNIS_BALL_RED_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(DoggoEntities.TENNIS_BALL_YELLOW_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(DoggoEntities.TENNIS_BALL_LIME_ENTITY, ThrownItemRenderer::new);
        EntityRendererRegistry.register(DoggoEntities.TENNIS_BALL_LIGHT_BLUE_ENTITY, ThrownItemRenderer::new);

        receiveEntityPacket();
    }

    private void receiveEntityPacket() {
        ClientPlayNetworking.registerGlobalReceiver(PacketID, (client, handler, byteBuf, sender) -> {
            EntityType<?> et = BuiltInRegistries.ENTITY_TYPE.byId(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUUID();
            int entityId = byteBuf.readVarInt();
            Vec3 pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);

            if (Minecraft.getInstance().level == null)
                throw new IllegalStateException("Tried to spawn entity in a null world!");
            Entity entity = et.create(Minecraft.getInstance().level);
            if (entity == null)
                throw new IllegalStateException("Failed to create instance of entity \"" + BuiltInRegistries.ENTITY_TYPE.getKey(et) + "\"!");
            entity.syncPacketPositionCodec(pos.x, pos.y, pos.z);
            entity.setPosRaw(pos.x, pos.y, pos.z);
            entity.setXRot(pitch);
            entity.setYRot(yaw);
            entity.setId(entityId);
            entity.setUUID(uuid);
            Minecraft.getInstance().level.addEntity(entity);
        });
    }
}
