package attributes;

import controller.Point3D;

public class Position extends Attribute<Point3D> {
    private Point3D value;

    public Position(String name, Point3D val) {
        super(name);
        this.value = val;
    }

    @Override public Point3D getValue() { return value; }
    @Override public void setValue(Point3D v) { this.value = v; }
}

