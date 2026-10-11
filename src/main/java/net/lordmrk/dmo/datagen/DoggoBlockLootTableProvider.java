package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.minecraft.block.Block;

import java.util.AbstractMap;

/**
 * Generates the self-drop block loot tables for every dog bed and dog bowl.
 */
public class DoggoBlockLootTableProvider extends FabricBlockLootTableProvider {

    public DoggoBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            addDrop(entry.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            addDrop(entry.getValue());
        }
    }
}
