package net.lordmrk.dmo;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A tiny software rasterizer for a baked {@link ModelPart}, used by the rendering tests. It walks the
 * model's cubes, projects each face into a 2D view, and fills pixels by sampling a texture with the
 * same per-face shading Minecraft applies. It is only an approximation of the real client renderer,
 * but deterministic and good enough to compare two models or preview one.
 */
final class SoftwareRenderer {

    static final int TEX_W = 64;
    static final int TEX_H = 32;
    /** Pixels per model unit. */
    static final int SCALE = 200;

    /** One model face: 4 positions, 4 UVs (in texels) and the transformed normal. */
    record Face(float[][] pos, float[][] uv, float[] normal) {
    }

    /** A camera: {@code forward} is the view direction, {@code right}/{@code up} span the screen. */
    record View(String name, float[] forward, float[] right, float[] up) {
    }

    interface Sampler {
        /** @return ARGB, or a fully transparent value to skip the pixel. */
        int sample(double u, double v);
    }

    static List<Face> faces(ModelPart root) {
        List<Face> out = new ArrayList<>();
        root.visit(new PoseStack(), (pose, path, cubeIndex, cube) -> {
            Matrix4f matrix = pose.pose();
            for (ModelPart.Polygon polygon : cube.polygons) {
                Vector3f normal = pose.transformNormal(polygon.normal(), new Vector3f());
                float[] n = {normal.x(), normal.y(), normal.z()};
                ModelPart.Vertex[] vertices = polygon.vertices();
                float[][] pos = new float[4][3];
                float[][] uv = new float[4][2];
                for (int i = 0; i < 4; i++) {
                    Vector3f p = matrix.transformPosition(
                            vertices[i].worldX(), vertices[i].worldY(), vertices[i].worldZ(), new Vector3f());
                    pos[i] = new float[]{p.x(), p.y(), p.z()};
                    uv[i] = new float[]{vertices[i].u() * TEX_W, vertices[i].v() * TEX_H};
                }
                out.add(new Face(pos, uv, n));
            }
        });
        return out;
    }

    static float[] bounds(List<List<Face>> sets, View view) {
        float[] b = {Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE};
        for (List<Face> set : sets) {
            for (Face face : set) {
                for (float[] p : face.pos()) {
                    b[0] = Math.min(b[0], dot(p, view.right()));
                    b[1] = Math.min(b[1], dot(p, view.up()));
                    b[2] = Math.max(b[2], dot(p, view.right()));
                    b[3] = Math.max(b[3], dot(p, view.up()));
                }
            }
        }
        return b;
    }

    static BufferedImage render(List<Face> faces, Sampler sampler, View view, float[] bounds) {
        float[] forward = view.forward();
        int pad = 2;
        int width = (int) ((bounds[2] - bounds[0]) * SCALE) + 2 * pad + 1;
        int height = (int) ((bounds[3] - bounds[1]) * SCALE) + 2 * pad + 1;
        float[] depth = new float[width * height];
        int[] pixels = new int[width * height];
        Arrays.fill(depth, Float.MAX_VALUE);

        for (Face face : faces) {
            if (dot(face.normal(), forward) >= 0) {
                continue; // back-facing
            }
            float shade = shade(face.normal());
            double[][] v = new double[4][5];
            for (int i = 0; i < 4; i++) {
                v[i][0] = dot(face.pos()[i], view.right());
                v[i][1] = dot(face.pos()[i], view.up());
                v[i][2] = dot(face.pos()[i], forward);
                v[i][3] = face.uv()[i][0];
                v[i][4] = face.uv()[i][1];
            }
            triangle(pixels, depth, width, height, pad, bounds, v[0], v[1], v[2], sampler, shade);
            triangle(pixels, depth, width, height, pad, bounds, v[0], v[2], v[3], sampler, shade);
        }

        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        out.setRGB(0, 0, width, height, pixels, 0, width);
        return out;
    }

