package net.lordmrk.dmo;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.lordmrk.dmo.block.DogBed;
import net.lordmrk.dmo.block.DogBowl;
import net.lordmrk.dmo.item.DogBedItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
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
        CreativeModeTabEvents.modifyOutputEvent(registryKey).register(content -> {
            ItemLike lastItem = addAfter;

            for(AbstractMap.SimpleEntry<String, Block> entry : blockList) {
                if(lastItem == null) {
                    content.accept(entry.getValue());
                } else {
                    content.insertAfter(lastItem, entry.getValue());
                }

                lastItem = entry.getValue();
            }
        });
    }

    public static void addToColoredBlocksItemGroup() {
        ResourceKey<CreativeModeTab> registryKey = CreativeModeTabs.COLORED_BLOCKS;

        addToItemGroup(registryKey, BED_LIST, Items.BANNER.pick(DyeColor.PINK));
        addToItemGroup(registryKey, BOWL_LIST, BED_LIST.get(BED_LIST.size() - 1).getValue());
    }

    public static void addToDoggoModItemGroup() {
        ResourceKey<CreativeModeTab> registryKey = DoggoItemGroups.DOG_STUFF_REGISTRY_KEY;

        addToItemGroup(registryKey, BED_LIST, Items.BANNER.pick(DyeColor.PINK));
        addToItemGroup(registryKey, BOWL_LIST, BED_LIST.get(BED_LIST.size() - 1).getValue());
    }

    public static void addToFunctionalBlocksItemGroup() {
        ResourceKey<CreativeModeTab> registryKey = CreativeModeTabs.FUNCTIONAL_BLOCKS;

        addToItemGroup(registryKey, BED_LIST, Items.BANNER.pick(DyeColor.PINK));
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
        Identifier identifier = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, id);
        Registry.register(BuiltInRegistries.BLOCK, identifier, block);

        Item.Properties properties = new Item.Properties().setId(itemKey(id)).useBlockDescriptionPrefix();

        if(block instanceof DogBed) {
            Registry.register(BuiltInRegistries.ITEM, identifier, new DogBedItem(block, properties));
            return;
        }

        Registry.register(BuiltInRegistries.ITEM, identifier, new BlockItem(block, properties));
    }

    public static ResourceKey<Block> blockKey(String id) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, id));
    }

    public static ResourceKey<Item> itemKey(String id) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, id));
    }

    static {
        for(WoodType woodType : WoodType.values().toList()) {
            String woodTypeName = woodType.name();

            if(woodTypeName.contains(":")) {
                continue;
            }

            for(DyeColor color : COLORS) {
                String id = "dog_bed_" + woodTypeName + "_" + color.getName();
                BED_LIST.add(new AbstractMap.SimpleEntry<>(id, new DogBed(blockKey(id), color, woodType)));
            }
        }

        for(DyeColor color : COLORS) {
            String id = "dog_bowl_" + color.getName();
            BOWL_LIST.add(new AbstractMap.SimpleEntry<>(id, new DogBowl(blockKey(id), color)));
        }
    }
}
