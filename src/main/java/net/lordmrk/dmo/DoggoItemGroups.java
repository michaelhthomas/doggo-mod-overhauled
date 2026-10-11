package net.lordmrk.dmo;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class DoggoItemGroups {

    public static final ResourceKey<CreativeModeTab> DOG_STUFF_REGISTRY_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(DoggoModOverhauled.MODID, "doggomodoverhauled"));
    public static final CreativeModeTab DOG_STUFF_GROUP = FabricItemGroup.builder()
            .title(Component.translatable("itemGroup." + DoggoModOverhauled.MODID + ".doggomodoverhauled"))
            .icon(() -> new ItemStack(DoggoBlocks.getDogBowl(DyeColor.CYAN)))
            .build();

    public static void registerAll() {
        registerItemGroup("doggomodoverhauled", DOG_STUFF_GROUP);
    }

    private static void registerItemGroup(String id, CreativeModeTab itemGroup) {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(DoggoModOverhauled.MODID, id), itemGroup);
    }
}
