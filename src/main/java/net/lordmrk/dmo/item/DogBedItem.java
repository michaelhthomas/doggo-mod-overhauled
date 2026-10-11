package net.lordmrk.dmo.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class DogBedItem extends BlockItem {

    //private final Text name;

    public DogBedItem(Block block, Item.Properties properties) {
        super(block, properties);

        //this.name = block.getName();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.literal("Decoration only for the time being.").withStyle(ChatFormatting.RED));
    }
/*
    @Override
    public Text getName() {
        return name;
    }

    @Override
    public Text getName(ItemStack itemStack) {
        return name;
    }*/
}
