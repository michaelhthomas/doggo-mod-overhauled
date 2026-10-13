package net.lordmrk.dmo;

import net.lordmrk.dmo.client.render.entity.model.DoggoEntityModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Renders the doggo and the vanilla wolf with the mouth closed and asserts they look the same.
 *
 * <p>This guards the variant work: the doggo is a wolf reskin whose base texture is the vanilla
 * variant texture, with the lower-jaw cube drawn by overlay layers. When the mouth is closed the
 * jaw's interior is hidden, so the doggo's visible surfaces must reproduce the wolf's muzzle
 * underside and lip. Both models are rendered in software using the same texture compositing the
 * renderer does (base vanilla texture, then the mouth overlay, then the tongue) and the pixels are
 * compared.</p>
 *
 * <p>The lower jaw reproduces the wolf muzzle's bottom unit by sampling the wolf's own texture (see
 * {@code DoggoEntityModel}), so with the mouth closed the doggo renders the wolf's own texels and
 * matches it pixel for pixel (see {@link #MISMATCH_BUDGET}). It still fails loudly if a leg stops
 * being mirrored, the jaw's texture mapping regresses, or the mouth stops closing flush.</p>
 */
class DoggoClosedMouthComparisonTest {

    /**
     * Differing pixels allowed per render. The doggo's texels are identical to the wolf's, but
     * splitting the muzzle so the lower jaw can open leaves its front/side quad as two triangles
     * instead of the wolf's one, which the software rasterizer can break differently at a shared
     * corner pixel.
     */
    private static final int MISMATCH_BUDGET = 1;

    /** Vanilla variant id -> vanilla tame-texture file base name. */
    private static final Map<String, String> VARIANTS = new LinkedHashMap<>();
    static {
        VARIANTS.put("pale", "wolf");
        VARIANTS.put("spotted", "wolf_spotted");
        VARIANTS.put("snowy", "wolf_snowy");
        VARIANTS.put("black", "wolf_black");
        VARIANTS.put("ashen", "wolf_ashen");
        VARIANTS.put("rusty", "wolf_rusty");
        VARIANTS.put("woods", "wolf_woods");
        VARIANTS.put("chestnut", "wolf_chestnut");
        VARIANTS.put("striped", "wolf_striped");
    }

    private static final List<SoftwareRenderer.View> VIEWS = List.of(
            new SoftwareRenderer.View("side", new float[]{1, 0, 0}, new float[]{0, 0, 1}, new float[]{0, -1, 0}),
            new SoftwareRenderer.View("below", new float[]{0, -1, 0}, new float[]{1, 0, 0}, new float[]{0, 0, -1}));

    @Test
    void closedMouthMatchesVanillaWolfForEveryVariant() throws IOException {
        List<SoftwareRenderer.Face> wolfFaces = SoftwareRenderer.faces(wolfRoot());
        List<SoftwareRenderer.Face> doggoFaces = SoftwareRenderer.faces(doggoRoot());

        BufferedImage mouth = SoftwareRenderer.image("/assets/doggomodoverhauled/textures/entity/doggo/doggo_mouth.png");
        BufferedImage tongue = SoftwareRenderer.image("/assets/doggomodoverhauled/textures/entity/doggo/doggo_mouth_tongue.png");

        List<String> failures = new ArrayList<>();
        for (Map.Entry<String, String> entry : VARIANTS.entrySet()) {
            String variant = entry.getKey();
            BufferedImage base = SoftwareRenderer.image("/assets/minecraft/textures/entity/wolf/" + entry.getValue() + "_tame.png");

            for (SoftwareRenderer.View view : VIEWS) {
                float[] bounds = SoftwareRenderer.bounds(List.of(wolfFaces, doggoFaces), view);
                BufferedImage wolf = SoftwareRenderer.render(wolfFaces, (u, v) -> SoftwareRenderer.sample(base, u, v), view, bounds);
                BufferedImage doggo = SoftwareRenderer.render(doggoFaces, composite(base, mouth, tongue), view, bounds);

                int mismatches = mismatches(wolf, doggo);
                System.out.printf("closed-mouth %-9s %-6s mismatches=%d%n", variant, view.name(), mismatches);
                if (mismatches > MISMATCH_BUDGET) {
                    failures.add(variant + " " + view.name() + "=" + mismatches + "px");
                }
            }
        }
        assertTrue(failures.isEmpty(), "closed-mouth doggo/wolf mismatches: " + failures);
    }

    private static ModelPart wolfRoot() {
        return LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE),
                SoftwareRenderer.TEX_W, SoftwareRenderer.TEX_H).bakeRoot();
    }

    private static ModelPart doggoRoot() {
        return DoggoEntityModel.getTexturedModelData().bakeRoot();
    }

    private static SoftwareRenderer.Sampler composite(BufferedImage base, BufferedImage mouth, BufferedImage tongue) {
        return (u, v) -> {
            int x = (int) u;
            int y = (int) v;
            if (x < 0 || y < 0 || x >= SoftwareRenderer.TEX_W || y >= SoftwareRenderer.TEX_H) {
                return 0;
            }
            int tonguePixel = tongue.getRGB(x, y);
            if ((tonguePixel >>> 24) != 0) {
                return tonguePixel;
            }
            int mouthPixel = mouth.getRGB(x, y);
            if ((mouthPixel >>> 24) != 0) {
                return mouthPixel;
            }
            return base.getRGB(x, y);
        };
    }

    /** @return the number of pixels where the two renders differ. */
    private static int mismatches(BufferedImage a, BufferedImage b) {
        int mismatches = 0;
        for (int y = 0; y < a.getHeight(); y++) {
            for (int x = 0; x < a.getWidth(); x++) {
                if (a.getRGB(x, y) != b.getRGB(x, y)) {
                    mismatches++;
                }
            }
        }
        return mismatches;
    }
}
