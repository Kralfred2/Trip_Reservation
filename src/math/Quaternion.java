package math;

import controller.Point3D;

public class Quaternion {
    public float w, x, y, z;

    public Quaternion(float w, float x, float y, float z) {
        this.w = w; this.x = x; this.y = y; this.z = z;
    }


    public static Quaternion fromAxisAngle(Point3D axis, float angle) {
        float s = (float) Math.sin(angle / 2);
        return new Quaternion(
                (float) Math.cos(angle / 2),
                axis.x * s,
                axis.y * s,
                axis.z * s
        );
    }


    public Quaternion multiply(Quaternion q) {
        return new Quaternion(
                w * q.w - x * q.x - y * q.y - z * q.z,
                w * q.x + x * q.w + y * q.z - z * q.y,
                w * q.y - x * q.z + y * q.w + z * q.x,
                w * q.z + x * q.y - y * q.x + z * q.w
        );
    }
    public static Quaternion fromEuler(Point3D euler) {
        // 1. Convert degrees to radians and get half-angles (as floats)
        float xRad = (float) Math.toRadians(euler.x) * 0.5f;
        float yRad = (float) Math.toRadians(euler.y) * 0.5f;
        float zRad = (float) Math.toRadians(euler.z) * 0.5f;

        // 2. Precompute sines and cosines
        float cx = (float) Math.cos(xRad);
        float sx = (float) Math.sin(xRad);
        float cy = (float) Math.cos(yRad);
        float sy = (float) Math.sin(yRad);
        float cz = (float) Math.cos(zRad);
        float sz = (float) Math.sin(zRad);

        // 3. Combine into final float components
        float w = cx * cy * cz + sx * sy * sz;
        float x = sx * cy * cz - cx * sy * sz;
        float y = cx * sy * cz + sx * cy * sz;
        float z = cx * cy * sz - sx * sy * cz;

        return new Quaternion(w, x, y, z);
    }
    public float[] toRotationMatrix() {
        float x2 = x * x; float y2 = y * y; float z2 = z * z;
        float xy = x * y; float xz = x * z; float yz = y * z;
        float wx = w * x; float wy = w * y; float wz = w * z;

        return new float[] {
                1 - 2 * (y2 + z2), 2 * (xy - wz),     2 * (xz + wy), // Row 0
                2 * (xy + wz),     1 - 2 * (x2 + z2), 2 * (yz - wx), // Row 1
                2 * (xz - wy),     2 * (yz + wx),     1 - 2 * (x2 + y2) // Row 2
        };
    }

    public Point3D rotate(Point3D v) {
        Quaternion p = new Quaternion(0, v.x, v.y, v.z);
        Quaternion conjugate = new Quaternion(w, -x, -y, -z);
        Quaternion result = this.multiply(p).multiply(conjugate);
        return new Point3D(result.x, result.y, result.z);
    }
    public Point3D toEuler() {
        float sinr_cosp = 2 * (w * x + y * z);
        float cosr_cosp = 1 - 2 * (x * x + y * y);
        float roll = (float) Math.atan2(sinr_cosp, cosr_cosp);

        float sinp = 2 * (w * y - z * x);
        float pitch = (float) (Math.abs(sinp) >= 1 ? Math.copySign(Math.PI / 2, sinp) : Math.asin(sinp));

        float siny_cosp = 2 * (w * z + x * y);
        float cosy_cosp = 1 - 2 * (y * y + z * z);
        float yaw = (float) Math.atan2(siny_cosp, cosy_cosp);

        return new Point3D(roll, pitch, yaw);
    }
}