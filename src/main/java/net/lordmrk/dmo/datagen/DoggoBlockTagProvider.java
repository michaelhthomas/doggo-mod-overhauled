package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the mod's block tags and the {@code minecraft} block tags used for tool requirements.
 */
public class DoggoBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public DoggoBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        FabricTagBuilder dogBedTag = getOrCreateTagBuilder(tag("dog_bed"));
        FabricTagBuilder dogBowlTag = getOrCreateTagBuilder(tag("dog_bowl"));
        FabricTagBuilder pickaxeMineable = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE);
        FabricTagBuilder needsStoneTool = getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL);

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            dogBedTag.add(entry.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            Block bowl = entry.getValue();
            dogBowlTag.add(bowl);
            pickaxeMineable.add(bowl);
            needsStoneTool.add(bowl);
        }
    }

    private static TagKey<Block> tag(String path) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(DoggoModOverhauled.MODID, path));
    }
}