    static int sample(BufferedImage image, double u, double v) {
        int x = (int) u;
        int y = (int) v;
        return (x < 0 || y < 0 || x >= TEX_W || y >= TEX_H) ? 0 : image.getRGB(x, y);
    }

    static BufferedImage image(String path) throws IOException {
        try (InputStream in = SoftwareRenderer.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IOException("Missing test resource: " + path);
            }
            return ImageIO.read(in);
        }
    }

    private static void triangle(int[] pixels, float[] depth, int width, int height, int pad, float[] bounds,
                                 double[] a0, double[] a1, double[] a2, Sampler sampler, float shade) {
        double[] p0 = {(a0[0] - bounds[0]) * SCALE + pad, (bounds[3] - a0[1]) * SCALE + pad, a0[2]};
        double[] p1 = {(a1[0] - bounds[0]) * SCALE + pad, (bounds[3] - a1[1]) * SCALE + pad, a1[2]};
        double[] p2 = {(a2[0] - bounds[0]) * SCALE + pad, (bounds[3] - a2[1]) * SCALE + pad, a2[2]};
        double area = (p1[0] - p0[0]) * (p2[1] - p0[1]) - (p1[1] - p0[1]) * (p2[0] - p0[0]);
        if (Math.abs(area) < 1e-9) {
            return;
        }
        int x0 = (int) Math.floor(Math.min(p0[0], Math.min(p1[0], p2[0])));
        int x1 = (int) Math.ceil(Math.max(p0[0], Math.max(p1[0], p2[0])));
        int y0 = (int) Math.floor(Math.min(p0[1], Math.min(p1[1], p2[1])));
        int y1 = (int) Math.ceil(Math.max(p0[1], Math.max(p1[1], p2[1])));
        for (int y = Math.max(0, y0); y <= Math.min(height - 1, y1); y++) {
            for (int x = Math.max(0, x0); x <= Math.min(width - 1, x1); x++) {
                double cx = x + 0.5;
                double cy = y + 0.5;
                // Barycentric weights: w0 -> p0, w1 -> p1, w2 -> p2.
                double w2 = ((p1[0] - p0[0]) * (cy - p0[1]) - (p1[1] - p0[1]) * (cx - p0[0])) / area;
                double w1 = ((cx - p0[0]) * (p2[1] - p0[1]) - (cy - p0[1]) * (p2[0] - p0[0])) / area;
                double w0 = 1 - w1 - w2;
                if (w0 < -1e-6 || w1 < -1e-6 || w2 < -1e-6) {
                    continue;
                }
                int index = y * width + x;
                double z = w0 * p0[2] + w1 * p1[2] + w2 * p2[2];
                if (z >= depth[index] - 1e-6) {
                    continue;
                }
                double u = w0 * a0[3] + w1 * a1[3] + w2 * a2[3];
                double v = w0 * a0[4] + w1 * a1[4] + w2 * a2[4];
                int color = sampler.sample(u, v);
                if ((color >>> 24) == 0) {
                    continue;
                }
                depth[index] = (float) z;
                int r = clamp((int) (((color >> 16) & 255) * shade));
                int g = clamp((int) (((color >> 8) & 255) * shade));
                int b = clamp((int) ((color & 255) * shade));
                pixels[index] = 0xFF000000 | (r << 16) | (g << 8) | b;
            }
        }
    }

    private static float dot(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static float shade(float[] normal) {
        if (normal[1] < -0.5f) {
            return 1.0f; // facing up in world space (model y is down)
        }
        if (normal[1] > 0.5f) {
            return 0.5f; // facing down
        }
        if (Math.abs(normal[2]) > 0.5f) {
            return 0.8f; // north/south
        }
        return 0.6f; // east/west
    }

    private static int clamp(int channel) {
        return Math.max(0, Math.min(255, channel));
    }

    private SoftwareRenderer() {
    }
}
