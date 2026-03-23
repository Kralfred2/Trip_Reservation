package attributes.specific;

import java.util.Map;

public class CubeAttributes implements GeometrySettings {
    public float size = 1.0f;

    public CubeAttributes() {}
    public CubeAttributes(float size) { this.size = size; }

    @Override public String getSettingsName() { return "Cube"; }
    @Override public void resetToDefaults() { this.size = 1.0f; }
    @Override public Map<String, Object> serialize() { return Map.of("size", size); }
}
