package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the mod's item tags.
 */
public class DoggoItemTagProvider extends FabricTagProvider.ItemTagProvider {

    private static final DyeColor[] TENNIS_BALL_COLORS = {
            DyeColor.RED, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE
    };

    public DoggoItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, BlockTagProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
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
        return TagKey.of(RegistryKeys.ITEM, new Identifier(DoggoModOverhauled.MODID, path));
    }
}
