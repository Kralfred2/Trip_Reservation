package objectdata;

import attributes.Attribute;
import attributes.specific.SphereAttributes;
import controller.Point3D;

public class Sphere extends BaseObject {
    private final Point3D[] vertices;
    private final int[] indices;


    public Sphere() {
        this(new SphereAttributes());
    }


    public Sphere(SphereAttributes settings, Attribute<?>... globalAttrs) {
        super(settings, globalAttrs);


        int stacks = settings.stacks;
        int slices = settings.slices;
        float r = settings.radius;

        this.vertices = new Point3D[(stacks + 1) * (slices + 1)];
        this.indices = new int[stacks * slices * 6];

        generateMesh(r, stacks, slices);
    }

    private void generateMesh(float r, int stacks, int slices) {
        int vCount = 0;
        for (int i = 0; i <= stacks; i++) {
            float phi = (float) (Math.PI * i / stacks);
            for (int j = 0; j <= slices; j++) {
                float theta = (float) (2 * Math.PI * j / slices);
                float px = (float) (r * Math.sin(phi) * Math.cos(theta));
                float py = (float) (r * Math.cos(phi));
                float pz = (float) (r * Math.sin(phi) * Math.sin(theta));
                vertices[vCount++] = new Point3D(px, py, pz);
            }
        }

        int iCount = 0;
        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int first = (i * (slices + 1)) + j;
                int second = first + slices + 1;
                indices[iCount++] = first;
                indices[iCount++] = second;
                indices[iCount++] = first + 1;
                indices[iCount++] = second;
                indices[iCount++] = second + 1;
                indices[iCount++] = first + 1;
            }
        }
    }

    @Override protected Point3D[] getMeshVertices() { return vertices; }
    @Override protected int[] getMeshIndices() { return indices; }
}

