package net.lordmrk.dmo;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.lordmrk.dmo.entity.projectile.thrown.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

public class DoggoEntities {

    public static EntityType<RedTennisBallEntity> TENNIS_BALL_RED_ENTITY;
    public static EntityType<YellowTennisBallEntity> TENNIS_BALL_YELLOW_ENTITY;
    public static EntityType<LimeTennisBallEntity> TENNIS_BALL_LIME_ENTITY;
    public static EntityType<LightBlueTennisBallEntity> TENNIS_BALL_LIGHT_BLUE_ENTITY;

    public static EntityType<DoggoEntity> DOGGO_ENTITY;

    public static AbstractTennisBallEntity newTennisBall(Level world, Player user, DyeColor color) {
        switch(color) {
            case RED:
                return new RedTennisBallEntity(world, user);
            case YELLOW:
                return new YellowTennisBallEntity(world, user);
            case LIME:
                return new LimeTennisBallEntity(world, user);
            case LIGHT_BLUE:
                return new LightBlueTennisBallEntity(world, user);
            default:
                throw new IllegalArgumentException("No tennis ball entity found for color \"" + color.getName() + "\"");
        }
    }

    public static void registerAll() {
        DOGGO_ENTITY = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                key("doggo"),
                EntityType.Builder.of(DoggoEntity::new, MobCategory.CREATURE)
                        .sized(0.6F, 0.85F)
                        .build(key("doggo")));

        TENNIS_BALL_RED_ENTITY = registerTennisBall(DyeColor.RED, RedTennisBallEntity::new);
        TENNIS_BALL_YELLOW_ENTITY = registerTennisBall(DyeColor.YELLOW, YellowTennisBallEntity::new);
        TENNIS_BALL_LIME_ENTITY = registerTennisBall(DyeColor.LIME, LimeTennisBallEntity::new);
        TENNIS_BALL_LIGHT_BLUE_ENTITY = registerTennisBall(DyeColor.LIGHT_BLUE, LightBlueTennisBallEntity::new);

        FabricDefaultAttributeRegistry.register(DOGGO_ENTITY, DoggoEntity.createDoggoAttributes());
    }

    private static <T extends AbstractTennisBallEntity> EntityType<T> registerTennisBall(DyeColor color, EntityType.EntityFactory<T> entityFactory) {
        Identifier id = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "tennis_ball_" + color.getName() + "_entity");

        return Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                id,
                EntityType.Builder.of(entityFactory, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(4).updateInterval(10).build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
    }

    private static ResourceKey<EntityType<?>> key(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, id));
    }
}
