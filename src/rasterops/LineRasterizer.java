package rasterops;

import controller.Point3D;
import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import java.awt.Color;


public class LineRasterizer {

    /**
     * Draws a 3D line using the Z-buffer for depth testing.
     */
    public static void rasterize(ColorBuffer cb, DepthBuffer db,
                                 Point3D p1, Point3D p2, Color color) {

        if(p1 == null || p2 == null){
            return;
        }

        int x1 = (int) p1.x;
        int y1 = (int) p1.y;
        int x2 = (int) p2.x;
        int y2 = (int) p2.y;

        float z1 = p1.z;
        float z2 = p2.z;

        int dx = Math.abs(x2 - x1);
        int dy = -Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx + dy;


        float steps = Math.max(dx, Math.abs(dy));
        float zStep = (steps == 0) ? 0 : (z2 - z1) / steps;
        float currentZ = z1;

        while (true) {
            if (cb.isValidAddress(x1, y1)) {
                if (currentZ < db.getDepth(x1, y1)) {
                    db.setDepth(x1, y1, currentZ);
                    cb.setPixel(x1, y1, color);
                }
            }

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x1 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y1 += sy;
            }

            currentZ += zStep;
        }
    }
}
