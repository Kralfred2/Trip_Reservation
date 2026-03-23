package attributes.specific;

import java.util.Map;

public class TubeAttributes implements GeometrySettings {
    public int resolution = 32;
    public float radius = 1.0f;
    public float height = 2.0f;

    @Override
    public String getSettingsName() { return "Cylinder Configuration"; }

    @Override
    public void resetToDefaults() {
        this.resolution = 32;
        this.radius = 1.0f;
        this.height = 2.0f;
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of("res", resolution, "rad", radius, "h", height);
    }
}
