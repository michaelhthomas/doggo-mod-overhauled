package net.lordmrk.dmo;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class DoggoItemTags {

    public static final TagKey<Item> DOG_BED = of("dog_bed");
    public static final TagKey<Item> DOG_BOWL = of("dog_bowl");
    public static final TagKey<Item> TENNIS_BALL = of("tennis_ball");

    private static TagKey<Item> of(String id) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(DoggoModOverhauled.MODID, id));
    }
}
