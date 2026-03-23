package attributes;

import controller.Point3D;
import math.Quaternion;
import objectdata.BaseObject;

import java.util.ArrayList;
import java.util.List;

public class AttributeHandler {

    public static List<Attribute<?>> getAllAttributes(BaseObject obj) {
        List<Attribute<?>> all = new ArrayList<>();
        for (AttributeGroup group : obj.getFeatureGroups()) {
            all.addAll(group.getAttributes());
        }
        return all;
    }

    public static void increment(Attribute<?> attr, float delta) {
        Object val = attr.getValue();


    }

    public static String format(Attribute<?> attr) {
        Object v = attr.getValue();
        if (v instanceof Point3D p) return String.format("%.1f, %.1f, %.1f", p.x, p.y, p.z);
        if (v instanceof Float f) return String.format("%.2f", f);
        if (v instanceof Quaternion q) return "RotData";
        return v.toString();
    }
}
