package controller;

public class Point3D {
    public float x, y, z;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D rotateX(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        return new Point3D(x, y * cos - z * sin, y * sin + z * cos);
    }

    public Point3D rotateY(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        return new Point3D(x * cos + z * sin, y, -x * sin + z * cos);
    }

    public Point3D rotateZ(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        return new Point3D(x * cos - y * sin, x * sin + y * cos, z);
    }


    public static Point3D subtract(Point3D a, Point3D b) {
        return new Point3D(a.x - b.x, a.y - b.y, a.z - b.z);
    }
}
