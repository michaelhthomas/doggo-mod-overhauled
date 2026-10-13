package net.lordmrk.dmo;

import com.mojang.blaze3d.platform.NativeImage;
import net.lordmrk.dmo.client.render.entity.DoggoVariantTints;
import net.lordmrk.dmo.client.render.entity.model.DoggoEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.DyeColor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Development helper: exports every doggo variant in a few states as Wavefront {@code .obj} (+ {@code
 * .mtl} + texture {@code .png}) to {@code build/render/doggo/} so they can be inspected in a 3D
 * viewer. One model per variant and state: the jaw is open in the OPEN state, and the eye and collar
 * overlays are baked into the texture for the SLEEPING and TAMED states. Tagged {@code dev}, run with
 * {@code ./gradlew exportModels}.
 */
@Tag("dev")
class DoggoModelExportTest {

    /** The jaw-open angle {@code DoggoEntityModel} applies when the mouth is open. */
    private static final float MOUTH_OPEN = 0.17453292F;

    private enum State {
        CLOSED, OPEN, SLEEPING, TAMED
    }

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
    void exportModels() throws IOException {
        BufferedImage mouth = SoftwareRenderer.image("/assets/doggomodoverhauled/textures/entity/doggo/doggo_mouth.png");
        BufferedImage tongue = SoftwareRenderer.image("/assets/doggomodoverhauled/textures/entity/doggo/doggo_mouth_tongue.png");
        BufferedImage eyes = SoftwareRenderer.image("/assets/doggomodoverhauled/textures/entity/doggo/doggo_eyes_closed.png");
        BufferedImage collar = SoftwareRenderer.image("/assets/doggomodoverhauled/textures/entity/doggo/doggo_collar.png");
        int collarTint = DyeColor.RED.getTextureDiffuseColor();

        File dir = new File("build/render/doggo");
        dir.mkdirs();

        ModelPart root = DoggoEntityModel.getTexturedModelData().bakeRoot();
        ModelPart jaw = root.getChild("head").getChild("real_head").getChild("doggo_mouth");

        for (Map.Entry<String, String> entry : VARIANTS.entrySet()) {
            String variant = entry.getKey();
            BufferedImage base = SoftwareRenderer.image(wolfPath(entry.getValue()));
            DoggoVariantTints.Tint tints = deriveTint(entry.getValue());
            System.out.printf("tints %-9s face=#%06X jaw=#%06X%n", variant, tints.face() & 0xFFFFFF, tints.jaw() & 0xFFFFFF);

            for (State state : State.values()) {
                jaw.xRot = state == State.OPEN ? MOUTH_OPEN : 0.0F;
                List<SoftwareRenderer.Face> faces = SoftwareRenderer.faces(root);
                BufferedImage texture = compositeTexture(state, base, mouth, tongue, eyes, collar,
                        tints.face(), tints.jaw(), collarTint);

                String name = variant + "-" + state.name().toLowerCase(Locale.ROOT);
                ObjExporter.write(dir, name, faces, texture);
                System.out.printf("wrote %s.obj (+ .mtl + .png)%n", name);
            }
        }
    }

    private static String wolfPath(String wolfTexture) {
        return "/assets/minecraft/textures/entity/wolf/" + wolfTexture + "_tame.png";
    }

    private static DoggoVariantTints.Tint deriveTint(String wolfTexture) throws IOException {
        try (InputStream in = DoggoModelExportTest.class.getResourceAsStream(wolfPath(wolfTexture))) {
            NativeImage image = NativeImage.read(in);
            try {
                return DoggoVariantTints.fromImage(image);
            } finally {
                image.close();
            }
        }
    }

    private static BufferedImage compositeTexture(State state, BufferedImage base, BufferedImage mouth,
                                                  BufferedImage tongue, BufferedImage eyes, BufferedImage collar,
                                                  int faceTint, int jawTint, int collarTint) {
        BufferedImage out = new BufferedImage(SoftwareRenderer.TEX_W, SoftwareRenderer.TEX_H, BufferedImage.TYPE_INT_ARGB);
        for (int v = 0; v < SoftwareRenderer.TEX_H; v++) {
            for (int u = 0; u < SoftwareRenderer.TEX_W; u++) {
                out.setRGB(u, v, pixel(state, base, mouth, tongue, eyes, collar, faceTint, jawTint, collarTint, u, v));
            }
        }
        return out;
    }

    private static int pixel(State state, BufferedImage base, BufferedImage mouth, BufferedImage tongue,
                             BufferedImage eyes, BufferedImage collar, int faceTint, int jawTint, int collarTint,
                             int u, int v) {
        switch (state) {
            case OPEN:
                int tonguePixel = tongue.getRGB(u, v);
                if ((tonguePixel >>> 24) != 0) {
                    return tonguePixel;
                }
                int mouthPixel = mouth.getRGB(u, v);
                if ((mouthPixel >>> 24) != 0) {
                    return tint(mouthPixel, jawTint);
                }
                break;
            case SLEEPING:
                int eyePixel = eyes.getRGB(u, v);
                if ((eyePixel >>> 24) != 0) {
                    return tint(eyePixel, faceTint);
                }
                break;
            case TAMED:
                int collarPixel = collar.getRGB(u, v);
                if ((collarPixel >>> 24) != 0) {
                    return tint(collarPixel, collarTint);
                }
                break;
            default:
                break;
        }
        return base.getRGB(u, v);
    }

    /** Multiplies a texture pixel by a render colour, the way {@code submitModel}'s colour does. */
    private static int tint(int argb, int color) {
        int r = ((argb >> 16) & 255) * ((color >> 16) & 255) / 255;
        int g = ((argb >> 8) & 255) * ((color >> 8) & 255) / 255;
        int b = (argb & 255) * (color & 255) / 255;
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}
