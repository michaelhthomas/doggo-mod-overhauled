package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.BlockItemTagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the mod's item tags.
 */
public class DoggoItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    private static final DyeColor[] TENNIS_BALL_COLORS = {
            DyeColor.RED, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE
    };

    public DoggoItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, BlockTagsProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        BlockItemTagAppender<Item> dogBedTag = builder(tag("dog_bed"));
        BlockItemTagAppender<Item> dogBowlTag = builder(tag("dog_bowl"));
        BlockItemTagAppender<Item> tennisBallTag = builder(tag("tennis_ball"));

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            dogBedTag.add(entry.getValue().asItem().builtInRegistryHolder().key());
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            dogBowlTag.add(entry.getValue().asItem().builtInRegistryHolder().key());
        }

        for (DyeColor color : TENNIS_BALL_COLORS) {
            tennisBallTag.add(DoggoItems.getTennisBall(color).builtInRegistryHolder().key());
        }
    }

    private static TagKey<Item> tag(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, path));
    }
}
