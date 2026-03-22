package rasterops;

import controller.Point3D;
import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import java.awt.Color;



public class TriangleRasterizer {

    public static void fillTriangle(ColorBuffer cb, DepthBuffer db,
                                    Point3D p1, Point3D p2, Point3D p3, Color color) {

        // 1. Sort vertices by Y coordinate (y1 <= y2 <= y3)
        // We work with copies so we don't mess up the original points in the buffer
        Point3D v1 = p1;
        Point3D v2 = p2;
        Point3D v3 = p3;

        if (v1.y > v2.y) { Point3D t = v1; v1 = v2; v2 = t; }
        if (v1.y > v3.y) { Point3D t = v1; v1 = v3; v3 = t; }
        if (v2.y > v3.y) { Point3D t = v2; v2 = v3; v3 = t; }

        int y1 = (int) v1.y;
        int y2 = (int) v2.y;
        int y3 = (int) v3.y;

        // 2. Determine triangle type and draw
        if (y2 == y3) {
            fillFlatBottomTriangle(cb, db, v1, v2, v3, color);
        } else if (y1 == y2) {
            fillFlatTopTriangle(cb, db, v1, v2, v3, color);
        } else {
            // General triangle: split into two at the Y-level of v2
            float t = (v2.y - v1.y) / (v3.y - v1.y);
            float x4 = v1.x + t * (v3.x - v1.x);
            float z4 = v1.z + t * (v3.z - v1.z);
            Point3D v4 = new Point3D(x4, v2.y, z4);

            fillFlatBottomTriangle(cb, db, v1, v2, v4, color);
            fillFlatTopTriangle(cb, db, v2, v4, v3, color);
        }
    }

    private static void fillFlatBottomTriangle(ColorBuffer cb, DepthBuffer db,
                                               Point3D v1, Point3D v2, Point3D v3,
                                               Color color) {
        float invslope1 = (v2.x - v1.x) / (v2.y - v1.y);
        float invslope2 = (v3.x - v1.x) / (v3.y - v1.y);
        float zSlope1 = (v2.z - v1.z) / (v2.y - v1.y);
        float zSlope2 = (v3.z - v1.z) / (v3.y - v1.y);

        float curx1 = v1.x;
        float curx2 = v1.x;
        float curz1 = v1.z;
        float curz2 = v1.z;

        for (int scanlineY = (int)v1.y; scanlineY <= (int)v2.y; scanlineY++) {
            drawHorizontalLine(cb, db, (int)curx1, (int)curx2, scanlineY, curz1, curz2, color);
            curx1 += invslope1;
            curx2 += invslope2;
            curz1 += zSlope1;
            curz2 += zSlope2;
        }
    }

    private static void fillFlatTopTriangle(ColorBuffer cb, DepthBuffer db,
                                            Point3D v1, Point3D v2, Point3D v3,
                                            Color color) {
        float invslope1 = (v3.x - v1.x) / (v3.y - v1.y);
        float invslope2 = (v3.x - v2.x) / (v3.y - v2.y);
        float zSlope1 = (v3.z - v1.z) / (v3.y - v1.y);
        float zSlope2 = (v3.z - v2.z) / (v3.y - v2.y);

        float curx1 = v1.x;
        float curx2 = v2.x;
        float curz1 = v1.z;
        float curz2 = v2.z;

        for (int scanlineY = (int)v1.y; scanlineY <= (int)v3.y; scanlineY++) {
            drawHorizontalLine(cb, db, (int)curx1, (int)curx2, scanlineY, curz1, curz2, color);
            curx1 += invslope1;
            curx2 += invslope2;
            curz1 += zSlope1;
            curz2 += zSlope2;
        }
    }

    private static void drawHorizontalLine(ColorBuffer cb, DepthBuffer db, int x1, int x2, int y, float z1, float z2, Color color) {
        if (x1 > x2) {
            int tmpX = x1; x1 = x2; x2 = tmpX;
            float tmpZ = z1; z1 = z2; z2 = tmpZ;
        }
        float zStep = (x2 == x1) ? 0 : (z2 - z1) / (x2 - x1);
        float currentZ = z1;
        for (int x = x1; x <= x2; x++) {
            if (cb.isValidAddress(x, y)) {
                if (currentZ < db.getDepth(x, y)) {
                    db.setDepth(x, y, currentZ);
                    cb.setPixel(x, y, color);
                }
            }
            currentZ += zStep;
        }
    }
}
