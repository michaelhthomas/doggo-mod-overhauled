package net.lordmrk.dmo.client.render.entity;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tints for the grayscale overlay layers, sampled from the loaded wolf variant texture so they follow
 * resource packs. {@code face} colours the closed-eye overlay from the fur directly above the eyes;
 * {@code jaw} colours the mouth interior from the muzzle's lower lip, so the inside of the mouth reads
 * as the jaw's visible outside.
 *
 * <p>Regions are the eye pixels of the 64x32 wolf sheet (see {@code AdultWolfModel}), three per eye,
 * and the fur directly above them (see {@link #EYE_PIXELS}). The lip row ({@code v16}) is sampled
 * across its middle columns to skip the drawn outline.</p>
 */
public final class DoggoVariantTints {

    public record Tint(int face, int jaw) {
    }

    private record Box(int u0, int u1, int v0, int v1) {
    }

    /**
     * The eye pixels the closed-eye overlay replaces: three per eye, mirrored, as (u,v) pairs. The
     * face tint is taken from the fur directly above them.
     */
    private static final int[] EYE_PIXELS = {5, 5, 8, 5, 4, 6, 5, 6, 8, 6, 9, 6};

    /** The fur pixels directly above the eye pixels, as (u,v) pairs. */
    private static final int[] FACE_SAMPLE = aboveEyes();

    private static final Box JAW_LIP = new Box(2, 9, 16, 17);

    private static final Tint FALLBACK = new Tint(0xFFD3CFCF, 0xFF393C3F);

    private static final Map<Identifier, Tint> CACHE = new ConcurrentHashMap<>();

    /** Samples a decoded wolf variant sheet. Also used by the development render tests. */
    public static Tint fromImage(NativeImage image) {
        return new Tint(
                averagePoints(image, FALLBACK.face(), FACE_SAMPLE),
                average(image, FALLBACK.jaw(), JAW_LIP));
    }

    public static Tint forTexture(Identifier texture) {
        return CACHE.computeIfAbsent(texture, DoggoVariantTints::derive);
    }

    /** Drops cached tints; call when client resources reload so a texture pack takes effect. */
    public static void clearCache() {
        CACHE.clear();
    }

    private static Tint derive(Identifier texture) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        List<Resource> stack = manager.getResourceStack(texture);
        if (stack.isEmpty()) {
            return FALLBACK;
        }
        try (InputStream in = stack.getFirst().open()) {
            NativeImage image = NativeImage.read(in);
            try {
                return fromImage(image);
            } finally {
                image.close();
            }
        } catch (IOException e) {
            return FALLBACK;
        }
    }

    private static int average(NativeImage image, int fallback, Box... boxes) {
        long r = 0;
        long g = 0;
        long b = 0;
        int n = 0;
        for (Box box : boxes) {
            for (int v = box.v0(); v < box.v1() && v < image.getHeight(); v++) {
                for (int u = box.u0(); u < box.u1() && u < image.getWidth(); u++) {
                    int argb = image.getPixel(u, v);
                    if ((argb >>> 24) == 0) {
                        continue;
                    }
                    r += (argb >> 16) & 255;
                    g += (argb >> 8) & 255;
                    b += argb & 255;
                    n++;
                }
            }
        }
        return pack(r, g, b, n, fallback);
    }

    private static int averagePoints(NativeImage image, int fallback, int[] points) {
        long r = 0;
        long g = 0;
        long b = 0;
        int n = 0;
        for (int i = 0; i < points.length; i += 2) {
            int u = points[i];
            int v = points[i + 1];
            if (u < 0 || v < 0 || u >= image.getWidth() || v >= image.getHeight()) {
                continue;
            }
            int argb = image.getPixel(u, v);
            if ((argb >>> 24) == 0) {
                continue;
            }
            r += (argb >> 16) & 255;
            g += (argb >> 8) & 255;
            b += argb & 255;
            n++;
        }
        return pack(r, g, b, n, fallback);
    }

    private static int pack(long r, long g, long b, int n, int fallback) {
        if (n == 0) {
            return fallback;
        }
        return 0xFF000000 | ((int) (r / n) << 16) | ((int) (g / n) << 8) | (int) (b / n);
    }

    /** The fur pixels directly above each eye pixel, skipping those that are themselves eye pixels. */
    private static int[] aboveEyes() {
        int[] out = new int[EYE_PIXELS.length];
        int n = 0;
        for (int i = 0; i < EYE_PIXELS.length; i += 2) {
            int u = EYE_PIXELS[i];
            int v = EYE_PIXELS[i + 1] - 1;
            if (!isEye(u, v)) {
                out[n++] = u;
                out[n++] = v;
            }
        }
        return Arrays.copyOf(out, n);
    }

    /** @return whether {@code (u, v)} is one of the eye pixels the closed-eye overlay replaces. */
    private static boolean isEye(int u, int v) {
        for (int i = 0; i < EYE_PIXELS.length; i += 2) {
            if (EYE_PIXELS[i] == u && EYE_PIXELS[i + 1] == v) {
                return true;
            }
        }
        return false;
    }

    private DoggoVariantTints() {
    }
}
