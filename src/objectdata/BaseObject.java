package objectdata;

import attributes.Attribute;
import attributes.AttributeGroup;
import attributes.RenderingAttributes;
import attributes.TransformAttributes;
import attributes.specific.GeometrySettings;
import controller.Point3D;
import math.Quaternion;
import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import rasterops.LineRasterizer;
import rasterops.TriangleRasterizer;
import view.Camera;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public abstract class BaseObject implements Renderable {

    protected float velX, velY, velZ;
    protected GeometrySettings settings;
    protected TransformAttributes transform;
    protected List<AttributeGroup> featureGroups = new ArrayList<>();
    protected RenderingAttributes renderData = new RenderingAttributes(this);


    public BaseObject(GeometrySettings settings, Attribute<?>... attrs) {
        this.settings = settings;

        this.transform = new TransformAttributes(java.util.Arrays.asList(attrs));
        this.featureGroups.add(transform);
        this.featureGroups.add(new RenderingAttributes(this));
    }

    public BaseObject() {
        this.transform = new TransformAttributes();
        this.featureGroups.add(transform);

        this.featureGroups.add(new RenderingAttributes(this));
    }
    protected boolean selected = false;
    protected Color baseColor = Color.GRAY;

    protected boolean showAxes = false;

    public void setShowAxes(boolean show) { this.showAxes = show; }

    @Override
    public void setSelected(boolean selected) { this.selected = selected; }
    @Override
    public boolean isSelected() { return selected; }



    @Override
    public void setRotationSpeed(float vx, float vy, float vz) {
        this.velX = vx;
        this.velY = vy;
        this.velZ = vz;
    }

    @Override
    public void render(Camera camera, ColorBuffer cb, DepthBuffer db, int sw, int sh) {

        boolean isFilled = (boolean) renderData.getAttributeValue("Filled");
        boolean isWireframe = (boolean) renderData.getAttributeValue("Wireframe");
        boolean useCulling = (boolean) renderData.getAttributeValue("Backface Culling");

        Point3D[] verts = getMeshVertices();
        int[] indices = getMeshIndices();

        Quaternion rot = (Quaternion) transform.getAttributeValue("Rotation");
        Point3D pos = (Point3D) transform.getAttributeValue("Position");
        float scale = (float) transform.getAttributeValue("Scale");

        Point3D[] projected = new Point3D[verts.length];

        for (int i = 0; i < verts.length; i++) {
            projected[i] = projectOptimized(verts[i], sw, sh, camera, rot.toRotationMatrix(), pos, scale);
        }


        Color drawColor = selected ? Color.YELLOW : baseColor;
        if (showAxes || selected) {
            drawLocalAxes(camera, cb, db, sw, sh, rot.toRotationMatrix(), pos,scale);
        }

        for (int i = 0; i < indices.length; i += 3) {
            Point3D v1 = projected[indices[i]];
            Point3D v2 = projected[indices[i+1]];
            Point3D v3 = projected[indices[i+2]];

            if (v1 == null || v2 == null || v3 == null) continue;

            float nz = calculateNormalZ(v1, v2, v3);
            if (!useCulling || nz > 0) {
                if (isFilled) {
                    TriangleRasterizer.fillTriangle(cb, db, v1, v2, v3, drawColor);
                }
                if (isWireframe) {
                    Color lineColor = selected ? Color.WHITE : Color.LIGHT_GRAY;
                    LineRasterizer.rasterize(cb, db, v1, v2, lineColor);
                    LineRasterizer.rasterize(cb, db, v2, v3, lineColor);
                    LineRasterizer.rasterize(cb, db, v3, v1, lineColor);
                }
            }
        }
    }


    private void drawLocalAxes(Camera camera, ColorBuffer cb, DepthBuffer db, int sw, int sh,
                               float[] matrix, Point3D pos, float scale) {
        float axisSize = 2.0f; // The length of the lines

        Point3D origin = projectOptimized(new Point3D(0, 0, 0), sw, sh, camera, matrix, pos, scale);
        Point3D xPos   = projectOptimized(new Point3D(axisSize, 0, 0), sw, sh, camera, matrix, pos, scale);
        Point3D yPos   = projectOptimized(new Point3D(0, axisSize, 0), sw, sh, camera, matrix, pos, scale);
        Point3D zPos   = projectOptimized(new Point3D(0, 0, axisSize), sw, sh, camera, matrix, pos, scale);

        if (origin == null) return;

        if (xPos != null) LineRasterizer.rasterize(cb, db, origin, xPos, Color.RED);
        if (yPos != null) LineRasterizer.rasterize(cb, db, origin, yPos, Color.GREEN);
        if (zPos != null) LineRasterizer.rasterize(cb, db, origin, zPos, Color.BLUE);
    }

    protected abstract Point3D[] getMeshVertices();
    protected abstract int[] getMeshIndices();

    private float calculateNormalZ(Point3D v1, Point3D v2, Point3D v3) {
        return (v2.x - v1.x) * (v3.y - v1.y) - (v2.y - v1.y) * (v3.x - v1.x);
    }
    @Override
    public void update(float deltaTime) {
        if (velX != 0 || velY != 0 || velZ != 0) {
            Quaternion current = (Quaternion) transform.getAttributeValue("Rotation");

            if (current != null) {
                Quaternion delta = Quaternion.fromAxisAngle(new Point3D(1, 0, 0), velX * deltaTime)
                        .multiply(Quaternion.fromAxisAngle(new Point3D(0, 1, 0), velY * deltaTime))
                        .multiply(Quaternion.fromAxisAngle(new Point3D(0, 0, 1), velZ * deltaTime));

                transform.setAttribute("Rotation", delta.multiply(current));
            }
        }
    }
    protected Point3D projectOptimized(Point3D v, int sw, int sh, Camera cam,
                                       float[] m, Point3D pos, float scale) {
        // 1. Manual Matrix Multiplication (Rotation + Scaling)
        float rx = (m[0] * v.x + m[1] * v.y + m[2] * v.z) * scale;
        float ry = (m[3] * v.x + m[4] * v.y + m[5] * v.z) * scale;
        float rz = (m[6] * v.x + m[7] * v.y + m[8] * v.z) * scale;

        // 2. World Translation & Camera Offset
        float tx = (rx + pos.x) - cam.x;
        float ty = (ry + pos.y) - cam.y;
        float tz = (rz + pos.z) - cam.z;

        Point3D viewP = new Point3D(tx, ty, tz).rotateY(-cam.rotY).rotateX(-cam.rotX);

        if (viewP.z < 0.1f) return null;

        float fov = 400;
        float invZ = 1.0f / viewP.z;
        return new Point3D(
                (viewP.x * fov) * invZ + (sw / 2f),
                (viewP.y * fov) * invZ + (sh / 2f),
                viewP.z
        );
    }
    public List<AttributeGroup> getFeatureGroups() {
        return featureGroups;
    }

    protected Point3D project(Point3D vertex, int sw, int sh, Camera cam) {

        Quaternion currentRot = (Quaternion) transform.getAttributeValue("Rotation");
        Point3D currentPos = (Point3D) transform.getAttributeValue("Position");

        Point3D p = currentRot.rotate(vertex);

        float worldX = p.x + currentPos.x;
        float worldY = p.y + currentPos.y;
        float worldZ = p.z + currentPos.z;

        float tx = worldX - cam.x;
        float ty = worldY - cam.y;
        float tz = worldZ - cam.z;

        Point3D viewP = new Point3D(tx, ty, tz)
                .rotateY(-cam.rotY)
                .rotateX(-cam.rotX);

        if (viewP.z < 0.1f) return null;
        float fov = 400;
        float vZ = Math.max(viewP.z, 0.1f);

        float sx = (viewP.x * fov) / vZ + (sw / 2f);
        float sy = (viewP.y * fov) / vZ + (sh / 2f);

        return new Point3D(sx, sy, vZ);
    }
}
