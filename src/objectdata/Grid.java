package objectdata;

import attributes.specific.GridAttribute;
import controller.Point3D;
import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import rasterops.LineRasterizer;
import view.Camera;
import java.awt.Color;

public class Grid extends BaseObject {
    private final int size;
    private final float spacing;

    public Grid(int size, float spacing) {
        super(new GridAttribute(size, spacing));
        this.size = size;
        this.spacing = spacing;
    }

    @Override
    public void render(Camera camera, ColorBuffer cb, DepthBuffer db, int sw, int sh) {

        Point3D pos = (Point3D) transform.getAttributeValue("Position");
        float scale = (float) transform.getAttributeValue("Scale");


        float[] identityMatrix = new float[]{1,0,0, 0,1,0, 0,0,1};

        float halfSize = (size * spacing) / 2f;


        for (int i = 0; i <= size; i++) {
            float offset = (i * spacing) - halfSize;


            Point3D startX = new Point3D(-halfSize, 0, offset);
            Point3D endX   = new Point3D(halfSize, 0, offset);


            Point3D startZ = new Point3D(offset, 0, -halfSize);
            Point3D endZ   = new Point3D(offset, 0, halfSize);


            Point3D p1 = projectOptimized(startX, sw, sh, camera, identityMatrix, pos, scale);
            Point3D p2 = projectOptimized(endX, sw, sh, camera, identityMatrix, pos, scale);
            Point3D p3 = projectOptimized(startZ, sw, sh, camera, identityMatrix, pos, scale);
            Point3D p4 = projectOptimized(endZ, sw, sh, camera, identityMatrix, pos, scale);


            if (p1 != null && p2 != null) LineRasterizer.rasterize(cb, db, p1, p2, Color.DARK_GRAY);
            if (p3 != null && p4 != null) LineRasterizer.rasterize(cb, db, p3, p4, Color.DARK_GRAY);
        }
    }

    @Override protected Point3D[] getMeshVertices() { return new Point3D[0]; }
    @Override protected int[] getMeshIndices() { return new int[0]; }
}