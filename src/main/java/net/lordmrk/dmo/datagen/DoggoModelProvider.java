package net.lordmrk.dmo.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.lordmrk.dmo.DoggoBlocks;
import net.lordmrk.dmo.DoggoItems;
import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.block.DogBed;
import net.lordmrk.dmo.block.DogBowl;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.AbstractMap;
import java.util.Optional;

/**
 * Generates blockstates and the per-variant block models for dog beds and dog bowls, plus the
 * item model definitions and tennis ball item models.
 *
 * <p>The base models are hand-authored Blockbench files in {@code src/main/resources}; this
 * provider only generates the coloured/wooded variants that reference them.</p>
 */
public class DoggoModelProvider extends FabricModelProvider {

    private static final TextureSlot BORDER = TextureSlot.create("border");
    private static final TextureSlot BED = TextureSlot.create("bed");

    private static final DyeColor[] TENNIS_BALL_COLORS = {
            DyeColor.RED, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE
    };

    public DoggoModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BED_LIST) {
            DogBed bed = (DogBed) entry.getValue();
            String wood = bed.getWoodType().name();
            String color = bed.getColor().getName();

            TextureMapping textures = new TextureMapping()
                    .put(BORDER, blockTexture(wood + "_planks"))
                    .put(BED, blockTexture(color + "_wool"))
                    .put(TextureSlot.PARTICLE, blockTexture(wood + "_planks"));

            ModelTemplate template = new ModelTemplate(Optional.of(modLoc("block/dog_bed")), Optional.empty(), BORDER, BED, TextureSlot.PARTICLE);
            Identifier model = template.create(bed, textures, generator.modelOutput);

            MultiVariant plain = plainVariant(model);
            generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(bed)
                    .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING)
                            .select(Direction.NORTH, plain)
                            .select(Direction.EAST, plain.with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                            .select(Direction.SOUTH, plain.with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                            .select(Direction.WEST, plain.with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))));

            generator.itemModelOutput.accept(bed.asItem(), ItemModelUtils.plainModel(model));
        }

        for (AbstractMap.SimpleEntry<String, Block> entry : DoggoBlocks.BOWL_LIST) {
            DogBowl bowl = (DogBowl) entry.getValue();
            String color = bowl.getColor().getName();
            // The light gray bowl deliberately uses a gray bottom so it contrasts with its light gray side.
            String bottom = bowl.getColor() == DyeColor.LIGHT_GRAY ? "gray_concrete" : "light_gray_concrete";

            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.BOTTOM, blockTexture(bottom))
                    .put(TextureSlot.SIDE, blockTexture(color + "_concrete"))
                    .put(TextureSlot.PARTICLE, blockTexture(bottom));

            ModelTemplate template = new ModelTemplate(Optional.of(modLoc("block/dog_bowl")), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.PARTICLE);
            Identifier model = template.create(bowl, textures, generator.modelOutput);

            generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(bowl, plainVariant(model)));
            generator.itemModelOutput.accept(bowl.asItem(), ItemModelUtils.plainModel(model));
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        for (DyeColor color : TENNIS_BALL_COLORS) {
            createTennisBallModel(generator, DoggoItems.getTennisBall(color), color.getName());
        }

        // Legacy tennis ball uses the lime texture.
        createTennisBallModel(generator, DoggoItems.TENNIS_BALL_LEGACY, DyeColor.LIME.getName());
    }

    private static void createTennisBallModel(ItemModelGenerators generator, Item item, String textureColor) {
        ModelTemplate template = new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("item/generated")), Optional.empty(), TextureSlot.LAYER0);
        TextureMapping textures = TextureMapping.singleSlot(TextureSlot.LAYER0, new Material(modLoc("item/tennis_ball_" + textureColor)));
        Identifier model = template.create(item, textures, generator.modelOutput);

        generator.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
    }

    private static MultiVariant plainVariant(Identifier model) {
        return new MultiVariant(WeightedList.of(new Variant(model)));
    }

    private static Material blockTexture(String name) {
        return new Material(Identifier.withDefaultNamespace("block/" + name));
    }

    private static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, path);
    }
}
