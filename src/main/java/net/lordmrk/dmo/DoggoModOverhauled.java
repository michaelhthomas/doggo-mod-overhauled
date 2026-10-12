package net.lordmrk.dmo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.lordmrk.dmo.screen.DogBowlScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class DoggoModOverhauled implements ModInitializer {

	public static final String MODID = "doggomodoverhauled";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Identifier JADE_DOG_BOWL = Identifier.fromNamespaceAndPath(MODID, "dog_bowl");
	public static final Identifier DOG_BOWL = Identifier.fromNamespaceAndPath(MODID, "dog_bowl_entity");
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
				new BlockEntityType<>(DogBowlEntity::new, Set.<Block>of(DoggoBlocks.getDogBowls())));

		FabricEntityDataRegistry.register(Identifier.fromNamespaceAndPath(MODID, "doggo_action"), TrackedDoggoData.DOGGO_ACTION);
		FabricEntityDataRegistry.register(Identifier.fromNamespaceAndPath(MODID, "doggo_feeling"), TrackedDoggoData.DOGGO_FEELING);
	}

	static {
		DOG_BOWL_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, DOG_BOWL, new MenuType<>(DogBowlScreenHandler::new, FeatureFlags.VANILLA_SET));
	}
}
