package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the mod's item tags.
 */
public class DoggoItemTagProvider extends FabricTagProvider.ItemTagProvider {

    private static final DyeColor[] TENNIS_BALL_COLORS = {
            DyeColor.RED, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE
    };

    public DoggoItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, BlockTagProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        FabricTagBuilder dogBedTag = getOrCreateTagBuilder(tag("dog_bed"));
        FabricTagBuilder dogBowlTag = getOrCreateTagBuilder(tag("dog_bowl"));
        FabricTagBuilder tennisBallTag = getOrCreateTagBuilder(tag("tennis_ball"));

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            dogBedTag.add(entry.getValue().asItem());
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            dogBowlTag.add(entry.getValue().asItem());
        }

        for (DyeColor color : TENNIS_BALL_COLORS) {
            tennisBallTag.add(DoggoItems.getTennisBall(color));
        }
    }

    private static TagKey<Item> tag(String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(DoggoModOverhauled.MODID, path));
    }
}
