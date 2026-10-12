package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.BlockItemTagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the mod's block tags and the {@code minecraft} block tags used for tool requirements.
 */
public class DoggoBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public DoggoBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        BlockItemTagAppender<Block> dogBedTag = builder(tag("dog_bed"));
        BlockItemTagAppender<Block> dogBowlTag = builder(tag("dog_bowl"));
        BlockItemTagAppender<Block> pickaxeMineable = builder(BlockTags.MINEABLE_WITH_PICKAXE);
        BlockItemTagAppender<Block> needsStoneTool = builder(BlockTags.NEEDS_STONE_TOOL);

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            dogBedTag.add(entry.getValue().builtInRegistryHolder().key());
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            Block bowl = entry.getValue();
            dogBowlTag.add(bowl.builtInRegistryHolder().key());
            pickaxeMineable.add(bowl.builtInRegistryHolder().key());
            needsStoneTool.add(bowl.builtInRegistryHolder().key());
        }
    }

    private static TagKey<Block> tag(String path) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, path));
    }
}
