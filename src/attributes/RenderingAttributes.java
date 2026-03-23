package attributes;

import objectdata.BaseObject;
import java.awt.Color;
import java.util.*;

public class RenderingAttributes implements AttributeGroup {
    private final Map<String, Attribute<?>> registry = new LinkedHashMap<>();

    public RenderingAttributes(BaseObject owner) {
        for (Attribute<?> attr : Templates.defaultRendering()) {
            addAttribute(attr);
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

        if (value instanceof Boolean b) {
            ((Attribute<Boolean>) attr).setValue(b);
        } else if (value instanceof Color c) {
            ((Attribute<Color>) attr).setValue(c);
        }
    }

    @Override
    public List<String> getAttributeNames() {
        return new ArrayList<>(registry.keySet());
    }

    @Override
    public String getGroupName() { return "Appearance"; }

    @Override public Object getAttribute(Object obj) { return null; }
    @Override public void setAttribute(Object value) { /* Generic type setting */ }
}