package objectdata;

import controller.Point3D;
import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import view.Camera;

import controller.Point3D;

public class Cube extends BaseObject {
    private final Point3D[] vertices;
    private final int[] indices = {

            0, 1, 2,  0, 2, 3,

            4, 5, 6,  4, 6, 7,

            3, 2, 6,  3, 6, 7,

            0, 1, 5,  0, 5, 4,

            0, 3, 7,  0, 7, 4,

            1, 2, 6,  1, 6, 5
    };

    public Cube(float x, float y, float z) {
        this.position = new Point3D(x, y, z);
        this.vertices = new Point3D[]{
                new Point3D(-1, -1, -1), new Point3D(1, -1, -1),
                new Point3D(1, 1, -1), new Point3D(-1, 1, -1),
                new Point3D(-1, -1, 1), new Point3D(1, -1, 1),
                new Point3D(1, 1, 1), new Point3D(-1, 1, 1)
        };
    }

    @Override
    protected Point3D[] getMeshVertices() {
        return vertices;
    }

    @Override
    protected int[] getMeshIndices() {
        return indices;
    }
}
