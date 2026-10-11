package net.lordmrk.dmo;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lordmrk.dmo.item.TennisBall;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class DoggoItems {

    public static final List<AbstractMap.SimpleEntry<String, Item>> TOY_LIST = new ArrayList<>();

    public static final Item TENNIS_BALL_LEGACY = new TennisBall(DyeColor.LIME, true);
    public static final TennisBall TENNIS_BALL_RED = new TennisBall(DyeColor.RED, false);
    public static final TennisBall TENNIS_BALL_YELLOW = new TennisBall(DyeColor.YELLOW, false);
    public static final TennisBall TENNIS_BALL_LIME = new TennisBall(DyeColor.LIME, false);
    public static final TennisBall TENNIS_BALL_LIGHT_BLUE = new TennisBall(DyeColor.LIGHT_BLUE, false);

    public static void addToDoggoModItemGroup() {
        addItemsToItemGroup(DoggoItemGroups.DOG_STUFF_REGISTRY_KEY, TOY_LIST, Items.ENDER_EYE);
    }

    private static void addItemsToItemGroup(ResourceKey<CreativeModeTab> registryKey, List<AbstractMap.SimpleEntry<String, Item>> itemList, ItemLike addAfter) {
        ItemGroupEvents.modifyEntriesEvent(registryKey).register(content -> {
            ItemLike lastItem = addAfter;

            for(AbstractMap.SimpleEntry<String, Item> entry : itemList) {
                if(lastItem == null) {
                    content.accept(entry.getValue());
                } else {
                    content.addAfter(lastItem, entry.getValue());
                }

                lastItem = entry.getValue();
            }
        });
    }

    public static void addToToolItemsItemGroup() {
        addItemsToItemGroup(CreativeModeTabs.TOOLS_AND_UTILITIES, TOY_LIST, Items.ENDER_EYE);
    }

    public static TennisBall getTennisBall(DyeColor color) {
        switch (color) {
            case RED:
                return TENNIS_BALL_RED;
            case YELLOW:
                return TENNIS_BALL_YELLOW;
            case LIME:
                return TENNIS_BALL_LIME;
            case LIGHT_BLUE:
                return TENNIS_BALL_LIGHT_BLUE;
            default:
                throw new IllegalArgumentException("No tennis ball found for color \"" + color.getName() + "\"");
        }
    }

    public static void registerAll() {
        for(AbstractMap.SimpleEntry<String, Item> entry : TOY_LIST) {
            registerItem(entry.getKey(), entry.getValue());
        }
    }

    private static void registerItem(String id, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(DoggoModOverhauled.MODID, id), item);
    }

    static {
        TOY_LIST.add(new AbstractMap.SimpleEntry<>("tennis_ball_red", TENNIS_BALL_RED));
        TOY_LIST.add(new AbstractMap.SimpleEntry<>("tennis_ball_yellow", TENNIS_BALL_YELLOW));
        TOY_LIST.add(new AbstractMap.SimpleEntry<>("tennis_ball_lime", TENNIS_BALL_LIME));
        TOY_LIST.add(new AbstractMap.SimpleEntry<>("tennis_ball_light_blue", TENNIS_BALL_LIGHT_BLUE));
        TOY_LIST.add(new AbstractMap.SimpleEntry<>("tennis_ball", TENNIS_BALL_LEGACY));
    }
}
