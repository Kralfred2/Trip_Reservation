package objectdata;

import controller.Point3D;
import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import rasterops.LineRasterizer;
import rasterops.TriangleRasterizer;
import view.Camera;

import java.awt.*;

public abstract class BaseObject implements Renderable {
    protected Point3D position = new Point3D(0, 0, 0);
    protected float rotX, rotY, rotZ;
    protected float velX, velY, velZ;

    protected boolean selected = false;
    protected Color baseColor = Color.GRAY;

    protected boolean showAxes = false;

    public void setShowAxes(boolean show) { this.showAxes = show; }

    @Override
    public void setSelected(boolean selected) { this.selected = selected; }
    @Override
    public boolean isSelected() { return selected; }

    @Override
    public void update(float deltaTime) {
        rotX += velX * deltaTime;
        rotY += velY * deltaTime;
        rotZ += velZ * deltaTime;
    }

    @Override
    public void setRotationSpeed(float vx, float vy, float vz) {
        this.velX = vx;
        this.velY = vy;
        this.velZ = vz;
    }

    @Override
    public void render(Camera camera, ColorBuffer cb, DepthBuffer db, int sw, int sh,
                       boolean filled, boolean wireframe, boolean backFaceCulling) {


        Point3D[] verts = getMeshVertices();
        int[] indices = getMeshIndices();


        Point3D[] projected = new Point3D[verts.length];
        for (int i = 0; i < verts.length; i++) {
            projected[i] = project(verts[i], sw, sh, camera);
        }


        Color drawColor = selected ? Color.YELLOW : baseColor;
        if (showAxes || selected) {
            drawLocalAxes(camera, cb, db, sw, sh);
        }

        for (int i = 0; i < indices.length; i += 3) {
            Point3D v1 = projected[indices[i]];
            Point3D v2 = projected[indices[i+1]];
            Point3D v3 = projected[indices[i+2]];

            float nz = calculateNormalZ(v1, v2, v3);

            if (!backFaceCulling || nz > 0) {
                if (filled) {
                    TriangleRasterizer.fillTriangle(cb, db, v1, v2, v3, drawColor);
                }
                if (wireframe) {
                    Color lineColor = selected ? Color.WHITE : Color.LIGHT_GRAY;
                    LineRasterizer.rasterize(cb, db, v1, v2, lineColor);
                    LineRasterizer.rasterize(cb, db, v2, v3, lineColor);
                    LineRasterizer.rasterize(cb, db, v3, v1, lineColor);
                }
            }
        }
    }


    private void drawLocalAxes(Camera camera, ColorBuffer cb, DepthBuffer db, int sw, int sh) {
        float size = 2.0f;
        Point3D origin = project(new Point3D(0, 0, 0), sw, sh, camera);


        Point3D xPos = project(new Point3D(size, 0, 0), sw, sh, camera);
        Point3D yPos = project(new Point3D(0, size, 0), sw, sh, camera);
        Point3D zPos = project(new Point3D(0, 0, size), sw, sh, camera);


        LineRasterizer.rasterize(cb, db, origin, xPos, Color.RED);
        LineRasterizer.rasterize(cb, db, origin, yPos, Color.GREEN);
        LineRasterizer.rasterize(cb, db, origin, zPos, Color.BLUE);
    }

    protected abstract Point3D[] getMeshVertices();
    protected abstract int[] getMeshIndices();

    private float calculateNormalZ(Point3D v1, Point3D v2, Point3D v3) {
        return (v2.x - v1.x) * (v3.y - v1.y) - (v2.y - v1.y) * (v3.x - v1.x);
    }

    protected Point3D project(Point3D vertex, int sw, int sh, Camera cam) {
        // 1. Local Rotation
        Point3D p = vertex.rotateX(rotX).rotateY(rotY).rotateZ(rotZ);

        // 2. World Position
        float worldX = p.x + position.x;
        float worldY = p.y + position.y;
        float worldZ = p.z + position.z;

        // 3. View Transformation (Relative to Camera Position)
        float tx = worldX - cam.x;
        float ty = worldY - cam.y;
        float tz = worldZ - cam.z;

        // 4. CAMERA ROTATION (The missing step!)
        // Rotate the point around the camera using the INVERSE of camera angles
        Point3D viewP = new Point3D(tx, ty, tz)
                .rotateY(-cam.rotY)
                .rotateX(-cam.rotX);

        // 5. Perspective Projection
        float fov = 400;
        float vZ = Math.max(viewP.z, 0.1f);

        float sx = (viewP.x * fov) / vZ + (sw / 2f);
        float sy = (viewP.y * fov) / vZ + (sh / 2f);

        return new Point3D(sx, sy, vZ);
    }
}
