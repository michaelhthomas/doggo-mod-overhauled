package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the mod's block tags and the {@code minecraft} block tags used for tool requirements.
 */
public class DoggoBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public DoggoBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        FabricTagBuilder dogBedTag = getOrCreateTagBuilder(tag("dog_bed"));
        FabricTagBuilder dogBowlTag = getOrCreateTagBuilder(tag("dog_bowl"));
        FabricTagBuilder pickaxeMineable = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);
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
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(DoggoModOverhauled.MODID, path));
    }
}
