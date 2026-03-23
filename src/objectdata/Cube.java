package objectdata;

import attributes.Attribute;
import attributes.specific.CubeAttributes;
import controller.Point3D;

public class Cube extends BaseObject {

    public Cube(CubeAttributes settings, Attribute<?>... attrs) {
        super(settings, attrs);
    }

    private final int[] indices = {

            0, 1, 2,  0, 2, 3,

            4, 5, 6,  4, 6, 7,

            3, 2, 6,  3, 6, 7,

            0, 1, 5,  0, 5, 4,

            0, 3, 7,  0, 7, 4,

            1, 2, 6,  1, 6, 5
    };


    @Override
    protected Point3D[] getMeshVertices() {
        // Cast the settings to CubeSettings to get the size
        float s = ((CubeAttributes)settings).size / 2f;
        return new Point3D[]{
                new Point3D(-1, -1, -1), new Point3D(1, -1, -1),
                new Point3D(1, 1, -1), new Point3D(-1, 1, -1),
                new Point3D(-1, -1, 1), new Point3D(1, -1, 1),
                new Point3D(1, 1, 1), new Point3D(-1, 1, 1)
        };
        }


    @Override
    protected int[] getMeshIndices() {
        return indices;
    }
}
