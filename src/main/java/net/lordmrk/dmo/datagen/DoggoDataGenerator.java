package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;

/**
 * Data generation entrypoint (fabric-datagen).
 *
 * <p>Run with {@code ./gradlew runDatagen}. Output is written to the directory
 * configured via {@code -Dfabric-api.datagen.output-dir} in {@code build.gradle}.</p>
 */
public class DoggoDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        Pack pack = generator.createPack();

        pack.addProvider(DoggoRecipeProvider::new);
        pack.addProvider(DoggoBlockLootTableProvider::new);
        pack.addProvider(DoggoModelProvider::new);

        DoggoBlockTagProvider blockTagProvider = pack.addProvider(DoggoBlockTagProvider::new);
        pack.addProvider((output, registriesFuture) ->
                new DoggoItemTagProvider(output, registriesFuture, blockTagProvider));
    }
}
