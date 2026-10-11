package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.block.DogBed;
import net.lordmrk.dmo.block.DogBowl;
import net.lordmrk.dmo.item.TennisBall;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

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
    public void generate(RecipeExporter exporter) {
        generateDogBedRecipes(exporter);
        generateDogBowlRecipes(exporter);
        generateTennisBallRecipes(exporter);
    }

    private void generateDogBedRecipes(RecipeExporter exporter) {
        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            DogBed bed = (DogBed) entry.getValue();
            DyeColor color = bed.getColor();
            String wood = bed.getWoodType().name();

            Item slab = Registries.ITEM.get(new Identifier("minecraft", wood + "_slab"));
            Item wool = Registries.ITEM.get(new Identifier("minecraft", color.getName() + "_wool"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, bed)
                    .pattern("WW")
                    .pattern("PP")
                    .input('W', wool)
                    .input('P', slab)
                    .criterion(RecipeProvider.hasItem(wool), RecipeProvider.conditionsFromItem(wool))
                    .offerTo(exporter, modId(entry.getKey()));
        }
    }

    private void generateDogBowlRecipes(RecipeExporter exporter) {
        TagKey<Item> dogBowlTag = TagKey.of(RegistryKeys.ITEM, modId("dog_bowl"));

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            DogBowl bowl = (DogBowl) entry.getValue();
            DyeColor color = bowl.getColor();
            Item dye = dye(color);

            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, bowl)
                    .pattern("   ")
                    .pattern("ICI")
                    .pattern(" I ")
                    .input('I', Items.IRON_INGOT)
                    .input('C', dye)
                    .criterion(RecipeProvider.hasItem(Items.IRON_INGOT), RecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                    .offerTo(exporter, modId(entry.getKey()));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, bowl)
                    .input(dogBowlTag)
                    .input(dye)
                    .criterion(RecipeProvider.hasItem(dye), RecipeProvider.conditionsFromItem(dye))
                    .offerTo(exporter, modId("any_dog_bowl_" + color.getName()));
        }
    }

    private void generateTennisBallRecipes(RecipeExporter exporter) {
        TagKey<Item> tennisBallTag = TagKey.of(RegistryKeys.ITEM, modId("tennis_ball"));

        for (DyeColor color : TENNIS_BALL_COLORS) {
            TennisBall ball = DoggoItems.getTennisBall(color);
            Item dye = dye(color);

            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ball)
                    .pattern("SSS")
                    .pattern("SDS")
                    .pattern("SSS")
                    .input('S', Items.STRING)
                    .input('D', dye)
                    .criterion(RecipeProvider.hasItem(Items.STRING), RecipeProvider.conditionsFromItem(Items.STRING))
                    .offerTo(exporter, modId("tennis_ball_" + color.getName()));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ball)
                    .input(tennisBallTag)
                    .input(dye)
                    .criterion(RecipeProvider.hasItem(dye), RecipeProvider.conditionsFromItem(dye))
                    .offerTo(exporter, modId("any_tennis_ball_" + color.getName()));
        }

        // Legacy tennis ball conversion: tennis_ball -> tennis_ball_lime
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, DoggoItems.getTennisBall(DyeColor.LIME))
                .input(DoggoItems.TENNIS_BALL_LEGACY)
                .criterion(RecipeProvider.hasItem(DoggoItems.TENNIS_BALL_LEGACY), RecipeProvider.conditionsFromItem(DoggoItems.TENNIS_BALL_LEGACY))
                .offerTo(exporter, modId("tennis_ball_legacy"));
    }

    private static Item dye(DyeColor color) {
        return Registries.ITEM.get(new Identifier("minecraft", color.getName() + "_dye"));
    }

    private static Identifier modId(String path) {
        return new Identifier(DoggoModOverhauled.MODID, path);
    }
}
