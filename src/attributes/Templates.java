package attributes;


import controller.Point3D;
import math.Quaternion;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Templates {


    public static List<Attribute<?>> defaultTransform() {
        List<Attribute<?>> list = new ArrayList<>();
        list.add(new BasicAttribute<>("Position", new Point3D(0, 0, 3)));
        list.add(new BasicAttribute<>("Rotation", new Quaternion(1, 0, 0, 0)));
        list.add(new BasicAttribute<>("Scale", 0.2f));
        return list;
    }

    public static List<Attribute<?>> defaultRendering() {
        List<Attribute<?>> list = new ArrayList<>();
        list.add(new BasicAttribute<>("Wireframe", true));
        list.add(new BasicAttribute<>("Backface Culling", true));
        list.add(new BasicAttribute<>("Filled", true));
        list.add(new BasicAttribute<>("Base Color", Color.GRAY));
        return list;
    }
}