package net.lordmrk.dmo;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class DoggoItemGroups {

    public static final ResourceKey<CreativeModeTab> DOG_STUFF_REGISTRY_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "doggomodoverhauled"));
    public static final CreativeModeTab DOG_STUFF_GROUP = FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + DoggoModOverhauled.MODID + ".doggomodoverhauled"))
            .icon(() -> new ItemStack(DoggoBlocks.getDogBowl(DyeColor.CYAN)))
            .build();

    public static void registerAll() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, DOG_STUFF_REGISTRY_KEY, DOG_STUFF_GROUP);
    }
}
