package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.block.DogBed;
import net.lordmrk.dmo.block.DogBowl;
import net.lordmrk.dmo.item.TennisBall;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import java.util.AbstractMap;

/**
 * Generates the mod's crafting recipes and their unlock advancements.
 */
public class DoggoRecipeProvider extends FabricRecipeProvider {

    private static final DyeColor[] TENNIS_BALL_COLORS = {
            DyeColor.RED, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE
    };

    public DoggoRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        generateDogBedRecipes(exporter);
        generateDogBowlRecipes(exporter);
        generateTennisBallRecipes(exporter);
    }

    private void generateDogBedRecipes(RecipeOutput exporter) {
        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            DogBed bed = (DogBed) entry.getValue();
            DyeColor color = bed.getColor();
            String wood = bed.getWoodType().name();

            Item slab = BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft", wood + "_slab"));
            Item wool = BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft", color.getName() + "_wool"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, bed)
                    .pattern("WW")
                    .pattern("PP")
                    .define('W', wool)
                    .define('P', slab)
                    .unlockedBy(RecipeProvider.getHasName(wool), RecipeProvider.has(wool))
                    .save(exporter, modId(entry.getKey()));
        }
    }

    private void generateDogBowlRecipes(RecipeOutput exporter) {
        TagKey<Item> dogBowlTag = TagKey.create(Registries.ITEM, modId("dog_bowl"));

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            DogBowl bowl = (DogBowl) entry.getValue();
            DyeColor color = bowl.getColor();
            Item dye = dye(color);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, bowl)
                    .pattern("   ")
                    .pattern("ICI")
                    .pattern(" I ")
                    .define('I', Items.IRON_INGOT)
                    .define('C', dye)
                    .unlockedBy(RecipeProvider.getHasName(Items.IRON_INGOT), RecipeProvider.has(Items.IRON_INGOT))
                    .save(exporter, modId(entry.getKey()));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, bowl)
                    .requires(dogBowlTag)
                    .requires(dye)
                    .unlockedBy(RecipeProvider.getHasName(dye), RecipeProvider.has(dye))
                    .save(exporter, modId("any_dog_bowl_" + color.getName()));
        }
    }

    private void generateTennisBallRecipes(RecipeOutput exporter) {
        TagKey<Item> tennisBallTag = TagKey.create(Registries.ITEM, modId("tennis_ball"));

        for (DyeColor color : TENNIS_BALL_COLORS) {
            TennisBall ball = DoggoItems.getTennisBall(color);
            Item dye = dye(color);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ball)
                    .pattern("SSS")
                    .pattern("SDS")
                    .pattern("SSS")
                    .define('S', Items.STRING)
                    .define('D', dye)
                    .unlockedBy(RecipeProvider.getHasName(Items.STRING), RecipeProvider.has(Items.STRING))
                    .save(exporter, modId("tennis_ball_" + color.getName()));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ball)
                    .requires(tennisBallTag)
                    .requires(dye)
                    .unlockedBy(RecipeProvider.getHasName(dye), RecipeProvider.has(dye))
                    .save(exporter, modId("any_tennis_ball_" + color.getName()));
        }

        // Legacy tennis ball conversion: tennis_ball -> tennis_ball_lime
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DoggoItems.getTennisBall(DyeColor.LIME))
                .requires(DoggoItems.TENNIS_BALL_LEGACY)
                .unlockedBy(RecipeProvider.getHasName(DoggoItems.TENNIS_BALL_LEGACY), RecipeProvider.has(DoggoItems.TENNIS_BALL_LEGACY))
                .save(exporter, modId("tennis_ball_legacy"));
    }

    private static Item dye(DyeColor color) {
        return BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft", color.getName() + "_dye"));
    }

    private static ResourceLocation modId(String path) {
        return new ResourceLocation(DoggoModOverhauled.MODID, path);
    }
}
