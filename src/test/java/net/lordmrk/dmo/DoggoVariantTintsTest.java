package net.lordmrk.dmo;

import com.mojang.blaze3d.platform.NativeImage;
import net.lordmrk.dmo.client.render.entity.DoggoVariantTints;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards {@link DoggoVariantTints}: the samplers must fall back per region (rather than to a single
 * shared constant) when a region has no opaque texels, the face tint must come from the fur directly
 * above the eye pixels (excluding the eyes themselves), and every vanilla wolf texture must yield
 * usable (opaque) tints.
 */
class DoggoVariantTintsTest {

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

    @Test
    void fallsBackPerRegionWhenNoOpaqueTexels() {
        NativeImage image = new NativeImage(64, 32, true);
        try {
            DoggoVariantTints.Tint tint = DoggoVariantTints.fromImage(image);
            assertEquals(0xFFD3CFCF, tint.face());
            assertEquals(0xFF393C3F, tint.jaw());
        } finally {
            image.close();
        }
    }

    @Test
    void faceTintComesFromThePixelsDirectlyAboveTheEyes() {
        NativeImage image = new NativeImage(64, 32, true);
        try {
            fill(image, 0, 64, 0, 32, 0xFF111111); // everything else
            setEyes(image, 0xFFFF00FF);            // the six eye pixels: must be excluded
            setAboveEyes(image, 0xFF204080);       // the fur directly above them
            assertEquals(0xFF204080, DoggoVariantTints.fromImage(image).face());
        } finally {
            image.close();
        }
    }

    @Test
    void everyVariantTextureYieldsOpaqueTints() throws IOException {
        for (Map.Entry<String, String> entry : VARIANTS.entrySet()) {
            String path = "/assets/minecraft/textures/entity/wolf/" + entry.getValue() + "_tame.png";
            try (InputStream in = DoggoVariantTintsTest.class.getResourceAsStream(path)) {
                NativeImage image = NativeImage.read(in);
                try {
                    DoggoVariantTints.Tint tint = DoggoVariantTints.fromImage(image);
                    System.out.printf("tints %-9s face=#%06X jaw=#%06X%n",
                            entry.getKey(), tint.face() & 0xFFFFFF, tint.jaw() & 0xFFFFFF);
                    assertEquals(0xFF, tint.face() >>> 24, entry.getKey() + " face opaque");
                    assertEquals(0xFF, tint.jaw() >>> 24, entry.getKey() + " jaw opaque");
                    assertTrue(luminance(tint.jaw()) < 96, entry.getKey() + " jaw must be the dark lip line");
                } finally {
                    image.close();
                }
            }
        }
    }

    private static void fill(NativeImage image, int u0, int u1, int v0, int v1, int argb) {
        for (int v = v0; v < v1; v++) {
            for (int u = u0; u < u1; u++) {
                image.setPixel(u, v, argb);
            }
        }
    }

    /** The six eye pixels the closed-eye overlay replaces, three per eye and mirrored. */
    private static void setEyes(NativeImage image, int argb) {
        int[] eyes = {5, 5, 8, 5, 4, 6, 5, 6, 8, 6, 9, 6};
        for (int i = 0; i < eyes.length; i += 2) {
            image.setPixel(eyes[i], eyes[i + 1], argb);
        }
    }

    /** The four fur pixels directly above the eye pixels (the other two above-pixels are eyes). */
    private static void setAboveEyes(NativeImage image, int argb) {
        image.setPixel(5, 4, argb);
        image.setPixel(8, 4, argb);
        image.setPixel(4, 5, argb);
        image.setPixel(9, 5, argb);
    }

    private static int luminance(int argb) {
        return (((argb >> 16) & 255) + ((argb >> 8) & 255) + (argb & 255)) / 3;
    }
}
