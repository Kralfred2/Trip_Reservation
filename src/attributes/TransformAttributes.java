package attributes;

import controller.Point3D;
import math.Quaternion;
import java.util.*;

public class TransformAttributes implements AttributeGroup {

    private final Map<String, Attribute<?>> registry = new LinkedHashMap<>();

    public TransformAttributes() {
        ensureDefaults();
    }

    public TransformAttributes(List<Attribute<?>> all) {
        if (all != null) {
            for (Attribute<?> attr : all) addAttribute(attr);
        }
        ensureDefaults();
    }

    public void ensureDefaults() {
        List<Attribute<?>> defaults = Templates.defaultTransform();
        for (Attribute<?> defaultAttr : defaults) {
            // Instant check instead of stream().anyMatch()
            if (!registry.containsKey(defaultAttr.getName())) {
                this.addAttribute(defaultAttr);
            }
        }
    }

    @Override
    public void addAttribute(Attribute<?> attr) {
        registry.put(attr.getName(), attr);
    }

    @Override
    public Object getAttributeValue(String name) {
        Attribute<?> attr = registry.get(name);
        return (attr != null) ? attr.getValue() : null;
    }
    @Override
    public List<Attribute<?>> getAttributes() {

        return new ArrayList<>(registry.values());
    }

    @Override
    public void setAttribute(String name, Object value) {
        Attribute<?> attr = registry.get(name);
        if (attr == null) return;


        if (name.equals("Rotation") && value instanceof Quaternion q) {
            ((Attribute<Quaternion>) attr).setValue(q);
        } else if (name.equals("Position") && value instanceof Point3D p) {
            ((Attribute<Point3D>) attr).setValue(p);
        } else if (name.equals("Scale") && value instanceof Float f) {
            ((Attribute<Float>) attr).setValue(f);
        }
    }

    @Override
    public List<String> getAttributeNames() {
        return new ArrayList<>(registry.keySet());
    }

    @Override
    public String getGroupName() { return "Transform"; }

    @Override public Object getAttribute(Object obj) { return null; }
    @Override public void setAttribute(Object value) { }
}
