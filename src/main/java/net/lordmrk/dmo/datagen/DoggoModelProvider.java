package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.block.DogBed;
import net.lordmrk.dmo.block.DogBowl;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

    private static final TextureSlot BORDER = TextureSlot.create("border");
    private static final TextureSlot BED = TextureSlot.create("bed");

    private static final DyeColor[] TENNIS_BALL_COLORS = {
            DyeColor.RED, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE
    };

    public DoggoModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            DogBed bed = (DogBed) entry.getValue();
            String wood = bed.getWoodType().name();
            DyeColor color = bed.getColor();

            ResourceLocation planks = new ResourceLocation("block/" + wood + "_planks");
            ResourceLocation wool = new ResourceLocation("block/" + color.getName() + "_wool");

            TextureMapping textures = new TextureMapping()
                    .put(BORDER, planks)
                    .put(BED, wool)
                    .put(TextureSlot.PARTICLE, planks);

            ModelTemplate model = new ModelTemplate(Optional.of(modLoc("block/dog_bed")), Optional.empty(), BORDER, BED, TextureSlot.PARTICLE);
            ResourceLocation modelId = model.create(modLoc("block/" + entry.getKey()), textures, generator.modelOutput);

            generator.blockStateOutput.accept(createBedBlockState(bed, modelId));
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            DogBowl bowl = (DogBowl) entry.getValue();
            DyeColor color = bowl.getColor();

            ResourceLocation concrete = new ResourceLocation("block/" + color.getName() + "_concrete");
            // The light gray bowl deliberately uses a gray bottom so it contrasts with its light gray side.
            ResourceLocation bottom = color == DyeColor.LIGHT_GRAY
                    ? new ResourceLocation("block/gray_concrete")
                    : new ResourceLocation("block/light_gray_concrete");

            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.BOTTOM, bottom)
                    .put(TextureSlot.SIDE, concrete)
                    .put(TextureSlot.PARTICLE, bottom);

            ModelTemplate model = new ModelTemplate(Optional.of(modLoc("block/dog_bowl")), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.PARTICLE);
            ResourceLocation modelId = model.create(modLoc("block/" + entry.getKey()), textures, generator.modelOutput);

            generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(bowl,
                    Variant.variant().with(VariantProperties.MODEL, modelId)));
        }
    }

    private static MultiVariantGenerator createBedBlockState(Block bed, ResourceLocation modelId) {
        PropertyDispatch.C1<Direction> variants = PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                .select(Direction.EAST, rotated(modelId, VariantProperties.Rotation.R90))
                .select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, modelId))
                .select(Direction.SOUTH, rotated(modelId, VariantProperties.Rotation.R180))
                .select(Direction.WEST, rotated(modelId, VariantProperties.Rotation.R270));

        return MultiVariantGenerator.multiVariant(bed).with(variants);
    }

    private static Variant rotated(ResourceLocation modelId, VariantProperties.Rotation rotation) {
        return Variant.variant()
                .with(VariantProperties.MODEL, modelId)
                .with(VariantProperties.Y_ROT, rotation)
                .with(VariantProperties.UV_LOCK, true);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            generator.generateFlatItem(entry.getValue().asItem(), parentModel(modLoc("block/" + entry.getKey())));
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            generator.generateFlatItem(entry.getValue().asItem(), parentModel(modLoc("block/" + entry.getKey())));
        }

        for (DyeColor color : TENNIS_BALL_COLORS) {
            generator.generateFlatItem(DoggoItems.getTennisBall(color), ModelTemplates.FLAT_ITEM);
        }

        // Legacy tennis ball uses the lime texture.
        ModelTemplate legacy = new ModelTemplate(Optional.of(new ResourceLocation("item/generated")), Optional.empty(), TextureSlot.LAYER0);
        legacy.create(ModelLocationUtils.getModelLocation(DoggoItems.TENNIS_BALL_LEGACY),
                TextureMapping.singleSlot(TextureSlot.LAYER0, modLoc("item/tennis_ball_lime")),
                generator.output);
    }

    private static ModelTemplate parentModel(ResourceLocation parent) {
        return new ModelTemplate(Optional.of(parent), Optional.empty());
    }

    private static ResourceLocation modLoc(String path) {
        return new ResourceLocation(DoggoModOverhauled.MODID, path);
    }
}
