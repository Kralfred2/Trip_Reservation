package attributes.specific;

import java.util.Map;

public class SphereAttributes implements GeometrySettings {
    public float radius = 1.0f;
    public int stacks = 16;
    public int slices = 16;

    public SphereAttributes() {}
    public SphereAttributes(float radius,int stacks, int resolution) {
        this.radius = radius;
        this.slices = resolution;
        this.stacks = stacks;
    }

    @Override public String getSettingsName() { return "Sphere"; }
    @Override public void resetToDefaults() { this.radius = 1.0f; this.slices = 20; }
    @Override public Map<String, Object> serialize() {
        return Map.of("radius", radius, "res", slices);
    }
}
