package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.block.DogBed;
import net.lordmrk.dmo.block.DogBowl;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.AbstractMap;
import java.util.Optional;

/**
 * Generates blockstates and the per-variant block/item models for dog beds and dog bowls,
 * plus the tennis ball item models.
 *
 * <p>The base models are hand-authored Blockbench files in {@code src/main/resources};
 * this provider only generates the coloured/wooded variants that reference them.</p>
 */
public class DoggoModelProvider extends FabricModelProvider {

    private static final TextureKey BORDER = TextureKey.of("border");
    private static final TextureKey BED = TextureKey.of("bed");

    private static final DyeColor[] TENNIS_BALL_COLORS = {
            DyeColor.RED, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE
    };

    public DoggoModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            DogBed bed = (DogBed) entry.getValue();
            String wood = bed.getWoodType().name();
            DyeColor color = bed.getColor();

            Identifier planks = new Identifier("block/" + wood + "_planks");
            Identifier wool = new Identifier("block/" + color.getName() + "_wool");

            TextureMap textures = new TextureMap()
                    .put(BORDER, planks)
                    .put(BED, wool)
                    .put(TextureKey.PARTICLE, planks);

            Model model = new Model(Optional.of(modLoc("block/dog_bed")), Optional.empty(), BORDER, BED, TextureKey.PARTICLE);
            Identifier modelId = model.upload(modLoc("block/" + entry.getKey()), textures, generator.modelCollector);

            generator.blockStateCollector.accept(createBedBlockState(bed, modelId));
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            DogBowl bowl = (DogBowl) entry.getValue();
            DyeColor color = bowl.getColor();

            Identifier concrete = new Identifier("block/" + color.getName() + "_concrete");
            // The light gray bowl deliberately uses a gray bottom so it contrasts with its light gray side.
            Identifier bottom = color == DyeColor.LIGHT_GRAY
                    ? new Identifier("block/gray_concrete")
                    : new Identifier("block/light_gray_concrete");

            TextureMap textures = new TextureMap()
                    .put(TextureKey.BOTTOM, bottom)
                    .put(TextureKey.SIDE, concrete)
                    .put(TextureKey.PARTICLE, bottom);

            Model model = new Model(Optional.of(modLoc("block/dog_bowl")), Optional.empty(), TextureKey.BOTTOM, TextureKey.SIDE, TextureKey.PARTICLE);
            Identifier modelId = model.upload(modLoc("block/" + entry.getKey()), textures, generator.modelCollector);

            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(bowl,
                    BlockStateVariant.create().put(VariantSettings.MODEL, modelId)));
        }
    }

    private static VariantsBlockStateSupplier createBedBlockState(Block bed, Identifier modelId) {
        BlockStateVariantMap.SingleProperty<Direction> variants = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
                .register(Direction.EAST, rotated(modelId, VariantSettings.Rotation.R90))
                .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
                .register(Direction.SOUTH, rotated(modelId, VariantSettings.Rotation.R180))
                .register(Direction.WEST, rotated(modelId, VariantSettings.Rotation.R270));

        return VariantsBlockStateSupplier.create(bed).coordinate(variants);
    }

    private static BlockStateVariant rotated(Identifier modelId, VariantSettings.Rotation rotation) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelId)
                .put(VariantSettings.Y, rotation)
                .put(VariantSettings.UVLOCK, true);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            generator.register(entry.getValue().asItem(), parentModel(modLoc("block/" + entry.getKey())));
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            generator.register(entry.getValue().asItem(), parentModel(modLoc("block/" + entry.getKey())));
        }

        for (DyeColor color : TENNIS_BALL_COLORS) {
            generator.register(DoggoItems.getTennisBall(color), Models.GENERATED);
        }

        // Legacy tennis ball uses the lime texture.
        Model legacy = new Model(Optional.of(new Identifier("item/generated")), Optional.empty(), TextureKey.LAYER0);
        legacy.upload(ModelIds.getItemModelId(DoggoItems.TENNIS_BALL_LEGACY),
                TextureMap.of(TextureKey.LAYER0, modLoc("item/tennis_ball_lime")),
                generator.writer);
    }

    private static Model parentModel(Identifier parent) {
        return new Model(Optional.of(parent), Optional.empty());
    }

    private static Identifier modLoc(String path) {
        return new Identifier(DoggoModOverhauled.MODID, path);
    }
}
