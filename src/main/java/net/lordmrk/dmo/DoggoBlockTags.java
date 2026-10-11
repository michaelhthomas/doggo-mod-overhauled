package net.lordmrk.dmo;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DoggoBlockTags {

    public static final TagKey<Block> DOG_BED = of("dog_bed");
    public static final TagKey<Block> DOG_BOWL = of("dog_bowl");

    private static TagKey<Block> of(String id) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(id));
    }
}
