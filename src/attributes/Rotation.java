package attributes;

import controller.Point3D;
import math.Quaternion;

public class Rotation extends Attribute<Quaternion> {
    private Quaternion value;

    public Rotation(String name, Quaternion val) {
        super(name);
        this.value = val;
    }

    @Override public Quaternion getValue() { return value; }
    @Override public void setValue(Quaternion v) { this.value = v; }

}
