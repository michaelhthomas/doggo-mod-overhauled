package net.lordmrk.dmo;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lordmrk.dmo.block.DogBed;
import net.lordmrk.dmo.block.DogBowl;
import net.lordmrk.dmo.item.DogBedItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class DoggoBlocks {

    private static final DyeColor[] COLORS = new DyeColor[] {
            DyeColor.WHITE,
            DyeColor.LIGHT_GRAY,
            DyeColor.GRAY,
            DyeColor.BLACK,
            DyeColor.BROWN,
            DyeColor.RED,
            DyeColor.ORANGE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.GREEN,
            DyeColor.CYAN,
            DyeColor.LIGHT_BLUE,
            DyeColor.BLUE,
            DyeColor.PURPLE,
            DyeColor.MAGENTA,
            DyeColor.PINK
    };

    public static final List<AbstractMap.SimpleEntry<String, Block>> BED_LIST = new ArrayList<>();
    public static final List<AbstractMap.SimpleEntry<String, Block>> BOWL_LIST = new ArrayList<>();

    private static void addToItemGroup(ResourceKey<CreativeModeTab> registryKey, List<AbstractMap.SimpleEntry<String, Block>> blockList, ItemLike addAfter) {
        ItemGroupEvents.modifyEntriesEvent(registryKey).register(content -> {
            ItemLike lastItem = addAfter;

            for(AbstractMap.SimpleEntry<String, Block> entry : blockList) {
                if(lastItem == null) {
                    content.accept(entry.getValue());
                } else {
                    content.addAfter(lastItem, entry.getValue());
                }

                lastItem = entry.getValue();
            }
        });
    }

    public static void addToColoredBlocksItemGroup() {
        ResourceKey<CreativeModeTab> registryKey = CreativeModeTabs.COLORED_BLOCKS;

        addToItemGroup(registryKey, BED_LIST, Items.PINK_BANNER);
        addToItemGroup(registryKey, BOWL_LIST, BED_LIST.get(BED_LIST.size() - 1).getValue());
    }

    public static void addToDoggoModItemGroup() {
        ResourceKey<CreativeModeTab> registryKey = DoggoItemGroups.DOG_STUFF_REGISTRY_KEY;

        addToItemGroup(registryKey, BED_LIST, Items.PINK_BANNER);
        addToItemGroup(registryKey, BOWL_LIST, BED_LIST.get(BED_LIST.size() - 1).getValue());
    }

    public static void addToFunctionalBlocksItemGroup() {
        ResourceKey<CreativeModeTab> registryKey = CreativeModeTabs.FUNCTIONAL_BLOCKS;

        addToItemGroup(registryKey, BED_LIST, Items.PINK_BANNER);
        addToItemGroup(registryKey, BOWL_LIST, BED_LIST.get(BED_LIST.size() - 1).getValue());
    }

    @NotNull
    public static DogBowl getDogBowl(DyeColor color) {
        for(DogBowl dogBowl : getDogBowls()) {
            if(dogBowl.getColor() == color) {
                return dogBowl;
            }
        }

        throw new IllegalArgumentException("No dog bowl found for color \"" + color.getName() + "\"");
    }

    public static DogBowl[] getDogBowls() {
        DogBowl[] dogBowls = new DogBowl[BOWL_LIST.size()];

        for(int i = 0; i < BOWL_LIST.size(); i++) {
            dogBowls[i] = (DogBowl) BOWL_LIST.get(i).getValue();
        }

        return dogBowls;
    }

    public static void registerAll() {
        for(AbstractMap.SimpleEntry<String, Block> entry : BED_LIST) {
            registerBlock(entry.getKey(), entry.getValue());
        }

        for(AbstractMap.SimpleEntry<String, Block> entry : BOWL_LIST) {
            registerBlock(entry.getKey(), entry.getValue());
        }
    }

    private static void registerBlock(String id, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(DoggoModOverhauled.MODID, id), block);

        if(block instanceof DogBed) {
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(DoggoModOverhauled.MODID, id), new DogBedItem(block));
            return;
        }

        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(DoggoModOverhauled.MODID, id), new BlockItem(block, new FabricItemSettings()));
    }

    static {
        for(WoodType woodType : WoodType.values().toList()) {
            //String woodTypeName = woodType.name().replaceAll(":", "_");
            String woodTypeName = woodType.name();

            if(woodTypeName.contains(":")) {
                continue;
            }

            for(DyeColor color : COLORS) {
                BED_LIST.add(new AbstractMap.SimpleEntry<>("dog_bed_" + woodTypeName + "_" + color.getName(), new DogBed(color, woodType)));
            }
        }

        for(DyeColor color : COLORS) {
            BOWL_LIST.add(new AbstractMap.SimpleEntry<>("dog_bowl_" + color.getName(), new DogBowl(color)));
        }
    }
}
