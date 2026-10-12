package net.lordmrk.dmo.modules.rei;

//public class ReiPlugin {}

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.DefaultInformationDisplay;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import java.util.AbstractMap;

public class ReiPlugin implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registerDescriptions(registry);
    }

    private void registerDescriptions(DisplayRegistry registry) {
        addDescriptionToDogBeds(registry,
                Component.translatable("rei.doggomodoverhauled.dog_bed.description.1"));
        addDescriptionToDogBowls(registry,
                Component.translatable("rei.doggomodoverhauled.dog_bowl.description.1"),
                Component.translatable("rei.doggomodoverhauled.dog_bowl.description.2"),
                Component.translatable("rei.doggomodoverhauled.dog_bowl.description.3"));
        addDescriptionToDogToys(registry,
                Component.translatable("rei.doggomodoverhauled.tennis_ball.description.1"),
                Component.translatable("rei.doggomodoverhauled.tennis_ball.description.2"));
    }

    private void addDescriptionToDogBeds(DisplayRegistry registry, Component... lines) {
        for(AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            addDescription(registry, entry.getValue(), lines);
        }
    }

    private void addDescriptionToDogBowls(DisplayRegistry registry, Component... lines) {
        for(AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            addDescription(registry, entry.getValue(), lines);
        }
    }

    private void addDescriptionToDogToys(DisplayRegistry registry, Component... lines) {
        for(AbstractMap.SimpleEntry<String, Item> entry : DoggoItems.TOY_LIST) {
            addDescription(registry, entry.getValue(), lines);
        }
    }

    private void addDescription(DisplayRegistry registry, Block block, Component... lines) {
        addDescription(registry, block.asItem(), lines);
    }

    private void addDescription(DisplayRegistry registry, Item item, Component... lines) {
        ItemStack itemStack = item.getDefaultInstance();
        DefaultInformationDisplay info = DefaultInformationDisplay.createFromEntry(EntryStacks.ofItemHolder(item.builtInRegistryHolder()), itemStack.getHoverName());

        info.lines(lines);
        registry.add(info);
    }
}