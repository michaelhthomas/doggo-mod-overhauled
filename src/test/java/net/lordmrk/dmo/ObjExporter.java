package net.lordmrk.dmo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

/**
 * Writes a baked model's faces as a Wavefront {@code .obj} plus a {@code .mtl} and texture {@code
 * .png}, so the doggo can be inspected in an external 3D viewer. Model coordinates are in blocks but
 * y-down, so y is negated to match a y-up viewer; UVs are converted to OBJ's bottom-left origin.
 */
final class ObjExporter {

    static void write(File dir, String name, List<SoftwareRenderer.Face> faces, BufferedImage texture) throws IOException {
        dir.mkdirs();
        ImageIO.write(texture, "png", new File(dir, name + ".png"));

        Files.writeString(new File(dir, name + ".mtl").toPath(),
                "newmtl " + name + "\n"
                        + "Ka 1.000 1.000 1.000\n"
                        + "Kd 1.000 1.000 1.000\n"
                        + "d 1.0\n"
                        + "illum 1\n"
                        + "map_Kd " + name + ".png\n",
                StandardCharsets.UTF_8);

        StringBuilder vertices = new StringBuilder();
        StringBuilder texcoords = new StringBuilder();
        StringBuilder normals = new StringBuilder();
        StringBuilder elements = new StringBuilder();
        int index = 0;
        for (SoftwareRenderer.Face face : faces) {
            int v = index * 4 + 1; // OBJ indices are 1-based; 4 vertices and 4 texcoords per face.
            int vn = index + 1;
            for (float[] p : face.pos()) {
                vertices.append(String.format(Locale.ROOT, "v %.5f %.5f %.5f%n", p[0], -p[1], p[2]));
            }
            for (float[] uv : face.uv()) {
                texcoords.append(String.format(Locale.ROOT, "vt %.6f %.6f%n",
                        uv[0] / SoftwareRenderer.TEX_W, 1.0f - uv[1] / SoftwareRenderer.TEX_H));
            }
            float[] n = face.normal();
            normals.append(String.format(Locale.ROOT, "vn %.5f %.5f %.5f%n", n[0], -n[1], n[2]));
            elements.append(String.format(Locale.ROOT, "f %d/%d/%d %d/%d/%d %d/%d/%d %d/%d/%d%n",
                    v, v, vn, v + 1, v + 1, vn, v + 2, v + 2, vn, v + 3, v + 3, vn));
            index++;
        }

        Files.writeString(new File(dir, name + ".obj").toPath(),
                "mtllib " + name + ".mtl\n"
                        + "o " + name + "\n"
                        + vertices + texcoords + normals
                        + "usemtl " + name + "\n"
                        + elements,
                StandardCharsets.UTF_8);
    }

    private ObjExporter() {
    }
}
