package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.block.DogBed;
import net.lordmrk.dmo.block.DogBowl;
import net.lordmrk.dmo.item.TennisBall;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the mod's crafting recipes and their unlock advancements.
 */
public class DoggoRecipeProvider extends FabricRecipeProvider {

    public DoggoRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new DoggoRecipeGenerator(recipes, advancements);
    }

    private static final class DoggoRecipeGenerator extends RecipeProvider {

        private static final DyeColor[] TENNIS_BALL_COLORS = {
                DyeColor.RED, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE
        };

        DoggoRecipeGenerator(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
            super(recipes, advancements);
        }

        @Override
        public void buildRecipes() {
            generateDogBedRecipes();
            generateDogBowlRecipes();
            generateTennisBallRecipes();
        }

        private void generateDogBedRecipes() {
            for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
                DogBed bed = (DogBed) entry.getValue();
                DyeColor color = bed.getColor();
                String wood = bed.getWoodType().name();

                Item slab = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("minecraft", wood + "_slab"));
                Item wool = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("minecraft", color.getName() + "_wool"));

                shaped(RecipeCategory.MISC, bed)
                        .pattern("WW")
                        .pattern("PP")
                        .define('W', wool)
                        .define('P', slab)
                        .unlockedBy(getHasName(wool), has(wool))
                        .save(output, recipeKey(entry.getKey()));
            }
        }

        private void generateDogBowlRecipes() {
            TagKey<Item> dogBowlTag = TagKey.create(Registries.ITEM, modId("dog_bowl"));

            for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
                DogBowl bowl = (DogBowl) entry.getValue();
                DyeColor color = bowl.getColor();
                Item dye = dye(color);

                shaped(RecipeCategory.MISC, bowl)
                        .pattern("   ")
                        .pattern("ICI")
                        .pattern(" I ")
                        .define('I', Items.IRON_INGOT)
                        .define('C', dye)
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                        .save(output, recipeKey(entry.getKey()));

                shapeless(RecipeCategory.MISC, bowl)
                        .requires(dogBowlTag)
                        .requires(dye)
                        .unlockedBy(getHasName(dye), has(dye))
                        .save(output, recipeKey("any_dog_bowl_" + color.getName()));
            }
        }

        private void generateTennisBallRecipes() {
            TagKey<Item> tennisBallTag = TagKey.create(Registries.ITEM, modId("tennis_ball"));

            for (DyeColor color : TENNIS_BALL_COLORS) {
                TennisBall ball = DoggoItems.getTennisBall(color);
                Item dye = dye(color);

                shaped(RecipeCategory.MISC, ball)
                        .pattern("SSS")
                        .pattern("SDS")
                        .pattern("SSS")
                        .define('S', Items.STRING)
                        .define('D', dye)
                        .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                        .save(output, recipeKey("tennis_ball_" + color.getName()));

                shapeless(RecipeCategory.MISC, ball)
                        .requires(tennisBallTag)
                        .requires(dye)
                        .unlockedBy(getHasName(dye), has(dye))
                        .save(output, recipeKey("any_tennis_ball_" + color.getName()));
            }

            // Legacy tennis ball conversion: tennis_ball -> tennis_ball_lime
            shapeless(RecipeCategory.MISC, DoggoItems.getTennisBall(DyeColor.LIME))
                    .requires(DoggoItems.TENNIS_BALL_LEGACY)
                    .unlockedBy(getHasName(DoggoItems.TENNIS_BALL_LEGACY), has(DoggoItems.TENNIS_BALL_LEGACY))
                    .save(output, recipeKey("tennis_ball_legacy"));
        }
    }

    private static Item dye(DyeColor color) {
        return BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("minecraft", color.getName() + "_dye"));
    }

    private static Identifier modId(String path) {
        return Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, path);
    }

    private static ResourceKey<Recipe<?>> recipeKey(String path) {
        return ResourceKey.create(Registries.RECIPE, modId(path));
    }
}
