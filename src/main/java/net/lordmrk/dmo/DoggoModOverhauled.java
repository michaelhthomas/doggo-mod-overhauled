package net.lordmrk.dmo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.lordmrk.dmo.screen.DogBowlScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoggoModOverhauled implements ModInitializer {

	public static final String MODID = "doggomodoverhauled";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final ResourceLocation JADE_DOG_BOWL = new ResourceLocation(MODID, "dog_bowl");
	public static final ResourceLocation DOG_BOWL = new ResourceLocation(MODID, "dog_bowl_entity");
	public static BlockEntityType<DogBowlEntity> DOG_BOWL_ENTITY;
	public static final MenuType<DogBowlScreenHandler> DOG_BOWL_SCREEN_HANDLER;


	@Override
	public void onInitialize() {
		DoggoEntities.registerAll();
		DoggoBlocks.registerAll();
		DoggoItems.registerAll();
		DoggoItemGroups.registerAll();

		DoggoBlocks.addToColoredBlocksItemGroup();
		DoggoBlocks.addToDoggoModItemGroup();
		DoggoBlocks.addToFunctionalBlocksItemGroup();

		DoggoItems.addToToolItemsItemGroup();
		DoggoItems.addToDoggoModItemGroup();

		DOG_BOWL_ENTITY = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				DOG_BOWL,
				FabricBlockEntityTypeBuilder.create(
						DogBowlEntity::new,
						DoggoBlocks.getDogBowls()).build(null));

		EntityDataSerializers.registerSerializer(TrackedDoggoData.DOGGO_ACTION);
		EntityDataSerializers.registerSerializer(TrackedDoggoData.DOGGO_FEELING);
	}

	static {
		DOG_BOWL_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, DOG_BOWL, new MenuType<>(DogBowlScreenHandler::new, FeatureFlags.VANILLA_SET));
	}
}
