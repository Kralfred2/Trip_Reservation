package objectdata;

import controller.Point3D;

public class Tube extends BaseObject {
    private final Point3D[] vertices;
    private final int[] indices;

    public Tube(float x, float y, float z, float r, float h, int sides) {
        this.vertices = new Point3D[sides * 2];
        this.indices = new int[sides * 6];

        for (int i = 0; i < sides; i++) {
            double theta = 2.0 * Math.PI * i / sides;
            float px = (float) (r * Math.cos(theta));
            float pz = (float) (r * Math.sin(theta));

            vertices[i] = new Point3D(px, h/2, pz);
            vertices[i + sides] = new Point3D(px, -h/2, pz);

            int next = (i + 1) % sides;
            int offset = i * 6;
            indices[offset] = i; indices[offset+1] = next; indices[offset+2] = i + sides;
            indices[offset+3] = next; indices[offset+4] = next + sides; indices[offset+5] = i + sides;
        }
    }

    protected Point3D[] getMeshVertices() { return vertices; }
    protected int[] getMeshIndices() { return indices; }
}