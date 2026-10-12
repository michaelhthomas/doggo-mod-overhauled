package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the self-drop block loot tables for every dog bed and dog bowl.
 */
public class DoggoBlockLootTableProvider extends FabricBlockLootSubProvider {

    public DoggoBlockLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
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
