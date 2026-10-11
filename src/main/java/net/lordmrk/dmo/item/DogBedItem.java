package net.lordmrk.dmo.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DogBedItem extends BlockItem {

    //private final Text name;

    public DogBedItem(Block block) {
        super(block, new FabricItemSettings());

        //this.name = block.getName();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.literal("Decoration only for the time being.").withStyle(ChatFormatting.RED));
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
