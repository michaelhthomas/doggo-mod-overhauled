package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.minecraft.world.level.block.Block;
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
            dropSelf(entry.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            dropSelf(entry.getValue());
        }
    }
}
